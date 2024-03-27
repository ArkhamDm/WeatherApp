package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName


data class WeatherInfoDTO(
    @SerializedName("dt") val time: Long,
    @SerializedName("main") val mainInfoDTO: MainInfoDTO,
    @SerializedName("weather") val weatherDescriptionDTO: List<WeatherDescriptionDTO>,
    @SerializedName("clouds") val cloudInfoDTO: CloudInfoDTO,
    @SerializedName("wind") val windInfoDTO: WindInfoDTO,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("pop") val probOfPrecipitation: Float,
    @SerializedName("rain") val rainInfoDTO: RainInfoDTO?,
    @SerializedName("snow") val snowInfoDTO: SnowInfoDTO?,
    @SerializedName("dt_txt") val timeTxt: String,
)
