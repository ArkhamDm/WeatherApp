package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class WeatherDescriptionDTO(
    @SerializedName("cond") val condId: Int,
    @SerializedName("main") val condMain: String,
    @SerializedName("description") val description: String,
)
