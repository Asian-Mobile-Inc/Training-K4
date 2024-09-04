package com.example.asian.repository

import android.app.Application
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService

class ImageRepository(private val app: Application) {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    fun getImages() = apiService.getPhotos()
}
