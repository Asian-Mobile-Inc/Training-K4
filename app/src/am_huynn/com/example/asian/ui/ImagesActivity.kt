package com.example.asian.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asian.adapter.GridSpacingItemDecoration
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.ActivityImagesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.ImagesViewModel

class ImagesActivity : AppCompatActivity() {
    private val binding: ActivityImagesBinding by lazy {
        ActivityImagesBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagesViewModel by viewModels()

    private val picturesAdapter by lazy {
        PicturesAdapter(onItemClick)
    }

    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult>() {
                val uri = it.data?.data
                if (uri != null) {
                    viewModel.uploadImage(uri)
                } else {
                    Log.e("TAG", "no ")
                }
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initControls()
        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.getAllPicture()
        viewModel.pictures.observe(this, Observer {
            picturesAdapter.setData(it)
        })
    }

    private fun initControls() {
        binding.rvPictures.adapter = picturesAdapter
        binding.rvPictures.layoutManager = GridLayoutManager(this, 3)
        binding.rvPictures.addItemDecoration(GridSpacingItemDecoration(14))
        binding.rvPictures.itemAnimator = null
    }

    private val onItemClick: (Picture) -> Unit = {}

    private fun initListener() {
        binding.fbPickImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            pickImageResultLauncher.launch(Intent.createChooser(intent, "pick image"))
        }
    }
}
