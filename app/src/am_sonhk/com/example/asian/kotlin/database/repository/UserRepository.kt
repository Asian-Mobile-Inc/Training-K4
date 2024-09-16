package com.example.asian.kotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asian.kotlin.database.UserDatabase
import com.example.asian.kotlin.database.dao.UserDao
import com.example.asian.kotlin.model.User

class UserRepository(application: Application) {

    private val UserDao: UserDao

    init {
        val UserDatabase: UserDatabase = UserDatabase.getInstance(application)
        UserDao = UserDatabase.getUserDao()
    }

    suspend fun addUser(user: User) {
        UserDao.addUser(user)
    }

    suspend fun deleteUser(userId: Int) {
        UserDao.deleteUser(userId)
    }

    suspend fun deleteAllUsers() {
        UserDao.deleteAllUsers()
    }

    fun updateUser(user: User) {
        UserDao.updateUser(user)
    }

    fun getAllUsers(): MutableList<User> {
        return UserDao.getAllUsers()
    }
}


