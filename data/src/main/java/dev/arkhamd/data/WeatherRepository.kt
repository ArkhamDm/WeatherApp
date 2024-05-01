package dev.arkhamd.data

import android.content.ContentValues.TAG
import android.util.Log
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.lib.GeocodingApi
import dev.arkhamd.weaherdatabase.WeatherDatabase
import dev.arkhamd.weaherdatabase.model.CityInfoDBO
import dev.arkhamd.weaherdatabase.model.DayWeatherInfoDBO
import dev.arkhamd.weaherdatabase.model.HourWeatherInfoDBO
import dev.arkhamd.weatherapi.WeatherApi
import io.reactivex.Completable
import io.reactivex.Single
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit

class WeatherRepository @Inject constructor(
    private val database: WeatherDatabase,
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi
) {
    fun getWeather(latitude: Float, longitude: Float): Single<RequestResult<WeatherInfo>> {
        return getDataFromApi(latitude, longitude)
            .onErrorResumeNext(getDataFromDatabase())
    }

    private fun getDataFromDatabase(): Single<RequestResult<WeatherInfo>> {
        return database.weatherInfoDao.getAllCityInfo()
            .flatMap { cityInfoDBO ->
                database.weatherInfoDao.getAllHourInfo()
                    .flatMap { listHourInfoDBO ->
                        database.weatherInfoDao.getAllDayInfo()
                            .flatMap { listDayInfoDBO ->
                                if (listDayInfoDBO.isNotEmpty()) {
                                    val data = WeatherInfo(
                                        cityInfoDBO.toCityInfo(),
                                        listHourInfoDBO.map { it.toHourWeatherInfo() },
                                        listDayInfoDBO.map { it.toDayWeatherInfo() }
                                    )
                                    Single.just(RequestResult.DatabaseSuccess(data))
                                } else {
                                    Single.just(RequestResult.Error())
                                }

                            }
                    }
            }
            .doOnSuccess { data ->
                Log.d(TAG, "get data from database ${data.data?.hourWeatherInfo?.size ?: "error"}")
            }
            .doOnError {
                Log.d(TAG, "error database", it)
            }
    }

    private fun getDataFromApi(latitude: Float, longitude: Float): Single<RequestResult<WeatherInfo>> {
        return weatherApi.getForecast(lat = latitude, lon = longitude)
            .timeout(5, TimeUnit.SECONDS)
            .flatMap { response ->
                if (response.weatherInfoDTO.isNotEmpty()) {
                    val data = WeatherInfo(
                        cityInfo = response.cityInfoDTO.toCityInfo(),
                        hourWeatherInfo = response.weatherInfoDTO.map { it.toHourWeatherInfo() },
                        dayWeatherInfo = response.weatherInfoDTO.toDayWeatherInfo()
                    )
                    Single.just(RequestResult.ApiSuccess(data))
                } else {
                    Single.just(RequestResult.Error())
                }
            }
            .doOnSuccess { weather ->
                if (weather.data?.hourWeatherInfo?.isNotEmpty() == true) {
                    val cityInfo = weather.data.cityInfo
                    val hourWeatherInfo = weather.data.hourWeatherInfo
                    val dayWeatherInfo = weather.data.dayWeatherInfo
                    clearAndAddDataToDatabase(
                        cityInfoDBO = cityInfo.toCityInfoDBO(),
                        hourWeatherInfoDBO = hourWeatherInfo.map { it.toHourWeatherInfoDBO() },
                        dayWeatherInfoDBO = dayWeatherInfo.map { it.toDayWeatherInfoDBO() }
                    ).subscribe()
                }

                Log.d(TAG, "get data from api ${weather.data?.hourWeatherInfo?.size ?: "error"}")
            }

    }

    /*private fun getDataFromApi(city: String): Single<RequestResult<WeatherInfo>> {
        return geocodingApi.getCords(city)
            .timeout(5, TimeUnit.SECONDS)
            .flatMap { cords ->
                weatherApi.getForecast(lat = cords[0].lat, lon = cords[0].lon)
                    .flatMap { response ->
                        if (response.weatherInfoDTO.isNotEmpty()) {
                            val data = WeatherInfo(
                                cityInfo = response.cityInfoDTO.toCityInfo(),
                                hourWeatherInfo = response.weatherInfoDTO.map { it.toHourWeatherInfo() },
                                dayWeatherInfo = response.weatherInfoDTO.toDayWeatherInfo()
                            )
                            Single.just(RequestResult.ApiSuccess(data))
                        } else {
                            Single.just(RequestResult.Error())
                        }
                    }
            }
            .doOnSuccess { weather ->
                if (weather.data?.hourWeatherInfo?.isNotEmpty() == true) {
                    val cityInfo = weather.data.cityInfo
                    val hourWeatherInfo = weather.data.hourWeatherInfo
                    val dayWeatherInfo = weather.data.dayWeatherInfo
                    clearAndAddDataToDatabase(
                        cityInfoDBO = cityInfo.toCityInfoDBO(),
                        hourWeatherInfoDBO = hourWeatherInfo.map { it.toHourWeatherInfoDBO() },
                        dayWeatherInfoDBO = dayWeatherInfo.map { it.toDayWeatherInfoDBO() }
                    ).subscribe()
                }

                Log.d(TAG, "get data from api ${weather.data?.hourWeatherInfo?.size ?: "error"}")
            }

    }*/

    private fun clearAndAddDataToDatabase(
        cityInfoDBO: CityInfoDBO,
        hourWeatherInfoDBO: List<HourWeatherInfoDBO>,
        dayWeatherInfoDBO: List<DayWeatherInfoDBO>
    ): Completable {
        return database.weatherInfoDao.clearAllInfo()
            .andThen(
                database.weatherInfoDao.insertCityInfo(cityInfoDBO)
            )
            .andThen(
                database.weatherInfoDao.insertHourInfo(hourWeatherInfoDBO)
            )
            .andThen(
                database.weatherInfoDao.insertDayInfo(dayWeatherInfoDBO)
            )
            .doOnComplete {
                Log.d(TAG, "cleared database and insert weatherInfo")
            }
    }

}

sealed class RequestResult<E>(val data: E?) {
    class DatabaseSuccess<E>(data: E): RequestResult<E>(data)
    class ApiSuccess<E>(data: E): RequestResult<E>(data)
    class Error<E>: RequestResult<E>(null)
}