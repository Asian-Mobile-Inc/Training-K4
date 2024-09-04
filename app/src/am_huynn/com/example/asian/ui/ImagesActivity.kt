package com.example.asian.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.R
import com.example.asian.model.Picture
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val call = apiService.getPhotos()

        call.enqueue(object : Callback<List<Picture>> {
            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>) {
                if (response.isSuccessful) {
                    val pictures = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
                Log.e("TAG", "onFailure: ")
            }

        })
    }
}
