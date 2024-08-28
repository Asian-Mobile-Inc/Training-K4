package com.example.asian.retrofit.http

import com.example.asian.retrofit.model.ImageModel
import retrofit2.http.GET

interface ImageApiService {
    @GET("images")
    suspend fun getAllImage(): MutableList<ImageModel>

}