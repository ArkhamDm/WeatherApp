package dev.arkhamd.data.model

data class WeatherInfo(
    val cityInfo: CityInfo,
    val hourWeatherInfo: List<HourWeatherInfo>,
    val dayWeatherInfo: List<DayWeatherInfo>
)