package com.example.asian.kotlin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.asian.kotlin.model.User
import com.example.asian.kotlin.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    private val _allUsers = MutableLiveData<MutableList<User>>()
    val allUsers: MutableLiveData<MutableList<User>> = _allUsers

    init {
        repository = UserRepository(application)
    }

    fun addUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.addUser(user)
        getAllUsers()
    }

    fun deleteUser(userId: Int) = viewModelScope.launch {
        repository.deleteUser(userId)
        getAllUsers()
    }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {

        val updatedUsers = allUsers.value?.map {
            if (user.userId == it.userId) {
                user.copy()
            } else {
                it.copy()
            }
        }?.toMutableList() ?: mutableListOf()
        // add data to live data
        repository.updateUser(user)
        // add data change to UI
        _allUsers.postValue(updatedUsers)
        getAllUsers()
    }

    fun getAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        _allUsers.postValue(repository.getAllUsers())
    }


    fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUsers()
        getAllUsers()
    }
}




