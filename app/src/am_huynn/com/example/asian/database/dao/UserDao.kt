package com.example.asian.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asian.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from user")
    fun getAllUsers(): LiveData<List<User>>
}