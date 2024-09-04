package com.example.asian.kotlin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.asian.kotlin.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun mInsertUser(mUser: User)

    @Update
    suspend fun mUpdateUser(mUser: User)

    @Delete
    suspend fun mDeleteUser(mUser: User)

    @Query("SELECT * FROM user_db")
    fun mGetAllUser(): LiveData<List<User>>

    //TODO DeleteUserById - DeleteAllUser
}