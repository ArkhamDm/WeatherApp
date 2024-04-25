package dev.arkhamd.data.model

data class DayWeatherInfo(
    val temp: String,
    var iconId: Int,
    var condMain: String,
    val humidity: String,
    val windSpeed: String,
    val time: Long,
    val timeTxt: String,
)
