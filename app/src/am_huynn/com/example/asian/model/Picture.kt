package com.example.asian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pictures")
data class Picture(
    @PrimaryKey @ColumnInfo(name = "column_id") @SerializedName("image_id") var imageId: String,
    @ColumnInfo(name = "column_permalink_url") @SerializedName("permalink_url") var permalinkUrl: String?,
    @ColumnInfo(name = "column_url") @SerializedName("url") var url: String,
    @ColumnInfo(name = "column_type") @SerializedName("type") var type: String?,
    @ColumnInfo(name = "column_thumb_url") @SerializedName("thumb_url") var thumb_url: String?,
    @ColumnInfo(name = "column_created_at") @SerializedName("created_at") var created_at: String?,
    @ColumnInfo(name = "column_favorite") var favorite: Boolean = false
)
