package com.example.asian.viewmodel

import android.app.Application
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import com.example.asian.model.Picture

class StorageViewModel(private val app: Application) : AndroidViewModel(app) {
    public fun loadAllImage(): List<Picture> {
        var listPicture: MutableList<Picture> = mutableListOf()

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
            cursor?.let {
                while (it.moveToNext()) {
                    val pictureId =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val pictureName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pictureId
                    )
                    val pic = Picture(pictureId, pictureName, uri)
                    listPicture.add(pic)
                }
            }
        }
        return listPicture
    }
}