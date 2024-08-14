package com.example.asian.issueeleventh.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asian.issueeleventh.database.UserDatabase
import com.example.asian.issueeleventh.database.dao.UserDao
import com.example.asian.issueeleventh.model.UserInfo

class UserRepository(application: Application) {
    private val mUserDao: UserDao

    init {
        val userDatabase: UserDatabase = UserDatabase.getInstance(application)
        mUserDao = userDatabase.getUserDao()
    }

    suspend fun insertUser(userInfo: UserInfo) = mUserDao.insertUser(userInfo)
    suspend fun updateUser(userInfo: UserInfo) = mUserDao.updateUser(userInfo.userName,userInfo.userAge,userInfo.userId)
    suspend fun deleteUser(userInfo: UserInfo) = mUserDao.deleteUser(userInfo)
    suspend fun deleteAllUser() = mUserDao.deleteAllUser()
    fun getAllUser(): LiveData<MutableList<UserInfo>> = mUserDao.getAllUser()
}
