package dev.arkhamd.lib.model

import com.google.gson.annotations.SerializedName


data class Response(
    @SerializedName("lat") val lat: Float,
    @SerializedName("lon") val lon: Float,
)
