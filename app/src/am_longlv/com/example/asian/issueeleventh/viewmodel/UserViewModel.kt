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
    private var mAllUsers = MutableLiveData<MutableList<UserInfo>>()
    internal val allUsers: LiveData<MutableList<UserInfo>> = mAllUsers
    private var mFavouriteUsers = MutableLiveData<MutableList<UserInfo>>()
    internal val favouriteUsers: LiveData<MutableList<UserInfo>> = mFavouriteUsers

    internal fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            mAllUsers.postValue(mUserRepository.getAllUser())
        }
    }

    internal fun getFavouriteUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            mFavouriteUsers.postValue(mUserRepository.getFavouriteUsers())
        }
    }

    fun insertUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUserInfo = mUserRepository.insertUser(userInfo)
            mAllUsers.value?.let {
                userInfo.userId = newUserInfo.toString().toInt()
                it.add(userInfo)
                mAllUsers.postValue(it)
            }
        }
    }

    fun updateUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.updateUser(userInfo)
            mAllUsers.value?.let {
                val index = it.indexOfFirst { itChild ->
                    itChild.userId == userInfo.userId
                }
                if (index != -1) {
                    it[index] = userInfo
                }
                mAllUsers.postValue(it)
            }
            mFavouriteUsers.value?.let {
                val index = it.indexOfFirst { itChild ->
                    itChild.userId == userInfo.userId
                }
                if (index != -1) {
                    it[index] = userInfo
                }
                mFavouriteUsers.postValue(it)
            }
        }
    }

    fun favouriteUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.updateUser(userInfo)
            mAllUsers.value?.let {
                val index = it.indexOfFirst { itChild ->
                    itChild.userId == userInfo.userId
                }
                if (index != -1) {
                    it[index] = userInfo
                }
                mAllUsers.postValue(it)
            }
            mFavouriteUsers.value?.let {
                val index = it.indexOfFirst { itChild ->
                    itChild.userId == userInfo.userId
                }
                if (index != -1) {
                    it[index] = userInfo
                    if (!userInfo.userFavourite) {
                        it.remove(userInfo)
                    }
                } else if (userInfo.userFavourite) {
                    it.add(userInfo)
                }
                it.sortBy { sit ->
                    sit.userId
                }
                mFavouriteUsers.postValue(it)
            }
        }
    }

    fun deleteUser(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.deleteUser(userInfo)
            mAllUsers.value?.let {
                it.remove(userInfo)
                mAllUsers.postValue(it)
            }
            mFavouriteUsers.value?.let {
                it.remove(userInfo)
                mFavouriteUsers.postValue(it)
            }
        }
    }

    fun deleteAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            mUserRepository.deleteAllUser()
            mAllUsers.value?.let {
                it.clear()
                mAllUsers.postValue(it)
            }
            mFavouriteUsers.value?.let {
                it.clear()
                mFavouriteUsers.postValue(it)
            }
        }
    }
}
