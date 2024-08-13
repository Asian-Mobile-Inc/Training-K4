package com.example.asian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "user_name")
    var userName: String = "",
    @ColumnInfo(name = "age")
    var age: Int = 0,
) {
    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0
}
