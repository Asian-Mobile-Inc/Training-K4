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

    @ColumnInfo("storage_id", defaultValue = "")
    var storageId: String = ""

    @Ignore
    var isFavourite = false

    @Ignore
    var isDownloaded = false
}
