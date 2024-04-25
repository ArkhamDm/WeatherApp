package dev.arkhamd.data.model

data class HourWeatherInfo(
    val time: Long,
    val temp: String,
    val feelsLike: String,
    val humidity: String,
    val windSpeed: String,
    var weatherCondition: String,
    var weatherConditionIconId: Int,
    val timeTxt: String,
)
