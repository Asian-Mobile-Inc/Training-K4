package com.example.asian.retrofit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.asian.retrofit.model.ImageModel

@Dao
interface RetrofitRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorage(imageModel: ImageModel): Long

    @Update
    suspend fun updateStorage(imageModel: ImageModel)

    @Query("DELETE FROM image_api WHERE url = :url")
    suspend fun deleteStorage(url: String)

    @Query("SELECT * FROM image_api")
    suspend fun getAllStorage(): MutableList<ImageModel>
}
