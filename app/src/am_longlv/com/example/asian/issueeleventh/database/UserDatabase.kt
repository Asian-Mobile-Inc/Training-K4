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
                db.execSQL("ALTER TABLE user RENAME TO user_old;")
                db.execSQL(
                    "CREATE TABLE user (" +
                            "user_id INTEGER PRIMARY KEY NOT NULL," +
                            "user_name TEXT NOT NULL, " +
                            "user_age INTEGER NOT NULL," +
                            "user_favourite INTEGER NOT NULL DEFAULT false)"
                )
                db.execSQL(
                    "INSERT INTO user( user_name, user_age) " +
                            "SELECT user_name, user_age FROM user_old;"
                )
                db.execSQL("DROP TABLE user_old")
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
