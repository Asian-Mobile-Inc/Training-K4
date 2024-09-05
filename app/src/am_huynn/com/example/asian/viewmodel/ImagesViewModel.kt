package com.example.asian.viewmodel

import RealPathUtil
import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.R
import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import com.example.asian.repository.ImageRepository
import com.example.asian.utils.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {
    private val imageRepository by lazy {
        ImageRepository(app)
    }

    private val _isLoadingNetwork = MutableLiveData<Boolean>()

    val isLoadingNetwork: LiveData<Boolean> = _isLoadingNetwork

    private val _pictures = MutableLiveData<MutableList<Picture>>()

    val pictures: LiveData<MutableList<Picture>> = _pictures

    private val _favoritePictures = MutableLiveData<MutableList<Picture>>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            postValue(imageRepository.getFavoritePictures())
        }
    }

    val favoritePictures: LiveData<MutableList<Picture>> = _favoritePictures

    private val _localPictures = MutableLiveData<MutableList<Picture>>()

    val localPictures: LiveData<MutableList<Picture>> = _localPictures

    val dialogLoading = LoadingDialog(app)

    fun getAllPicture() = viewModelScope.launch(Dispatchers.IO) {
        _isLoadingNetwork.postValue(true)
        val response = imageRepository.getImages()
        if (response.isSuccessful) {
            val pictures = response.body() ?: mutableListOf()
            _isLoadingNetwork.postValue(false)
            _favoritePictures.value?.let {
                _pictures.postValue(pictures.map { pic ->
                    if (it.contains(pic.copy(favorite = true))) {
                        pic.copy(favorite = true)
                    } else {
                        pic
                    }
                }.toMutableList())
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    app,
                    app.getString(R.string.error_param, response.code(), response.message()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getLocalPictures() {
        val pictures = imageRepository.loadLocalPictures()
        _favoritePictures.value?.let {
            _localPictures.postValue(pictures.map { pic ->
                if (it.contains(pic.copy(favorite = true))) {
                    pic.copy(favorite = true)
                } else {
                    pic
                }
            }.toMutableList())
        }
    }

    fun favoriteNetworkPicture(picture: Picture) = viewModelScope.launch(Dispatchers.IO) {
        if (picture.favorite) {
            imageRepository.deleteFavoritePicture(picture)
            favoritePictures.value?.let {
                it.remove(picture)
                _favoritePictures.postValue(it)
            }
        } else {
            imageRepository.insertFavoritePicture(picture.copy(favorite = true))
            favoritePictures.value?.let {
                it.add(picture.copy(favorite = true))
                _favoritePictures.postValue(it)
            }
        }

        pictures.value?.let {
            _pictures.postValue(it.map { pic ->
                if (pic.imageId == picture.imageId) {
                    pic.copy(favorite = !(picture.favorite))
                } else {
                    pic
                }
            }.toMutableList())
        }
    }

    fun favoriteLocalPicture(picture: Picture) = viewModelScope.launch(Dispatchers.IO) {
        if (picture.favorite) {
            imageRepository.deleteFavoritePicture(picture)
            favoritePictures.value?.let {
                it.remove(picture)
                _favoritePictures.postValue(it)
            }
        } else {
            imageRepository.insertFavoritePicture(picture.copy(favorite = true))
            favoritePictures.value?.let {
                it.add(picture.copy(favorite = true))
                _favoritePictures.postValue(it)
            }
        }

        localPictures.value?.let {
            _localPictures.postValue(it.map { pic ->
                if (pic.imageId == picture.imageId) {
                    pic.copy(favorite = !(picture.favorite))
                } else {
                    pic
                }
            }.toMutableList())
        }
    }

    fun unFavoritePicture(picture: Picture) = viewModelScope.launch(Dispatchers.IO) {
        imageRepository.deleteFavoritePicture(picture)
        favoritePictures.value?.let {
            it.remove(picture)
            _favoritePictures.postValue(it)
        }
        localPictures.value?.let {
            _localPictures.postValue(it.map { pic ->
                if (pic.imageId == picture.imageId) {
                    pic.copy(favorite = !(picture.favorite))
                } else {
                    pic
                }
            }.toMutableList())
        }

        pictures.value?.let {
            _pictures.postValue(it.map { pic ->
                if (pic.imageId == picture.imageId) {
                    pic.copy(favorite = !(picture.favorite))
                } else {
                    pic
                }
            }.toMutableList())
        }
    }

    fun uploadImage(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        val realPath = RealPathUtil.getRealPath(app, uri)
        realPath?.let {
            val file = File(it)
            val requestBody = RequestBody.create(
                app.contentResolver.getType(uri)?.let { it1 -> MediaType.parse(it1) }, file
            )
            val imagePart =
                MultipartBody.Part.createFormData(Constants.KEY_IMAGE_DATA, file.name, requestBody)
            val response = imageRepository.uploadImage(imagePart)
            if (response.isSuccessful) {
                val picture: Picture? = response.body()
                if (picture != null) {
                    val values = _pictures.value
                    values?.let { list ->
                        list.add(0, picture)
                        _pictures.postValue(list)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        app,
                        app.getString(R.string.error_param, response.code(), response.message()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialogLoading.dismissDialog()
        }
    }

    fun deleteImage(picture: Picture) = viewModelScope.launch(Dispatchers.IO) {
        val response = imageRepository.deleteImage(picture.imageId)
        if (response.isSuccessful) {
            val list = _pictures.value
            list?.let {
                it.remove(picture)
                _pictures.postValue(it)
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    app,
                    app.getString(R.string.error_param, response.code(), response.message()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
