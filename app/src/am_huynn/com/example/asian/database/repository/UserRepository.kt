package com.example.asian.database.repository

import android.app.Application
import com.example.asian.constants.Constants
import com.example.asian.database.UserDatabase
import com.example.asian.database.dao.UserDao
import com.example.asian.model.User
import com.example.asian.model.UserDataSource

class UserRepository(app: Application) {
    private val userDao: UserDao

    init {
        val userDatabase: UserDatabase = UserDatabase.getInstance(app)
        userDao = userDatabase.getUserDao()
    }

    suspend fun insertUser(
        user: User, list: MutableList<User>, callback: UserDataSource.InsertDataCallback
    ) {
        val index: Int = list.indexOfFirst {
            it.userId == user.userId
        }
        val id: Long = userDao.insertUser(user)
        if (index == Constants.NOT_FOUND_INDEX) {
            callback.insertUser(id)
        } else {
            callback.updateUser(index)
        }
    }

    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun deleteAllUsers() = userDao.deleteAllUsers()
    suspend fun getAllUsers(): MutableList<User> = userDao.getAllUsers()
}
