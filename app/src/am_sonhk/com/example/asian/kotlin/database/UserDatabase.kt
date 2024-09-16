package com.example.asian.kotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asian.kotlin.database.dao.UserDao
import com.example.asian.kotlin.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context):UserDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, UserDatabase::class.java, "user_database").build()
            }
            return INSTANCE!!
        }
    }
}

