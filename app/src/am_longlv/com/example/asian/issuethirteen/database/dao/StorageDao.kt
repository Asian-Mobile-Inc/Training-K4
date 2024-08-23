package com.example.asian.issuethirteen.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.asian.issuethirteen.model.StorageModel

@Dao
interface StorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorage(storageModel: StorageModel): Long

    @Update
    suspend fun updateStorage(storageModel: StorageModel)

    @Query("DELETE FROM storage WHERE storage_uri = :uri")
    suspend fun deleteStorage(uri: String)

    @Query("SELECT * FROM storage")
    suspend fun getAllStorage(): MutableList<StorageModel>

    @Query("UPDATE storage SET storage_uri = :newUri, storage_name = :newName where storage_uri =:uri")
    suspend fun updateNameStorage(uri: String, newUri: String, newName: String)
}
