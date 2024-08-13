package com.example.asian.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.asian.database.UserDatabase
import com.example.asian.database.dao.UserDao
import com.example.asian.model.User

class UserRepository(app:Application) {
    private val userDao: UserDao

    init {
        val userDatabase: UserDatabase = UserDatabase.getInstance(app);
        userDao = userDatabase.getUserDao()
    }

    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    fun getAppUsers(): LiveData<List<User>> = userDao.getAllUsers();
}