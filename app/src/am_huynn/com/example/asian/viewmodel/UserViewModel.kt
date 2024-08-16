package com.example.asian.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asian.R
import com.example.asian.constants.Constants
import com.example.asian.database.repository.UserRepository
import com.example.asian.model.User
import com.example.asian.model.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val app: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(app)
    private var isLoading = MutableLiveData<Boolean>()
    private var allUsers = MutableLiveData<MutableList<User>>().apply {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            postValue(userRepository.getAllUsers())
            setIsLoading(false)
        }
    }

    fun getAllUserObserver(): MutableLiveData<MutableList<User>> {
        return allUsers
    }

    fun getIsLoadingObserver(): MutableLiveData<Boolean> {
        return isLoading
    }

    private fun setIsLoading(isLoading: Boolean) {
        this.isLoading.postValue(isLoading)
    }

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val list = allUsers.value
        list?.let {
            userRepository.insertUser(user, it, object : UserDataSource.InsertDataCallback {
                override fun insertUser(id: Long) {
                    user.userId = id.toInt()
                    it.add(user)
                    allUsers.postValue(it)
                }

                override fun updateUser(index: Int) {
                    it[index] = user
                    allUsers.postValue(it)
                }
            })
        }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        userRepository.updateUser(user)
        val list = allUsers.value
        list?.let {
            val index = it.indexOfFirst { i ->
                i.userId == user.userId
            }
            if (index != Constants.NOT_FOUND_INDEX) {
                it[index] = user
                allUsers.postValue(it)
            }
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteUser(user)
        val list = allUsers.value
        list?.let {
            it.remove(user)
            allUsers.postValue(it)
        }
    }

    fun deleteAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteAllUsers()
        allUsers.postValue(mutableListOf())
    }

    fun validatorName(name: String): String? {
        return if (name.isEmpty()) {
            app.resources.getString(R.string.please_not_empty)
        } else {
            null
        }
    }

    fun validatorAge(email: String): String? {
        return if (email.isEmpty()) {
            app.resources.getString(R.string.please_not_empty)
        } else if (email.toInt() < 1) {
            app.resources.getString(R.string.age_greater_than_zero)
        } else {
            null
        }
    }

    class UserViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(app) as T
            }
            throw IllegalAccessException("Unable constructor")
        }
    }
}
