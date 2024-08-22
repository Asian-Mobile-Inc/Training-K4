package com.example.asian.issuethirteen.database.repository

import android.app.Application
import com.example.asian.issuethirteen.database.StorageDatabase
import com.example.asian.issuethirteen.database.dao.StorageDao
import com.example.asian.issuethirteen.model.StorageModel

class StorageRepository(application: Application) {
    private val mStorageDao: StorageDao

    init {
        val storageDatabase: StorageDatabase = StorageDatabase.getInstance(application)
        mStorageDao = storageDatabase.getStorageDao()
    }

    suspend fun insertStorage(storageModel: StorageModel) = mStorageDao.insertStorage(storageModel)
    suspend fun updateStorage(storageModel: StorageModel) = mStorageDao.updateStorage(storageModel)
    suspend fun deleteStorage(storageModel: StorageModel) =
        mStorageDao.deleteStorage(storageModel.storageUri)

    suspend fun getAllStorage(): MutableList<StorageModel> = mStorageDao.getAllStorage()
    suspend fun updateNameStorage(storageModel: StorageModel, newStorageModel: StorageModel) =
        mStorageDao.updateNameStorage(
            storageModel.storageUri,
            newStorageModel.storageUri,
            newStorageModel.storageName
        )
}