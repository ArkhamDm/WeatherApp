package dev.arkhamd.data

import android.content.ContentValues.TAG
import android.util.Log
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.lib.GeocodingApi
import dev.arkhamd.weaherdatabase.WeatherDatabase
import dev.arkhamd.weaherdatabase.model.WeatherInfoDBO
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
    fun getWeather(city: String): Single<RequestResult<List<WeatherInfo>>> {
        return getDataFromApi(city)
            .onErrorResumeNext(getDataFromDatabase())
    }

    private fun getDataFromDatabase(): Single<RequestResult<List<WeatherInfo>>> {
        return database.weatherInfoDao.getAll()
            .flatMap { response ->
                if (response.isNotEmpty()) {
                    Single.just(RequestResult.DatabaseSuccess(response.map { it.toWeatherInfo() }))
                } else {
                    Single.just(RequestResult.Error())
                }
            }
            .doOnSuccess { data ->
                Log.d(TAG, "get data from database ${data.data?.size}")
            }
            .doOnError { it ->
                Log.d(TAG, "error database", it)
            }
    }

    private fun getDataFromApi(city: String): Single<RequestResult<List<WeatherInfo>>> {
        return geocodingApi.getCords(city)
            .timeout(5, TimeUnit.SECONDS)
            .flatMap { cords ->
                weatherApi.getForecast(lat = cords[0].lat, lon = cords[0].lon)
                    .flatMap { response ->
                        if (response.weatherInfoDTO.isNotEmpty()) {
                            val data = response.weatherInfoDTO.map { it.toWeatherInfo() }
                            Single.just(RequestResult.ApiSuccess(data))
                        } else {
                            Single.just(RequestResult.Error())
                        }
                    }
            }
            .doOnSuccess {
                if (!it.data.isNullOrEmpty()) {
                    clearAndAddDataToDatabase(
                        it.data.map { weatherInfo -> weatherInfo.toWeatherInfoDBO() }
                    ).subscribe()
                }

                Log.d(TAG, "get data from api ${it.data?.size}")
            }

    }

    private fun clearAndAddDataToDatabase(weatherInfo: List<WeatherInfoDBO>): Completable {
        return database.weatherInfoDao.clear()
            .andThen(
                database.weatherInfoDao.insert(weatherInfo)
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