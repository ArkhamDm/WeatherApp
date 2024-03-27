package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class SnowInfoDTO(
    @SerializedName("3h") val threeHours: Float
)
