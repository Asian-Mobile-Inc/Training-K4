package com.example.asian.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asian.model.Picture
import com.example.asian.repository.ImageRepository

class StorageViewModel(app: Application) : AndroidViewModel(app) {
    private val imageRepository: ImageRepository = ImageRepository(app)

    private var _pictures = MutableLiveData<MutableList<Picture>>().apply {
        value = mutableListOf()
    }
    val pictures: LiveData<MutableList<Picture>> = _pictures

    fun loadAllImage() {
        _pictures.value = imageRepository.loadAllImage()
    }
}