package com.example.asian.kotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    var userName: String,
    var userAge: Int
)
