package com.example.asian.retrofit.database.repository

import android.app.Application
import com.example.asian.retrofit.database.RetrofitRoomDatabase
import com.example.asian.retrofit.database.dao.RetrofitRoomDao
import com.example.asian.retrofit.model.ImageModel

class RetrofitRoomRepository(application: Application) {
    private val mStorageDao: RetrofitRoomDao

    init {
        val storageDatabase: RetrofitRoomDatabase = RetrofitRoomDatabase.getInstance(application)
        mStorageDao = storageDatabase.getStorageDao()
    }

    suspend fun insertStorage(imageModel: ImageModel) = mStorageDao.insertStorage(imageModel)
    suspend fun deleteStorage(imageModel: ImageModel) =
        mStorageDao.deleteStorage(imageModel.url)

    suspend fun getAllStorage(): MutableList<ImageModel> = mStorageDao.getAllStorage()
}
