package com.example.asian.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.ActivityImagesBinding
import com.example.asian.model.Picture

class ImagesActivity : AppCompatActivity() {
    private val binding: ActivityImagesBinding by lazy {
        ActivityImagesBinding.inflate(layoutInflater)
    }

    private val picturesAdapter by lazy {
        PicturesAdapter(onItemClick)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initControls()

//        val apiService = ApiClient.retrofit.create(ApiService::class.java)
//        val call = apiService.getPhotos()
//
//        call.enqueue(object : Callback<List<Picture>> {
//            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>) {
//                if (response.isSuccessful) {
//                    val pictures = response.body() ?: emptyList()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
//                Log.e("TAG", "onFailure: ")
//            }
//
//        })
    }

    private fun initControls() {
        binding.rvPictures.adapter = picturesAdapter
        binding.rvPictures.layoutManager = GridLayoutManager(this, 3)
        binding.rvPictures.itemAnimator = null
    }

    private val onItemClick: (Picture) -> Unit = {

    }
}
