package com.example.asian.retrofit.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {
    private const val BASE_URL = "https://api.gyazo.com/api/"
    private const val UPLOAD_URL = "https://upload.gyazo.com/api/"
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    private fun getRetrofitUpload(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UPLOAD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    val apiServiceUpload: ApiService = getRetrofitUpload().create(ApiService::class.java)
}
