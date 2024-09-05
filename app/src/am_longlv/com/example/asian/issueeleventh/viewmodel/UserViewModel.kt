package com.example.asian.issueeleventh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.issueeleventh.database.repository.UserRepository
import com.example.asian.issueeleventh.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val mUserRepository: UserRepository = UserRepository(application)

    private var _allUsers = MutableLiveData<MutableList<UserInfo>>()
    internal val allUsers: LiveData<MutableList<UserInfo>> = _allUsers

    internal fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            _allUsers.postValue(mUserRepository.getAllUser())
        }
    }

    fun insertUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUserInfo = mUserRepository.insertUser(userInfo)
            _allUsers.value?.let {
                userInfo.userId = newUserInfo.toString().toInt()
                it.add(userInfo)
                _allUsers.postValue(it)
            }
        }
    }

    fun updateUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.updateUser(userInfo)
            _allUsers.value?.let {
                val index = it.indexOfFirst { itChild ->
                    itChild.userId == userInfo.userId
                }
                if (index != -1) {
                    it[index] = userInfo
                }
                _allUsers.postValue(it)
            }
        }
    }

    fun getListSize(): Int {
        return _allUsers.value?.size ?: 0
    }

    fun deleteUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.deleteUser(userInfo)
            _allUsers.value?.let {
                it.remove(userInfo)
                _allUsers.postValue(it)
            }
        }
    }

    fun deleteAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.deleteAllUser()
            _allUsers.value?.let {
                it.clear()
                _allUsers.postValue(it)
            }
        }
    }
}
