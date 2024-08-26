package com.example.asian.issueeleventh.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asian.issueeleventh.database.dao.UserDao
import com.example.asian.issueeleventh.model.UserInfo

private const val NAME_DATABASE = "UserDatabase"

@Database(entities = [UserInfo::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var mInstance: UserDatabase? = null
        fun getInstance(application: Application): UserDatabase {
            if (mInstance == null) {
                mInstance =
                    Room.databaseBuilder(application, UserDatabase::class.java, NAME_DATABASE)
                        .build()
            }
            return mInstance!!
        }
    }
}
