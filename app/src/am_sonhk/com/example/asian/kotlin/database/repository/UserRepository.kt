package com.example.asian.kotlin.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.asian.kotlin.database.UserDatabase
import com.example.asian.kotlin.database.dao.UserDao
import com.example.asian.kotlin.model.User

class UserRepository(app:Application) {
    private val userDao:UserDao
    init {
        val userDatabase:UserDatabase = UserDatabase.getInstance(app)
        userDao = userDatabase.mGetUserDao()
    }

    suspend fun insertUser(mUser: User) = userDao.mInsertUser(mUser)
    suspend fun updateUser(mUser: User) = userDao.mUpdateUser(mUser)
    suspend fun deleteUser(mUser: User) = userDao.mDeleteUser(mUser)

    fun getAllUser() :LiveData<List<User>> = userDao.mGetAllUser()
 }