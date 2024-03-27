package dev.arkhamd.data.model

data class WeatherInfo(
    val time: Long,
    val mainInfo: MainInfo,
    val weatherDescription: WeatherDescription,
    val cloudInfo: CloudInfo,
    val windInfo: WindInfo,
    val visibility: Int,
    val probOfPrecipitation: Float,
    val rainInfo: RainInfo? = null,
    val snowInfo: SnowInfo? = null,
    val timeTxt: String,
)
enum class PartOfDay {
    DAY,
    NIGHT
}