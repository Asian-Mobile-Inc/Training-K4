package com.example.asian.issueeleventh.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.asian.issueeleventh.model.UserInfo

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(userInfo: UserInfo)

    @Query("update user set user_name = :name , user_age = :age where user_id = :id")
    suspend fun updateUser(name:String,age:Int,id:Int)

    @Delete
    suspend fun deleteUser(userInfo: UserInfo)

    @Query("select * from user")
    fun getAllUser(): LiveData<MutableList<UserInfo>>

    @Query("delete from user")
    suspend fun deleteAllUser()
}
