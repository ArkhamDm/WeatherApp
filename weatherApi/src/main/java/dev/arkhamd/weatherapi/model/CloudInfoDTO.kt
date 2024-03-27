package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class CloudInfoDTO(
    @SerializedName("all") val cloudiness: Int
)
