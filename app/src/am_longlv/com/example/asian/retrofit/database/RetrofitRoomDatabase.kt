package com.example.asian.retrofit.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asian.retrofit.database.dao.RetrofitRoomDao
import com.example.asian.retrofit.model.ImageModel

private const val NAME_DATABASE = "RetrofitDatabase"

@Database(entities = [ImageModel::class], version = 1)
abstract class RetrofitRoomDatabase : RoomDatabase() {
    abstract fun getStorageDao(): RetrofitRoomDao

    companion object {
        @Volatile
        private var mInstance: RetrofitRoomDatabase? = null

        fun getInstance(application: Application): RetrofitRoomDatabase {
            if (mInstance == null) {
                mInstance =
                    Room.databaseBuilder(
                        application,
                        RetrofitRoomDatabase::class.java,
                        NAME_DATABASE
                    )
                        .build()
            }
            return mInstance!!
        }
    }
}
