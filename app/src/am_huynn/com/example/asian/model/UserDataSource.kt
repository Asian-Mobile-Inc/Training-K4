package com.example.asian.model

interface UserDataSource {
    interface InsertDataCallback {
        fun insertUser(id:Long)
        fun updateUser(index:Int)
    }
}