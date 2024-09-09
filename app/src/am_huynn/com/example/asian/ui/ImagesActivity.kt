package com.example.asian.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.asian.R
import com.example.asian.adapter.PagerImageAdapter
import com.example.asian.constants.Constants
import com.example.asian.databinding.ActivityImagesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.ImagesViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ImagesActivity : AppCompatActivity() {
    private val binding: ActivityImagesBinding by lazy {
        ActivityImagesBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagesViewModel by viewModels()

    private val pickImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val uri = it.data?.data
        if (uri != null) {
            viewModel.dialogLoading.startLoadingDialog(this)
            viewModel.uploadImage(uri)
        } else {
            Toast.makeText(
                this, resources.getText(R.string.can_not_pick_image), Toast.LENGTH_SHORT
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initControls()
        initListener()
    }

    private fun initControls() {
        val pagerImageAdapter = PagerImageAdapter(this, onDownLoad)
        binding.vpImages.adapter = pagerImageAdapter
        TabLayoutMediator(binding.tlTabImages, binding.vpImages) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.network)
                1 -> tab.text = resources.getString(R.string.local)
                2 -> tab.text = resources.getString(R.string.favorite)
            }
        }.attach()
    }

    private fun initListener() {
        with(binding) {
            btnShowAll.setOnClickListener {
                if (checkPermission()) {
                    viewModel.getAllPicture()
                } else {
                    askForPermission()
                }
            }

            fbPickImage.setOnClickListener {
                if (checkPermission()) {
                    pickImage()
                } else {
                    askForPermission()
                }
            }
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pickImageResultLauncher.launch(Intent.createChooser(intent, "pick image"))
    }

    private val onDownLoad: (Picture) -> Unit = {
        viewModel.setPictureDownload(it)
        if (checkPermissionWrite()) {
            viewModel.dialogLoading.startLoadingDialog(this)
            viewModel.downloadImage()
        } else {
            askForPermissionWrite()
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

    private fun checkPermissionWrite(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_CODE)
    }

    private fun askForPermissionWrite() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_WRITE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getAllPicture()
            } else {
                Toast.makeText(
                    this, resources.getText(R.string.permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (requestCode == Constants.REQUEST_CODE_WRITE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.downloadImage()
            } else {
                Toast.makeText(
                    this, resources.getText(R.string.permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
