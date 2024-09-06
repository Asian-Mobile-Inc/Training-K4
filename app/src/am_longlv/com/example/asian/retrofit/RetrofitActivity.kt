package com.example.asian.retrofit

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.asian.R
import com.example.asian.databinding.ActivityRetrofitBinding
import com.example.asian.databinding.DialogBottomSelectImageBinding
import com.example.asian.retrofit.adapter.TabName
import com.example.asian.retrofit.adapter.ViewPagerRetrofitAdapter
import com.example.asian.retrofit.broadcast.InternetBroadcast
import com.example.asian.retrofit.utils.Constant
import com.example.asian.retrofit.viewmodel.RetrofitViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.FileNotFoundException

class RetrofitActivity : AppCompatActivity() {
    private val binding: ActivityRetrofitBinding by lazy {
        ActivityRetrofitBinding.inflate(layoutInflater)
    }
    private val retrofitViewModel: RetrofitViewModel by viewModels()
    private val permissionNameList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    private val dialogSelectImageBinding: DialogBottomSelectImageBinding by lazy {
        DialogBottomSelectImageBinding.inflate(layoutInflater)
    }
    private val dialogSelectImage by lazy {
        BottomSheetDialog(this).apply {
            setContentView(dialogSelectImageBinding.root)
            setCancelable(false)
        }
    }
    private val dialogLoadingRetrofit by lazy {
        Dialog(this).apply {
            setContentView(R.layout.dialog_loading_retrofit)
            setCancelable(false)
        }
    }
    private var imageUri = Uri.parse("")
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null
                && intent.action == Constant.ACTION_INTERNET_CHANGE
            ) {
                if (intent.getBooleanExtra(Constant.KEY_INTERNET_CHANGE, false)) {
                    if (retrofitViewModel.getListItemRetrofit().size == 0) {
                        retrofitViewModel.fetchAllImages(applicationContext)
                    } else if (retrofitViewModel.isLoading) {
                        retrofitViewModel.isLoading = false
                        retrofitViewModel.loadMore()
                    }
                    retrofitViewModel.reDownloadImage(applicationContext)
                } else {
                    NotificationManagerCompat.from(context)
                        .cancel(Constant.NOTIFICATION_DOWNLOAD_ID)
                }
            }
        }
    }
    private val broadcastInternet = InternetBroadcast()
    private val vpAdapter = ViewPagerRetrofitAdapter(this)
    private val downloadNotification by lazy {
        NotificationCompat.Builder(this, Constant.CHANNEL_DOWNLOAD_ID).apply {
            setContentTitle(getString(R.string.channel_name_download))
            setContentText(getString(R.string.channel_description_download))
            setSmallIcon(R.drawable.ic_download)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initObserver()
        askPermissions()
        initListener()
        registerBroadcast()
        setupTabLayout()
        createNotificationChannel()
    }

    private fun initObserver() {
        retrofitViewModel.statusRetrofitCallback.observe(this) {
            it?.let { sub ->
                handleCallbackRetrofit(sub)
            }
        }
        retrofitViewModel.progressDownload.observe(this) {
            setupProgressNotification(it)
        }
    }

    private fun handleCallbackRetrofit(sub: Int) {
        when (sub) {
            Constant.STATUS_CODE_NO_INTERNET -> {
                Toast.makeText(
                    this,
                    getString(R.string.error_no_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_START_DOWNLOAD -> {
                if (!dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.show()
                }
            }

            Constant.STATUS_CODE_OTHER_EXCEPTION -> {
                Toast.makeText(
                    this,
                    getString(R.string.other_exception),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_SHOW_DIALOG_LOADING -> if (!dialogLoadingRetrofit.isShowing) {
                dialogLoadingRetrofit.show()
            }

            Constant.STATUS_CODE_SHOW_DIALOG_LOAD_MORE -> {
                binding.pbLoadMore.visibility = View.VISIBLE
            }

            Constant.STATUS_CODE_HIDE_DIALOG_LOAD_MORE -> {
                if (!retrofitViewModel.isLoading) {
                    binding.pbLoadMore.visibility = View.GONE
                }
            }

            Constant.STATUS_CODE_OK -> {
            }

            Constant.STATUS_CODE_NO_PICK_IMAGE -> {
                Toast.makeText(
                    this,
                    getString(R.string.no_pick_image),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_NO_ITEM_MORE -> {
                Toast.makeText(
                    this,
                    getString(R.string.no_item_more),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_EXISTS_IMAGE_API -> {
                Toast.makeText(
                    this,
                    getString(R.string.image_exists_api),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                Toast.makeText(
                    this,
                    getString(R.string.error_status_int_param).format(sub),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (sub != Constant.STATUS_CODE_SHOW_DIALOG_LOADING && sub != Constant.STATUS_CODE_START_DOWNLOAD) {
            if (dialogLoadingRetrofit.isShowing) {
                dialogLoadingRetrofit.dismiss()
            }
        }
    }

    private fun setupProgressNotification(pr: Int) {
        if (pr > 99) {
            downloadNotification.setContentText(getString(R.string.download_finished))
                .setProgress(0, 0, false)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this)
                    .notify(Constant.NOTIFICATION_DOWNLOAD_ID, downloadNotification.build())
            }
        } else if (pr > 0) {
            downloadNotification.setContentText(getString(R.string.channel_description_download))
            downloadNotification.setProgress(100, pr, false)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this)
                    .notify(Constant.NOTIFICATION_DOWNLOAD_ID, downloadNotification.build())
            }
        }
    }

    private fun initListener() {
        binding.fabUpload.setOnClickListener {
            dialogSelectImage.show()
        }
        with(dialogSelectImageBinding) {
            ivUpload.setOnClickListener {
                selectImage()
            }
            btnCancel.setOnClickListener {
                dialogSelectImage.dismiss()
            }
            btnUpload.setOnClickListener {
                retrofitViewModel.uploadImage(imageUri, applicationContext)
                dialogSelectImage.dismiss()
            }
        }
    }

    private fun askPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                permissionNameList.toTypedArray(),
                Constant.REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun hasPermissions() = permissionNameList.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val canShowPermissions = permissionNameList.all {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }
        if (!canShowPermissions) {
            if (requestCode == Constant.REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrofitViewModel.fetchImagesFromLocal(applicationContext)
            } else {
                showSnakeBarAskPermission()
            }
        }
    }

    private fun setupTabLayout() {
        binding.vpStorage.apply {
            binding.vpStorage.offscreenPageLimit = 1
            binding.vpStorage.adapter = vpAdapter
        }
        TabLayoutMediator(
            binding.tlStorage, binding.vpStorage
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = TabName.getName(position)
        }.attach()
        retrofitViewModel.fetchAllImages(applicationContext)
    }

    private fun selectImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultSelectImageLauncher.launch(intent)
    }

    private var resultSelectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    imageUri = result.data?.data
                    dialogSelectImageBinding.ivUpload.setImageURI(imageUri)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.no_pick_image), Toast.LENGTH_SHORT).show()
            }
        }

    private fun registerBroadcast() {
        val intentFilter = IntentFilter(Constant.ACTION_CONNECTIVITY_CHANGE)
        val iFActionInternet = IntentFilter(Constant.ACTION_INTERNET_CHANGE)
        ContextCompat.registerReceiver(
            applicationContext,
            broadcastInternet,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        ContextCompat.registerReceiver(
            applicationContext,
            broadcastReceiver,
            iFActionInternet,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun showSnakeBarAskPermission() {
        val snackBar = Snackbar.make(
            binding.root,
            resources.getString(
                R.string.message_no_permission
            ),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(resources.getString(R.string.setting)) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts(
                "package",
                this.packageName, null
            )
            intent.setData(uri)
            permissionActivityResultLauncher.launch(intent)
        }
        snackBar.show()
    }

    private var permissionActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (hasPermissions()) {
                retrofitViewModel.fetchImagesFromLocal(applicationContext)
            }
        }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name_download)
            val descriptionText = getString(R.string.channel_description_download)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(Constant.CHANNEL_DOWNLOAD_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
