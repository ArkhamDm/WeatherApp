package dev.arkhamd.data

import dev.arkhamd.data.model.CloudInfo
import dev.arkhamd.data.model.MainInfo
import dev.arkhamd.data.model.PartOfDay
import dev.arkhamd.data.model.RainInfo
import dev.arkhamd.data.model.SnowInfo
import dev.arkhamd.data.model.WeatherDescription
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.data.model.WindInfo
import dev.arkhamd.weaherdatabase.model.CloudInfoDBO
import dev.arkhamd.weaherdatabase.model.MainInfoDBO
import dev.arkhamd.weaherdatabase.model.RainInfoDBO
import dev.arkhamd.weaherdatabase.model.SnowInfoDBO
import dev.arkhamd.weaherdatabase.model.WeatherDescriptionDBO
import dev.arkhamd.weaherdatabase.model.WeatherInfoDBO
import dev.arkhamd.weaherdatabase.model.WindInfoDBO
import dev.arkhamd.weatherapi.model.WeatherInfoDTO

fun WeatherInfoDTO.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        time = time,
        mainInfo = MainInfo(
            temp = mainInfoDTO.temp.toInt(),
            feelsLike = mainInfoDTO.feelsLike.toInt(),
            tempMin = mainInfoDTO.tempMin.toInt(),
            tempMax = mainInfoDTO.tempMax.toInt(),
            pressure = mainInfoDTO.pressure,
            humidity = mainInfoDTO.humidity,
        ),
        weatherDescription = WeatherDescription(
            condId = weatherDescriptionDTO[0].condId,
            condMain = weatherDescriptionDTO[0].condMain,
            description = weatherDescriptionDTO[0].description,
        ),
        cloudInfo = CloudInfo(
            cloudiness = cloudInfoDTO.cloudiness
        ),
        windInfo = WindInfo(
            speed = windInfoDTO.speed,
            directionInDegrees = windInfoDTO.directionInDegrees,
        ),
        visibility = visibility,
        probOfPrecipitation = probOfPrecipitation,
        rainInfo = RainInfo(
            threeHours = rainInfoDTO?.threeHours ?: 0f
        ),
        snowInfo = SnowInfo(
            threeHours = snowInfoDTO?.threeHours ?: 0f
        ),
        timeTxt = timeTxt
    )
}

fun WeatherInfoDBO.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        time = time,
        mainInfo = MainInfo(
            temp = mainInfoDBO.temp,
            feelsLike = mainInfoDBO.feelsLike,
            tempMin = mainInfoDBO.tempMin,
            tempMax = mainInfoDBO.tempMax,
            pressure = mainInfoDBO.pressure,
            humidity = mainInfoDBO.humidity,
        ),
        weatherDescription = WeatherDescription(
            condId = weatherDescriptionDBO.condId,
            condMain = weatherDescriptionDBO.condMain,
            description = weatherDescriptionDBO.description,
        ),
        cloudInfo = CloudInfo(
            cloudiness = cloudInfoDBO.cloudiness
        ),
        windInfo = WindInfo(
            speed = windInfoDBO.speed.toFloat(),
            directionInDegrees = windInfoDBO.directionInDegrees,
        ),
        visibility = visibility,
        probOfPrecipitation = probOfPrecipitation,
        rainInfo = if (rainInfoDBO == null) null else
            RainInfo(threeHours =  rainInfoDBO!!.threeHours),
        snowInfo = if (snowInfoDBO == null) null else
            SnowInfo(threeHours = snowInfoDBO!!.threeHours),
        timeTxt = timeTxt
    )
}

fun WeatherInfo.toWeatherInfoDBO(): WeatherInfoDBO {
    return WeatherInfoDBO(
        time = time,
        mainInfoDBO = MainInfoDBO(
            temp = mainInfo.temp,
            feelsLike = mainInfo.feelsLike,
            tempMin = mainInfo.tempMin,
            tempMax = mainInfo.tempMax,
            pressure = mainInfo.pressure,
            humidity = mainInfo.humidity,
        ),
        weatherDescriptionDBO = WeatherDescriptionDBO(
            condId = weatherDescription.condId,
            condMain = weatherDescription.condMain,
            description = weatherDescription.description
        ),
        cloudInfoDBO = CloudInfoDBO(
            cloudiness = cloudInfo.cloudiness
        ),
        windInfoDBO = WindInfoDBO(
            speed = windInfo.speed.toInt(),
            directionInDegrees = windInfo.directionInDegrees
        ),
        visibility = visibility,
        probOfPrecipitation = probOfPrecipitation,
        rainInfoDBO = if (rainInfo == null) null else
            RainInfoDBO(threeHours = rainInfo.threeHours),
        snowInfoDBO = if (snowInfo == null) null else
            SnowInfoDBO(threeHours = snowInfo.threeHours),
        timeTxt = timeTxt
    )
}