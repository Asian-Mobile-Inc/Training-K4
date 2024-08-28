package com.example.asian.retrofit.model

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("image_id")
    var imageId: String,
    @SerializedName("permalink_url")
    var permalinkUrl: String,
    @SerializedName("thumb_url")
    var thumbUrl: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("created_at")
    var createdAt: String,
) {

}
