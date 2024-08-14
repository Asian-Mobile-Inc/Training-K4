package com.example.asian.issueeleventh.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asian.issueeleventh.database.repository.UserRepository
import com.example.asian.issueeleventh.model.UserInfo
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun insertUser(userInfo: UserInfo) = viewModelScope.launch {
        mUserRepository.insertUser(userInfo)
    }

    fun updateUser(userInfo: UserInfo) = viewModelScope.launch {
        mUserRepository.updateUser(userInfo)
    }

    fun deleteUser(userInfo: UserInfo) = viewModelScope.launch {
        mUserRepository.deleteUser(userInfo)
    }

    fun deleteAllUser() = viewModelScope.launch {
        mUserRepository.deleteAllUser()
    }

//    private var _allUsers: MutableLiveData<MutableList<UserInfo>> =
//        mUserRepository.getAllUser() as MutableLiveData<MutableList<UserInfo>>

    var allUsers: LiveData<MutableList<UserInfo>> =
        mUserRepository.getAllUser()
//    fun getAllData(){
//        viewModelScope.launch{
//            _allUsers.value = mUserRepository.getAllUser()
//        }
//    }

    class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewmodel")
        }
    }
}
