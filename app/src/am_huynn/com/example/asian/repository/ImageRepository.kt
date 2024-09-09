package com.example.asian.repository

import android.app.Application
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import com.example.asian.constants.Constants
import com.example.asian.model.Picture
import com.example.asian.services.local.PictureDatabase
import com.example.asian.services.local.dao.PictureDao
import com.example.asian.services.remote.ApiClient
import com.example.asian.services.remote.ApiService
import okhttp3.MultipartBody
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageRepository(private val app: Application) {
    private val pictureDao: PictureDao

    init {
        val pictureDatabase: PictureDatabase = PictureDatabase.getInstance(app)
        pictureDao = pictureDatabase.getPictureDao()
    }

    private val apiService = ApiClient.retrofit(Constants.BASE_URL).create(ApiService::class.java)
    private val apiServiceUpload =
        ApiClient.retrofit(Constants.BASE_URL_UPLOAD).create(ApiService::class.java)

    suspend fun getImages(page: Int, per_page: Int) = apiService.getImages(page, per_page)

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
            MediaStore.Images.Media.DISPLAY_NAME,
        )

        app.contentResolver.query(uri, projection, null, null, null).use { cursor ->
            cursor?.let { it ->
                it.moveToLast()
                do {
                    val pictureId =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val pictureName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val pictureUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pictureId
                    )
                    val pic = Picture(
                        pictureId.toString(),
                        pictureUri.toString(),
                        null,
                        null,
                        null,
                        null,
                        pictureName
                    )
                    list.add(pic)
                } while (it.moveToPrevious())
            }
        }
        return list
    }

    fun downImage(string: String): Bitmap? {
        val url: URL = stringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun stringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    fun saveMediaToStorage(bitmap: Bitmap?, id: String) {
        val filename = "${id}.jpg"
        var fos: OutputStream? = null
        app.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            }
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            imageUri?.let {
                fos = resolver.openOutputStream(it)
            }
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(app, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }

    }
}
