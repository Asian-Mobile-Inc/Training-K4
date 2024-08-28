package com.example.asian.repository

import android.app.Application
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import com.example.asian.database.PictureDatabase
import com.example.asian.database.dao.PictureDao
import com.example.asian.model.Picture


class ImageRepository(private val app: Application) {
    private val pictureDao: PictureDao

    init {
        val pictureDatabase: PictureDatabase = PictureDatabase.getInstance(app)
        pictureDao = pictureDatabase.getPictureDao()
    }

    suspend fun insertRoomPicture(picture: Picture) = pictureDao.insert(picture)
    suspend fun deleteRoomPicture(picture: Picture) = pictureDao.delete(picture)
    suspend fun getAllFavoritePicture(): MutableList<Picture> = pictureDao.getAllPictureFavorite()

    fun loadAllImage(): MutableList<Picture> {
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
            MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME
        )

        app.contentResolver.query(uri, projection, null, null, null).use { cursor ->
            cursor?.let { it ->
                while (it.moveToNext()) {
                    val pictureId =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val pictureName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pictureId
                    )
                    val pic = Picture(pictureId, pictureName, uri)
                    list.add(pic)
                }
            }
        }
        return list
    }
}