package com.example.asian.retrofit.database.repository

import android.app.Application
import com.example.asian.retrofit.database.RetrofitRoomDatabase
import com.example.asian.retrofit.database.dao.RetrofitRoomDao
import com.example.asian.retrofit.model.ImageModel

class RetrofitRoomRepository(application: Application) {
    private val storageDao: RetrofitRoomDao

    init {
        val storageDatabase: RetrofitRoomDatabase = RetrofitRoomDatabase.getInstance(application)
        storageDao = storageDatabase.getStorageDao()
    }

    suspend fun insertStorage(imageModel: ImageModel) = storageDao.insertStorage(imageModel)
    suspend fun getAllStorage(): MutableList<ImageModel> = storageDao.getAllStorage()
    suspend fun deleteStorage(imageModel: ImageModel) =
        storageDao.deleteStorage(imageModel.imageId)
}
