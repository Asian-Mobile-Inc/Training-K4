package com.example.asian.retrofit.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://api.gyazo.com/api/"
    private val gson = GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create()
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}