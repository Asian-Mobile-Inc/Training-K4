package com.example.asian.retrofit.api

import com.example.asian.retrofit.model.ImageModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val HEADER_AUTH = "Authorization: Bearer royvsrhBbaKa5Wr44s_eW6REBfi7W_23PBMvJ-T-pLA"

interface ApiService {
    @Headers(HEADER_AUTH)
    @GET("images")
    suspend fun getAllImage(@Query("per_page") perPage: Int): Response<MutableList<ImageModel>>

    @Headers(HEADER_AUTH)
    @GET("images")
    suspend fun loadMoreImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<MutableList<ImageModel>>

    @Headers(HEADER_AUTH)
    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageModel>

    @Headers(HEADER_AUTH)
    @DELETE("images/{image_id}")
    suspend fun deleteImage(
        @Path("image_id") imageId: String,
    ): Response<ImageModel>
}
