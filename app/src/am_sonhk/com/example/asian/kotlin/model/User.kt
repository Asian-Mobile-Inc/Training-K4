package com.example.asian.kotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//open class User ( val mUserId: Int, val mUserName: String, val mAge : Int)

@Entity(tableName ="user_db")
class User(
    @ColumnInfo(name = "name_col") var mUserName:String = "",
    @ColumnInfo(name = "age_col") var mAge: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_col")
    var mUserId:Int=0
}

