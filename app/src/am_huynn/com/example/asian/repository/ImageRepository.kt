package com.example.asian.repository

import android.app.Application
import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import com.example.asian.services.local.PictureDatabase
import com.example.asian.services.local.dao.PictureDao
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService
import okhttp3.MultipartBody

class ImageRepository(app: Application) {
    private val pictureDao: PictureDao

    init {
        val pictureDatabase: PictureDatabase = PictureDatabase.getInstance(app)
        pictureDao = pictureDatabase.getPictureDao()
    }

    private val apiService = ApiClient.retrofit(Constants.BASE_URL).create(ApiService::class.java)
    private val apiServiceUpload =
        ApiClient.retrofit(Constants.BASE_URL_UPLOAD).create(ApiService::class.java)

    suspend fun getImages() = apiService.getImages()

    suspend fun uploadImage(image: MultipartBody.Part) = apiServiceUpload.uploadImage(image)

    suspend fun deleteImage(imageId: String) = apiService.deleteImage(imageId)

    suspend fun getFavoritePictures() = pictureDao.getAllPictureFavorite()
    suspend fun insertFavoritePicture(picture: Picture) = pictureDao.insert(picture)
    suspend fun deleteFavoritePicture(picture: Picture) = pictureDao.delete(picture)
}
