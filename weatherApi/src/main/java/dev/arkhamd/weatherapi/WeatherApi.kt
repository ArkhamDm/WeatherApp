package dev.arkhamd.weatherapi

import dev.arkhamd.weatherapi.model.ResponseDTO
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    /**
     * API [here](https://openweathermap.org/forecast5)
     */
    @GET("/data/2.5/forecast")
    fun getForecast(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String? = "metric",
        @Query("cnt") numOfTimestamps: Int? = null,
        @Query("lang") lang: String = "ru",
    ): Single<ResponseDTO>
}

fun WeatherApi(
    baseUrl: String,
    apiKey: String
): WeatherApi {
    val client = OkHttpClient.Builder()
        .addInterceptor(WeatherApiInterceptor(apiKey))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    return retrofit.create()
}