package com.example.asian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pictures")
data class Picture(
    @PrimaryKey @ColumnInfo(name = "column_id") @SerializedName("image_id") val imageId: String,
    @ColumnInfo(name = "column_permalink_url") @SerializedName("permalink_url") val permalinkUrl: String,
    @ColumnInfo(name = "column_url") @SerializedName("url") val url: String,
    @ColumnInfo(name = "column_type") @SerializedName("type") val type: String,
    @ColumnInfo(name = "column_thumb_url") @SerializedName("thumb_url") val thumb_url: String,
    @ColumnInfo(name = "column_created_at") @SerializedName("created_at") val created_at: String
)
