package dev.arkhamd.data

import dev.arkhamd.data.model.CityInfo
import dev.arkhamd.data.model.DayWeatherInfo
import dev.arkhamd.data.model.HourWeatherInfo
import dev.arkhamd.weaherdatabase.model.CityInfoDBO
import dev.arkhamd.weaherdatabase.model.DayWeatherInfoDBO
import dev.arkhamd.weaherdatabase.model.HourWeatherInfoDBO
import dev.arkhamd.weatherapi.model.CityInfoDTO
import dev.arkhamd.weatherapi.model.WeatherInfoDTO

fun CityInfoDBO.toCityInfo(): CityInfo {
    return CityInfo(
        name, sunriseTime, sunsetTime
    )
}

fun HourWeatherInfoDBO.toHourWeatherInfo() : HourWeatherInfo {
    return HourWeatherInfo(
        time, temp, feelsLike, humidity, windSpeed, weatherCondition, weatherConditionIconId, timeTxt
    )
}

fun DayWeatherInfoDBO.toDayWeatherInfo(): DayWeatherInfo {
    return DayWeatherInfo(
        temp, iconId, condMain, humidity, windSpeed, time, timeTxt
    )
}

fun CityInfoDTO.toCityInfo(): CityInfo {
    return CityInfo(
        name, sunriseTime, sunsetTime
    )
}

fun WeatherInfoDTO.toHourWeatherInfo() : HourWeatherInfo {
    return HourWeatherInfo(
        time = time,
        temp = if (mainInfoDTO.temp.toInt() >= 0)
                "+" + mainInfoDTO.temp.toInt().toString()
            else
                mainInfoDTO.temp.toInt().toString(),
        humidity = mainInfoDTO.humidity.toString(),
        weatherCondition = weatherDescriptionDTO[0].condMain,
        weatherConditionIconId = 0,
        feelsLike = if (mainInfoDTO.feelsLike.toInt() >= 0)
                "+" + mainInfoDTO.feelsLike.toInt().toString()
            else
                mainInfoDTO.feelsLike.toInt().toString(),
        windSpeed = windInfoDTO.speed.toInt().toString(),
        timeTxt = timeTxt
    )
}

fun List<WeatherInfoDTO>.toDayWeatherInfo(): List<DayWeatherInfo> {
    val dayList = emptyList<DayWeatherInfo>().toMutableList()
    for (hourData in this) {
        val hour = hourData.timeTxt.subSequence(11, 13)
        if (hour == "12") dayList.add(hourData.toDayWeatherInfo())
    }

    return dayList
}

private fun WeatherInfoDTO.toDayWeatherInfo(): DayWeatherInfo {
    return DayWeatherInfo(
        temp = if (mainInfoDTO.temp.toInt() >= 0)
            "+" + mainInfoDTO.temp.toInt().toString()
        else
            mainInfoDTO.temp.toInt().toString(),
        humidity = mainInfoDTO.humidity.toString(),
        condMain = weatherDescriptionDTO[0].condMain,
        iconId = 0,
        windSpeed = windInfoDTO.speed.toInt().toString(),
        time = time,
        timeTxt = timeTxt
    )
}

fun CityInfo.toCityInfoDBO(): CityInfoDBO {
    return CityInfoDBO(
        name = name,
        sunriseTime = sunriseTime,
        sunsetTime = sunsetTime
    )
}

fun HourWeatherInfo.toHourWeatherInfoDBO() : HourWeatherInfoDBO {
    return HourWeatherInfoDBO(
        time = time,
        timeTxt = timeTxt,
        temp = temp,
        feelsLike = feelsLike,
        humidity = humidity,
        windSpeed = windSpeed,
        weatherCondition = weatherCondition,
        weatherConditionIconId = 0
    )
}

fun DayWeatherInfo.toDayWeatherInfoDBO(): DayWeatherInfoDBO {
    return DayWeatherInfoDBO(
        temp = temp,
        humidity = humidity,
        windSpeed = windSpeed,
        condMain = condMain,
        iconId = 0,
        time = time,
        timeTxt = timeTxt
    )
}