package com.example.asian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class Picture(
    @PrimaryKey @ColumnInfo(name = "column_id") var id: Long,
    @ColumnInfo(name = "column_name") var name: String,
    @ColumnInfo(name = "column_uri") var uri: String,
    @ColumnInfo(name = "column_favorite") var favorite: Boolean = false
)
