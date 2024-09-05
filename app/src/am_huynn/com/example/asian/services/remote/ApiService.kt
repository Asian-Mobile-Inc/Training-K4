package com.example.asian.services.remote

import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers(Constants.HEADER_AUTH)
    @GET("images")
    fun getPhotos(): Call<MutableList<Picture>>

    @Multipart
    @Headers(Constants.HEADER_AUTH)
    @POST("upload")
    fun uploadImage(@Part image: MultipartBody.Part): Call<Picture>

    @Headers(Constants.HEADER_AUTH)
    @DELETE("images/{image_id}")
    fun deleteImage(@Path("image_id") imageId: String): Call<Picture>
}
