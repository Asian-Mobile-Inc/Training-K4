package com.example.asian.retrofit.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.asian.retrofit.api.ApiHelper
import com.example.asian.retrofit.api.RetrofitBuilder
import kotlinx.coroutines.launch

class RetrofitViewModel(application: Application) : AndroidViewModel(application) {
    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)

    init {
        fetchImages()
    }

    internal fun fetchImages() {
        viewModelScope.launch {
            try {
                val usersFromApi = apiHelper.getAllImages()
                for (i in usersFromApi) {
                    Log.d("androidruntime", i.url)
                }
            } catch (_: Exception) {
            }
        }
    }
}