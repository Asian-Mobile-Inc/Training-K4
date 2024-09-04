package com.example.asian.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.databinding.ActivityImagesBinding

class ImagesActivity : AppCompatActivity() {
    private val activityImagesBinding: ActivityImagesBinding by lazy {
        ActivityImagesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityImagesBinding.root)

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
}
