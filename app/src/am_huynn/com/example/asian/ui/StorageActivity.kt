package com.example.asian.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.ActivityStorageBinding
import com.example.asian.databinding.DialogShowImageBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.StorageViewModel

class StorageActivity : AppCompatActivity() {
    private val binding: ActivityStorageBinding by lazy {
        ActivityStorageBinding.inflate(layoutInflater)
    }

    private val viewModel: StorageViewModel by viewModels()

    private val picturesAdapter: PicturesAdapter by lazy {
        PicturesAdapter(onClick)
    }

    private var requestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initControl()
        initObserver()
    }

    private fun initControl() {
        binding.rvPictures.layoutManager = GridLayoutManager(this, 3)

        binding.rvPictures.adapter = picturesAdapter
        binding.rvPictures.itemAnimator = null
    }

    private fun initObserver() {
        viewModel.pictures.observe(this) {
            picturesAdapter.setData(it)
        }
    }

    private fun initListener() {
        binding.btnShowAll.setOnClickListener {
            if (checkPermission()) {
                viewModel.loadAllImage()
            } else {
                askForPermission()
            }
        }

        binding.btnGoToAppSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private val onClick: (Picture) -> Unit = {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogShowImageBinding.inflate(LayoutInflater.from(this))

        dialog.apply {
            setView(dialogBinding.root)
            with(dialogBinding) {
                Glide.with(context).load(it.uri).into(dialogBinding.ivPictureDialog)
                tvNamePicture.text = it.name
                btnCancel.setOnClickListener { dismiss() }
            }
        }.show()
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
