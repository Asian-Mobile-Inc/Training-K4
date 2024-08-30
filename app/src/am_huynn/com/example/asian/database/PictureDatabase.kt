package com.example.asian.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asian.database.dao.PictureDao
import com.example.asian.model.Picture

@Database(entities = [Picture::class], version = 1)
abstract class PictureDatabase : RoomDatabase() {
    abstract fun getPictureDao(): PictureDao

    companion object {
        @Volatile
        private var instance: PictureDatabase? = null

        fun getInstance(context: Context): PictureDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, PictureDatabase::class.java, "PictureDatabase")
                        .build()
            }
            return instance!!
        }
    }
}
