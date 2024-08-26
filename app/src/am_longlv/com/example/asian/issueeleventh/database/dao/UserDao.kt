package com.example.asian.issueeleventh.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.asian.issueeleventh.model.UserInfo

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userInfo: UserInfo): Long

    @Update
    suspend fun updateUser(userInfo: UserInfo)

    @Delete
    suspend fun deleteUser(userInfo: UserInfo)

    @Query("select * from user order by user_id")
    suspend fun getAllUser(): MutableList<UserInfo>

    @Query("select * from user where user_favourite = 1 order by user_id")
    suspend fun getFavouriteUsers(): MutableList<UserInfo>

    @Query("delete from user")
    suspend fun deleteAllUser()
}
