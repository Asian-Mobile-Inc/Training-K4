package com.example.asian.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.R
import com.example.asian.constants.Constants
import com.example.asian.database.repository.UserRepository
import com.example.asian.model.User
import com.example.asian.model.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val app: Application) : AndroidViewModel(app) {
    private val userRepository: UserRepository = UserRepository(app)

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoading

    private var _allUsers = MutableLiveData<MutableList<User>>().apply {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            postValue(userRepository.getAllUsers())
            _isLoading.postValue(false)
        }
    }
    val allUsers: LiveData<MutableList<User>> = _allUsers

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val list = _allUsers.value
        list?.let {
            userRepository.insertUser(user, it, object : UserDataSource.InsertDataCallback {
                override fun insertUser(id: Long) {
                    user.userId = id.toInt()
                    it.add(user)
                    _allUsers.postValue(it)
                }

                override fun updateUser(index: Int) {
                    it[index] = user
                    _allUsers.postValue(it)
                }
            })
        }
    }

    val positionPageOne = 0

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateUser(user)
        val list = _allUsers.value
        list?.let {
            val index = it.indexOfFirst { i ->
                i.userId == user.userId
            }
            if (index != Constants.NOT_FOUND_INDEX) {
                it[index] = user
                _allUsers.postValue(it)
            }
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteUser(user)
        val list = _allUsers.value
        list?.let {
            it.remove(user)
            _allUsers.postValue(it)
        }
    }

    fun favoriteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val list = _allUsers.value
        list?.let {
            val index = it.indexOfFirst { i ->
                i.userId == user.userId
            }
            if (index != Constants.NOT_FOUND_INDEX) {
                user.favorite = !(user.favorite)
                it[index] = user
                _allUsers.postValue(it)
                userRepository.updateUser(user)
            }
        }
    }

    fun deleteAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteAllUsers()
        _allUsers.postValue(mutableListOf())
    }

    fun validatorName(name: String): String? {
        return if (name.isEmpty()) {
            app.resources.getString(R.string.please_not_empty)
        } else {
            null
        }
    }

    fun validatorAge(age: String): String? {
        return if (age.isEmpty()) {
            app.resources.getString(R.string.please_not_empty)
        } else if (age.toInt() < 1) {
            app.resources.getString(R.string.age_greater_than_zero)
        } else if (age.length > 3) {
            app.resources.getString(R.string.age_is_less_than_three_digits)
        } else {
            null
        }
    }
}
