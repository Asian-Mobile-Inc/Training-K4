package com.example.asian.retrofit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("image_api")
data class ImageModel(
    @ColumnInfo("image_id")
    @SerializedName("image_id")
    var imageId: String,
    @ColumnInfo("permalink_url")
    @SerializedName("permalink_url")
    var permalinkUrl: String,
    @ColumnInfo("thumb_url")
    @SerializedName("thumb_url")
    var thumbUrl: String,
    @ColumnInfo("url")
    @SerializedName("url")
    var url: String,
    @ColumnInfo("type")
    @SerializedName("type")
    var type: String,
    @ColumnInfo("created_at")
    @SerializedName("created_at")
    var createdAt: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long = 0

    @Ignore
    var isSelected = false
}
