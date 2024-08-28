package com.example.asian.retrofit.http

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.gyazo.com/api"
    private val gson = GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create()
    val retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(RetrofitClient::class.java)
}