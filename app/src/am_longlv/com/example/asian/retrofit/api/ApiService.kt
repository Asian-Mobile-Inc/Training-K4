package com.example.asian.retrofit.api

import com.example.asian.retrofit.model.ImageModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("Authorization: Bearer royvsrhBbaKa5Wr44s_eW6REBfi7W_23PBMvJ-T-pLA")
    @GET("images")
    suspend fun getAllImage(): MutableList<ImageModel>
}