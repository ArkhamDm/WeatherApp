package dev.arkhamd.data

import android.content.ContentValues.TAG
import android.util.Log
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.lib.GeocodingApi
import dev.arkhamd.lib.model.Response
import dev.arkhamd.weaherdatabase.WeatherDatabase
import dev.arkhamd.weaherdatabase.model.WeatherInfoDBO
import dev.arkhamd.weatherapi.WeatherApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit

class WeatherRepository @Inject constructor(
    private val database: WeatherDatabase,
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi
) {
    fun getWeather(city: String): Observable<RequestResult<List<WeatherInfo>>> {
        return Observable.concatArray(
            getDataFromDatabase(),
            getDataFromApi(city)
        )
            .onErrorResumeNext(getDataFromDatabase(apiError = true))
    }

    private fun getDataFromDatabase(apiError: Boolean = false): Observable<RequestResult<List<WeatherInfo>>> {
        return database.weatherInfoDao.getAll()
            .filter { it.isNotEmpty() }
            .map { response ->
                if (apiError) {
                    RequestResult.ApiError(response.map {it.toWeatherInfo()})
                }
                else {
                    RequestResult.DatabaseSuccess(response.map { it.toWeatherInfo() })
                }
            }
            .toObservable()
            .doOnNext {
                Log.d(TAG, "get data from database ${it.data.size}")
            }
    }

    private fun getDataFromApi(city: String): Observable<RequestResult.ApiSuccess<List<WeatherInfo>>> {
        return geocodingApi.getCords(city)
            .flatMap { cords ->
                weatherApi.getForecast(lat = cords[0].lat, lon = cords[0].lon)
                    .map { response ->
                        val result = response.weatherInfoDTO.map { it.toWeatherInfo() }
                        RequestResult.ApiSuccess(result)
                    }
            }
            .toObservable()
            .timeout(5, TimeUnit.SECONDS)
            .doOnNext {
                Log.d(TAG, "get data from api ${it.data.size}")
                if (it.data.isNotEmpty()) {
                    clearAndAddDataToDatabase(
                        it.data.map { weatherInfo -> weatherInfo.toWeatherInfoDBO() }
                    ).subscribe()
                }
            }
            .doOnError {
                Log.e(TAG, it.message.toString())
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

sealed class RequestResult<E>(val data: E, val msg: String?) {
    class ApiError<E>(data: E): RequestResult<E>(data, null)
    class DatabaseSuccess<E>(data: E): RequestResult<E>(data, null)
    class ApiSuccess<E>(data: E): RequestResult<E>(data, null)
}