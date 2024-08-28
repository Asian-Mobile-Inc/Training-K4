package com.example.asian.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "pictures")
data class Picture(
    @ColumnInfo(name = "column_id") val id: Long,
    @ColumnInfo(name = "column_name") val name: String,
    @ColumnInfo(name = "column_uri") val uri: Uri,
    @ColumnInfo(name = "column_favorite") val favorite: Boolean = false
)
