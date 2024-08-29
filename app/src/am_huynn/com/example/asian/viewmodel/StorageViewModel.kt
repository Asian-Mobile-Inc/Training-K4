package com.example.asian.viewmodel

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.model.Picture
import com.example.asian.model.PictureDataSource
import com.example.asian.repository.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StorageViewModel(private val app: Application) : AndroidViewModel(app) {
    private val pictureRepository: PictureRepository = PictureRepository(app)

    private var _pictures = MutableLiveData<MutableList<Picture>>().apply {
        value = mutableListOf()
    }
    val pictures: LiveData<MutableList<Picture>> = _pictures

    private var pictureEdit: Picture? = null
    private var newName: String? = null

    fun setPictureEdit(picture: Picture) {
        pictureEdit = picture
    }

    fun setNewName(newName: String) {
        this.newName = newName
    }

    fun loadAllImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _pictures.postValue(pictureRepository.loadAllImage())
        }
    }

    fun confirmEditName() {
        pictureEdit?.let {
            val cv = ContentValues()
            val ext = it.name.substring(it.name.indexOf("."), it.name.length)
            cv.put(MediaStore.Files.FileColumns.DISPLAY_NAME, newName)
            app.contentResolver.update(
                Uri.parse(it.uri), cv, "${MediaStore.Video.Media._ID}=${it.id}", null
            )
            updateUiPicture(it.id, newName + ext, null)
        }
    }

    private fun updateUiPicture(id: Long, name: String?, favorite: Boolean?) {
        _pictures.value?.let { list ->
            for (i in list) {
                if (i.id == id) {
                    if (name != null) i.name = name
                    if (favorite != null) i.favorite = favorite
                    break
                }
            }
            _pictures.postValue(list)
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
                updateUiPicture(picture.id, null, false)
            }
        }
    }
}
