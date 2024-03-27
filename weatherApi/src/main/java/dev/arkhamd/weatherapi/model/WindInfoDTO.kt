package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class WindInfoDTO(
    @SerializedName("speed") val speed: Float,
    @SerializedName("deg") val directionInDegrees: Int,
)
