package com.example.asian.database.dao

import androidx.room.*
import com.example.asian.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) : Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from user")
    suspend fun getAllUsers(): MutableList<User>

    @Query("delete from user")
    suspend fun deleteAllUsers()
}
