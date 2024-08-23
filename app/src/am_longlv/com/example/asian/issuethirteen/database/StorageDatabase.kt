package com.example.asian.issuethirteen.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asian.issuethirteen.database.dao.StorageDao
import com.example.asian.issuethirteen.model.StorageModel

private const val NAME_DATABASE = "StorageDatabase"

@Database(entities = [StorageModel::class], version = 1)
abstract class StorageDatabase : RoomDatabase() {
    abstract fun getStorageDao(): StorageDao

    companion object {
        @Volatile
        private var mInstance: StorageDatabase? = null

        fun getInstance(application: Application): StorageDatabase {
            if (mInstance == null) {
                mInstance =
                    Room.databaseBuilder(
                        application,
                        StorageDatabase::class.java,
                        NAME_DATABASE
                    )
                        .build()
            }
            return mInstance!!
        }
    }
}
