package com.example.asian.database.dao

import androidx.room.*
import com.example.asian.model.Picture

@Dao
interface PictureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picture: Picture)

    @Delete
    suspend fun delete(picture: Picture)

    @Query("select * from pictures")
    suspend fun getAllPictureFavorite(): MutableList<Picture>
}