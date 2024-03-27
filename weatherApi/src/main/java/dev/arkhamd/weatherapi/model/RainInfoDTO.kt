package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class RainInfoDTO(
     @SerializedName("3h") val threeHours: Float
)