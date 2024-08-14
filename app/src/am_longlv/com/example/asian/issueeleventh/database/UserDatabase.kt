package com.example.asian.issueeleventh.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.asian.issueeleventh.database.dao.UserDao
import com.example.asian.issueeleventh.model.UserInfo

@Database(entities = [UserInfo::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    val migration1to2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE user \n" +
                        "       ADD COLUMN user_age_new int\n" +
                        "UPDATE user \n" +
                        "       SET user_age_new = CAST(user_age as user_age_new)"
            )
        }
    }

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        fun getInstance(application: Application): UserDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(application, UserDatabase::class.java, "UserDatabase")
                        .build()
            }
            return instance!!
        }
    }
}
