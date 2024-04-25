package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class CityInfoDTO(
    @SerializedName("name") val name: String,
    @SerializedName("sunrise") val sunriseTime: Long,
    @SerializedName("sunset") val sunsetTime: Long
)
