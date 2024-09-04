package com.example.asian.repository

import android.app.Application
import com.example.asian.constants.Constants
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService
import okhttp3.MultipartBody

class ImageRepository(private val app: Application) {
    private val apiService = ApiClient.retrofit(Constants.BASE_URL).create(ApiService::class.java)
    private val apiServiceUpload =
        ApiClient.retrofit(Constants.BASE_URL_UPLOAD).create(ApiService::class.java)

    fun getImages() = apiService.getPhotos()

    fun uploadImage(image: MultipartBody.Part) = apiServiceUpload.uploadImage(image)
}
