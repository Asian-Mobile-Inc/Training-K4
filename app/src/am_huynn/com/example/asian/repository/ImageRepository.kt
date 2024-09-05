package com.example.asian.repository

import android.app.Application
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import com.example.asian.services.local.PictureDatabase
import com.example.asian.services.local.dao.PictureDao
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService
import okhttp3.MultipartBody

class ImageRepository(private val app: Application) {
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
    fun loadLocalPictures(): MutableList<Picture> {
        val list: MutableList<Picture> = mutableListOf()

        val uri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            else -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
        )

        app.contentResolver.query(uri, projection, null, null, null).use { cursor ->
            cursor?.let { it ->
                while (it.moveToNext()) {
                    val pictureId =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val pictureUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pictureId
                    )
                    val pic = Picture(pictureId.toString(),pictureUri.toString(),null,null,null,null)
                    list.add(pic)
                }
            }
        }
        return list
    }
}
