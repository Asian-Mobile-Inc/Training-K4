package com.example.asian.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.asian.R
import com.example.asian.adapter.PagerAdapter
import com.example.asian.databinding.ActivityStorageBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.StorageViewModel
import com.google.android.material.tabs.TabLayoutMediator


class StorageActivity : AppCompatActivity() {
    private val binding: ActivityStorageBinding by lazy {
        ActivityStorageBinding.inflate(layoutInflater)
    }

    private val viewModel: StorageViewModel by viewModels()

    private var requestCode = 100

    private var editResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.confirmEditName()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.do_not_request_permission_edit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var deleteResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.deletePictures()
                Toast.makeText(
                    this, resources.getString(R.string.delete_successfully), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.do_not_request_permission_delete),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initControl()
        initObserver()
    }

    private fun initControl() {
        val pagerAdapter = PagerAdapter(this, onEdit)
        binding.vpPictures.adapter = pagerAdapter
        TabLayoutMediator(binding.tlPictures, binding.vpPictures) { tab, position ->
            if (position == 0) {
                tab.text = resources.getString(R.string.all)
            } else {
                tab.text = resources.getString(R.string.favorite)
            }
        }.attach()
    }

    private fun initListener() {
        with(binding) {
            btnShowAll.setOnClickListener {
                if (checkPermission()) {
                    viewModel.loadAllImage()
                } else {
                    askForPermission()
                }
            }

            btnGoToAppSetting.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }

            btnDelete.setOnClickListener {
                viewModel.listSelected.value?.let { value ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val pi = MediaStore.createDeleteRequest(contentResolver,
                            value.map { e -> Uri.parse(e.uri) })
                        val senderRequest = IntentSenderRequest.Builder(pi.intentSender).build()
                        deleteResultLauncher.launch(senderRequest)
                    }
                }
            }

            btnCancel.setOnClickListener { viewModel.cancelSelected() }
            btnSelectAll.setOnClickListener { viewModel.selectAll() }
        }
    }

    private fun initObserver() {
        viewModel.listSelected.observe(this) {
            binding.clLayoutSelected.isVisible = it.isNotEmpty()
            binding.tvSelected.text = getString(R.string.selected_int_param, it.size)
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askForPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    private val onEdit: (Picture, String) -> Unit = { pic, newName ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            viewModel.setPictureEdit(pic)
            viewModel.setNewName(newName)
            val pi = MediaStore.createWriteRequest(
                contentResolver, mutableListOf<Uri>(Uri.parse(pic.uri))
            )
            val senderRequest = IntentSenderRequest.Builder(pi.intentSender)
                .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0).build()
            editResultLauncher.launch(senderRequest)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadAllImage()
            } else {
                Toast.makeText(
                    this, resources.getText(R.string.permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
