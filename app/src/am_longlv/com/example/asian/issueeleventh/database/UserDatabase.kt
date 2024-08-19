package com.example.asian.issueeleventh.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.asian.issueeleventh.database.dao.UserDao
import com.example.asian.issueeleventh.model.UserInfo

@Database(entities = [UserInfo::class], version = 2)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var mInstance: UserDatabase? = null
        private val mMigrationOneToTwo = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE user " +
                            "add column user_favourite INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getInstance(application: Application): UserDatabase {
            if (mInstance == null) {
                mInstance =
                    Room.databaseBuilder(application, UserDatabase::class.java, "UserDatabase")
                        .addMigrations(mMigrationOneToTwo)
                        .build()
            }
            return mInstance!!
        }
    }
}
