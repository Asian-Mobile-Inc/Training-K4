package com.example.asian.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asian.database.repository.UserRepository
import com.example.asian.model.User
import kotlinx.coroutines.launch

class UserViewModel(app:Application) : ViewModel() {
    private val userRepository:UserRepository = UserRepository(app)

    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        userRepository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        userRepository.deleteUser(user)
    }

    fun getAllUsers():LiveData<List<User>> = userRepository.getAppUsers()

    class UserViewModelFactory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)){
                return UserViewModel(app) as T
            }
            throw IllegalAccessException("Unable constructor")
        }
    }
}