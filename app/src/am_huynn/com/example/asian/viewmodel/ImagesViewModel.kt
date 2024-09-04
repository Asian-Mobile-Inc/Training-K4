package com.example.asian.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asian.model.Picture
import com.example.asian.repository.ImageRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {
    private val imageRepository by lazy {
        ImageRepository(app)
    }

    private val _pictures = MutableLiveData<MutableList<Picture>>()

    val pictures: LiveData<MutableList<Picture>> = _pictures

    fun getAllPicture() {
        val call = imageRepository.getImages()
        call.enqueue(object : Callback<MutableList<Picture>> {
            override fun onResponse(
                call: Call<MutableList<Picture>>, response: Response<MutableList<Picture>>
            ) {
                if (response.isSuccessful) {
                    val pictures = response.body() ?: mutableListOf()
                    _pictures.value = pictures
                } else {
                    Log.e("TAG", response.code().toString())
                }
            }

            override fun onFailure(call: Call<MutableList<Picture>>, t: Throwable) {
                Log.e("TAG", "Failure")
            }
        })
    }
}
