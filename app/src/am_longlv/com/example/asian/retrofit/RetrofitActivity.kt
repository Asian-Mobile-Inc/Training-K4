package com.example.asian.retrofit

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.FileNotFoundException

class RetrofitActivity : AppCompatActivity() {
    private val mBinding: ActivityRetrofitBinding by lazy {
        ActivityRetrofitBinding.inflate(layoutInflater)
    }
    private val mRetrofitViewModel: RetrofitViewModel by viewModels()
    private val mPermissionNameList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(Manifest.permission.READ_MEDIA_IMAGES)
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
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null
                && intent.action == Constant.ACTION_INTERNET_CHANGE
                && intent.getBooleanExtra(Constant.KEY_INTERNET_CHANGE, false)
            ) {
                if (mRetrofitViewModel.getListItemRetrofit().size == 0) {
                    mRetrofitViewModel.fetchAllImages(applicationContext)
                }
                if (mRetrofitViewModel.mIsLoading) {
                    mRetrofitViewModel.loadMore()
                }
            }
        }
    }
    private val broadcastInternet = InternetBroadcast()
    private val vpAdapter = ViewPagerRetrofitAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initObserver()
        askPermissions()
        initListener()
        registerBroadcast()
    }

    private fun initObserver() {
        mRetrofitViewModel.statusRetrofitCallback.observe(this) {
            it?.let { sub ->
                handlerCallbackRetrofit(sub)
            }
        }
    }

    private fun handlerCallbackRetrofit(sub: Int) {
        when (sub) {
            Constant.STATUS_CODE_NO_INTERNET -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
                Toast.makeText(
                    this,
                    getString(R.string.error_no_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_OTHER_EXCEPTION -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
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
                mBinding.pbLoadMore.visibility = View.VISIBLE
            }

            Constant.STATUS_CODE_HIDE_DIALOG_LOAD_MORE -> {
                if (!mRetrofitViewModel.mIsLoading) {
                    mBinding.pbLoadMore.visibility = View.GONE
                }
            }

            Constant.STATUS_CODE_OK -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
            }

            Constant.STATUS_CODE_NO_PICK_IMAGE -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
                Toast.makeText(
                    this,
                    getString(R.string.no_pick_image),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_NO_ITEM_MORE -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
                Toast.makeText(
                    this,
                    getString(R.string.no_item_more),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Constant.STATUS_CODE_EXISTS_IMAGE_API -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
                Toast.makeText(
                    this,
                    getString(R.string.image_exists_api),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                if (dialogLoadingRetrofit.isShowing) {
                    dialogLoadingRetrofit.dismiss()
                }
                Toast.makeText(
                    this,
                    getString(R.string.error_status_int_param).format(sub),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initListener() {
        mBinding.fabUpload.setOnClickListener {
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
                mRetrofitViewModel.uploadImage(imageUri, this@RetrofitActivity)
                dialogSelectImage.dismiss()
            }
        }
    }

    private fun askPermissions() {
        if (hasPermissions()) {
            setupTabLayout()
            mRetrofitViewModel.fetchAllImages(applicationContext)
        } else {
            ActivityCompat.requestPermissions(
                this,
                mPermissionNameList.toTypedArray(),
                Constant.REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun hasPermissions() = mPermissionNameList.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupTabLayout()
                mRetrofitViewModel.fetchAllImages(applicationContext)
            } else {
                finish()
            }
        }
    }

    private fun setupTabLayout() {
        mBinding.vpStorage.apply {
            mBinding.vpStorage.offscreenPageLimit = 1
            mBinding.vpStorage.adapter = vpAdapter
        }
        TabLayoutMediator(
            mBinding.tlStorage, mBinding.vpStorage
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = TabName.getName(position)
        }.attach()
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
            mBroadcastReceiver,
            iFActionInternet,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val parcelable = vpAdapter.saveState()
        outState.putParcelable("Adapter", parcelable)
    }
}
