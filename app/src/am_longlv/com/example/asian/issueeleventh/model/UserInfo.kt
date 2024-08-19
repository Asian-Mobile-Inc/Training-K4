package com.example.asian.issueeleventh.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserInfo(
    @ColumnInfo("user_name") var userName: String,
    @ColumnInfo("user_age") var userAge: Int,
    @ColumnInfo("user_favourite", defaultValue = "false") var userFavourite: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("user_id")
    var userId: Int = 0
}
