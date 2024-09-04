package com.example.asian.kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asian.kotlin.database.repository.UserRepository
import com.example.asian.kotlin.model.User
import kotlinx.coroutines.launch

class UserViewModel(app:Application) : ViewModel() {
    private val mUserRepository:UserRepository = UserRepository(app)

    fun insertUser(mUser: User) = viewModelScope.launch {
        mUserRepository.insertUser(mUser)
    }

    fun updateUser(mUser: User) = viewModelScope.launch {
        mUserRepository.updateUser(mUser)
    }

    fun deleteUser(mUser: User) = viewModelScope.launch {
        mUserRepository.deleteUser(mUser)
    }

    fun getAllUser():LiveData<List<User>> = mUserRepository.getAllUser()

    class UserViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(app) as T
            }

            throw IllegalArgumentException("Unnable contrust viewModel")
        }
    }
}