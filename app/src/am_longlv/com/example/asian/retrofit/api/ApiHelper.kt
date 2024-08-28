package com.example.asian.retrofit.api

import com.example.asian.retrofit.model.ImageModel

class ApiHelper(private val apiService: ApiService) {
    suspend fun getAllImages(): MutableList<ImageModel> {
        return apiService.getAllImage()
    }
}