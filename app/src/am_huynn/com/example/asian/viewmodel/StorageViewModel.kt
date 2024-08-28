package com.example.asian.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.model.Picture
import com.example.asian.model.PictureDataSource
import com.example.asian.repository.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StorageViewModel(app: Application) : AndroidViewModel(app) {
    private val pictureRepository: PictureRepository = PictureRepository(app)

    private var _pictures = MutableLiveData<MutableList<Picture>>().apply {
        value = mutableListOf()
    }
    val pictures: LiveData<MutableList<Picture>> = _pictures

    fun loadAllImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _pictures.postValue(pictureRepository.loadAllImage())
        }
    }

    fun savePicture(picture: Picture) {
        viewModelScope.launch(Dispatchers.IO) {
            if (picture.favorite) {
                _pictures.value?.let {
                    pictureRepository.insertRoomPicture(
                        picture,
                        it,
                        object : PictureDataSource.InsertDataCallback {
                            override fun insert() {
                                it.add(picture)
                                _pictures.postValue(it)
                            }

                            override fun update() {
                                for (i in it) {
                                    if (i.id == picture.id) {
                                        i.favorite = picture.favorite
                                        break
                                    }
                                }
                                _pictures.postValue(it)
                            }
                        })
                }
            } else {
                pictureRepository.deleteRoomPicture(picture)
                _pictures.value?.let {
                    for (i in it) {
                        if (i.id == picture.id) {
                            i.favorite = false
                            break
                        }
                    }
                    _pictures.postValue(it)
                }
            }
        }
    }
}