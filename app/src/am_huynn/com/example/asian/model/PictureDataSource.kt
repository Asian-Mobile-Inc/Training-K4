package com.example.asian.model

interface PictureDataSource {
    interface InsertDataCallback {
        fun insert(id:Long)
        fun update(id:Long)
    }
}
