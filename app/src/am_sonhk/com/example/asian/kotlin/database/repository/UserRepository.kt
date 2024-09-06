package com.example.asian.kotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.asian.kotlin.database.dao.UserDao
import com.example.asian.kotlin.model.User

class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun deleteUser(userId: Int) {
        userDao.deleteUser(userId)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}


