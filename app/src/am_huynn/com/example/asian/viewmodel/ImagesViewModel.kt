package com.example.asian.viewmodel

import RealPathUtil
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
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
import java.util.concurrent.Executors

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {
    private val imageRepository by lazy {
        ImageRepository(app)
    }

    private val _isLoadingNetwork = MutableLiveData<Boolean>()

    val isLoadingNetwork: LiveData<Boolean> = _isLoadingNetwork

    private val _pictures = MutableLiveData<MutableList<Picture>>()

    val pictures: LiveData<MutableList<Picture>> = _pictures

    private val _favoritePictures = MutableLiveData<MutableList<Picture>>().apply {
        postValue(mutableListOf())
    }

    val favoritePictures: LiveData<MutableList<Picture>> = _favoritePictures

    private val _localPictures = MutableLiveData<MutableList<Picture>>()

    val localPictures: LiveData<MutableList<Picture>> = _localPictures

    val dialogLoading = LoadingDialog(app)

    private lateinit var pictureDownload: Picture

    private val myExecutor = Executors.newSingleThreadExecutor()
    private val myHandler = Handler(Looper.getMainLooper())

    fun setPictureDownload(pic: Picture) {
        pictureDownload = pic
    }

    fun getAllPicture() = viewModelScope.launch(Dispatchers.IO) {
        _isLoadingNetwork.postValue(true)
        getLocalPictures()
        val response = imageRepository.getImages()
        if (response.isSuccessful) {
            val pictures = response.body() ?: mutableListOf()
            pictures.forEach {
                val indexFavorite = favoritePictures.value?.indexOfFirst { e ->
                    e.imageId == it.imageId
                }

                val indexDownloaded = _localPictures.value?.indexOfFirst { e ->
                    "${it.imageId}.jpg" == e.name
                }

                if (indexFavorite != -1) {
                    it.favorite = true
                }

                if (indexDownloaded != -1) {
                    it.downloaded = true
                }
            }
            _pictures.postValue(pictures)
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    app,
                    app.getString(R.string.error_param, response.code(), response.message()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        _isLoadingNetwork.postValue(false)
    }

    private fun getLocalPictures() = viewModelScope.launch {
        _favoritePictures.postValue(imageRepository.getFavoritePictures())
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

    fun downloadImage() {
        myExecutor.execute {
            val mImage: Bitmap? = imageRepository.downImage(pictureDownload.url)
            myHandler.post {
                if (mImage != null) {
                    imageRepository.saveMediaToStorage(mImage, pictureDownload.imageId)
                    pictures.value?.let {
                        _pictures.postValue(it.map { pic ->
                            if (pic.imageId == pictureDownload.imageId) {
                                pic.copy(downloaded = true)
                            } else {
                                pic
                            }
                        }.toMutableList())
                    }
                    getLocalPictures()
                }
            }
            dialogLoading.dismissDialog()
        }
    }
}
