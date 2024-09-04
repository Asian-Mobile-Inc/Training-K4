package com.example.asian.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("image_id") val imageId: String,
    @SerializedName("permalink_url") val permalinkUrl: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("thumb_url") val thumb_url: String,
    @SerializedName("created_at") val created_at: String
)
