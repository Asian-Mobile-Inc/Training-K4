package com.example.asian.services.remote

import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers(Constants.HEADER_AUTH)
    @GET("images")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?,
    ): Response<MutableList<Picture>>

    @Multipart
    @Headers(Constants.HEADER_AUTH)
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<Picture>

    @Headers(Constants.HEADER_AUTH)
    @DELETE("images/{image_id}")
    suspend fun deleteImage(@Path("image_id") imageId: String): Response<Picture>
}
