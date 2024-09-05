package com.example.asian.viewmodel

import RealPathUtil
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import com.example.asian.repository.ImageRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {
    private val imageRepository by lazy {
        ImageRepository(app)
    }

    private val _pictures = MutableLiveData<MutableList<Picture>>()

    val pictures: LiveData<MutableList<Picture>> = _pictures

    fun getAllPicture() {
        val call = imageRepository.getImages()
        call.enqueue(object : Callback<MutableList<Picture>> {
            override fun onResponse(
                call: Call<MutableList<Picture>>, response: Response<MutableList<Picture>>
            ) {
                if (response.isSuccessful) {
                    val pictures = response.body() ?: mutableListOf()
                    _pictures.value = pictures
                } else {
                    Log.e("TAG", response.code().toString())
                }
            }

            override fun onFailure(call: Call<MutableList<Picture>>, t: Throwable) {
                Log.e("TAG", "Failure")
            }
        })
    }

    fun uploadImage(uri: Uri) {
        val realPath = RealPathUtil.getRealPath(app, uri)
        realPath?.let {
            val file = File(it)
            val requestBody = RequestBody.create(
                app.contentResolver.getType(uri)?.let { it1 -> MediaType.parse(it1) }, file
            )
            val imagePart =
                MultipartBody.Part.createFormData(Constants.KEY_IMAGE_DATA, file.name, requestBody)
            val call = imageRepository.uploadImage(imagePart)

            call.enqueue(object : Callback<Picture> {
                override fun onResponse(call: Call<Picture>, response: Response<Picture>) {
                    if (response.isSuccessful) {
                        val picture: Picture? = response.body()
                        if (picture != null) {
                            val values = _pictures.value
                            values?.let { list ->
                                list.add(0, picture)
                                _pictures.value = list
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Picture>, t: Throwable) {
                    Log.e("TAG", t.message.toString())
                }
            })
        }
    }

    fun deleteImage(picture: Picture) {
        val call = imageRepository.deleteImage(picture.imageId)
        call.enqueue(object : Callback<Picture> {
            override fun onResponse(call: Call<Picture>, response: Response<Picture>) {
                if (response.isSuccessful) {
                    val list = _pictures.value
                    list?.let {
                        it.remove(picture)
                        _pictures.value = it
                    }
                }
            }

            override fun onFailure(call: Call<Picture>, t: Throwable) {
                Log.e("TAG", t.message.toString())
            }
        })
    }
}
