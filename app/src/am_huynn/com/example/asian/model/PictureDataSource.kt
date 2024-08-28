package com.example.asian.model

interface PictureDataSource {
    interface InsertDataCallback {
        fun insert()
        fun update()
    }
}
