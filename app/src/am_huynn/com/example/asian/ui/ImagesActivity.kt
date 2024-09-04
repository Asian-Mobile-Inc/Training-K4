package com.example.asian.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asian.R
import com.example.asian.adapter.GridSpacingItemDecoration
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.constants.Constants
import com.example.asian.databinding.ActivityImagesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.ImagesViewModel

class ImagesActivity : AppCompatActivity() {
    private val binding: ActivityImagesBinding by lazy {
        ActivityImagesBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagesViewModel by viewModels()

    private val picturesAdapter by lazy {
        PicturesAdapter(onItemClick, onItemDelete)
    }

    private val pickImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val uri = it.data?.data
        if (uri != null) {
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
        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.getAllPicture()
        viewModel.pictures.observe(this) {
            picturesAdapter.setData(it)
        }
    }

    private fun initControls() {
        binding.rvPictures.apply {
            adapter = picturesAdapter
            layoutManager = GridLayoutManager(this@ImagesActivity, 3)
            addItemDecoration(GridSpacingItemDecoration(14))
            itemAnimator = null
        }
    }

    private val onItemClick: (Picture) -> Unit = {}

    private val onItemDelete: (Picture) -> Unit = {
        val dialogBuilder = AlertDialog.Builder(this)
        with(dialogBuilder) {
            setMessage(resources.getText(R.string.do_you_want_delete_image))
            setPositiveButton(resources.getText(R.string.yes)) { _, _ ->
                viewModel.deleteImage(it)
            }
            setNegativeButton(resources.getText(R.string.no)) { _, _ -> }
        }.create().show()
    }

    private fun initListener() {
        binding.fbPickImage.setOnClickListener {
            if (checkPermission()) {
                pickImage()
            } else {
                askForPermission()
            }
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pickImageResultLauncher.launch(Intent.createChooser(intent, "pick image"))
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
        ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                Toast.makeText(
                    this, resources.getText(R.string.permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
