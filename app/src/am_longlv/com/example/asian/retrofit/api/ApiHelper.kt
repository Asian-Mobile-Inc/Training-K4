package com.example.asian.retrofit.api

import com.example.asian.retrofit.model.ImageModel
import okhttp3.MultipartBody
import retrofit2.Response

class ApiHelper(private val apiService: ApiService) {
    suspend fun getAllImages(): Response<MutableList<ImageModel>> {
        return apiService.getAllImage()
    }

    suspend fun uploadImage(image: MultipartBody.Part): Response<ImageModel> {
        return apiService.uploadImage(image)
    }

    suspend fun deleteImage(imageId: String): Response<ImageModel> {
        return apiService.deleteImage(imageId)
    }
}
