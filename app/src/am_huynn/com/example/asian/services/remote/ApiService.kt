package com.example.asian.services.remote

import com.example.asian.model.Picture
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: Bearer 5FiuqhBFKuWZa8YG516uTRAyoqPTU4KfMMzoNt4jpMQ")
    @GET("images")
    fun getPhotos(): Call<MutableList<Picture>>

    @Multipart
    @Headers("Authorization: Bearer 5FiuqhBFKuWZa8YG516uTRAyoqPTU4KfMMzoNt4jpMQ")
    @POST("upload")
    fun uploadImage(@Part image: MultipartBody.Part): Call<Picture>
}