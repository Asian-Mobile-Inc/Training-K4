package com.example.asian.services.remote

import com.example.asian.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("Authorization: Bearer 5FiuqhBFKuWZa8YG516uTRAyoqPTU4KfMMzoNt4jpMQ")
    @GET("images")
    fun getPhotos(): Call<List<Picture>>
}