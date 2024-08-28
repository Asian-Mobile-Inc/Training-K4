package com.example.asian.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.asian.model.Picture

interface PictureDao {
    @Insert
    suspend fun insert(picture: Picture)

    @Delete
    suspend fun delete(picture: Picture)

    @Query("select * from pictures")
    fun getAllPictureFavorite(): LiveData<MutableList<Picture>>
}