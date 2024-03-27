package dev.arkhamd.lib

import dev.arkhamd.lib.model.GeocodingApiInterceptor
import dev.arkhamd.lib.model.Response
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://openweathermap.org/api/geocoding-api
 */

interface GeocodingApi {
    @GET("/geo/1.0/direct")
    fun getCords(
        @Query("q") cityName: String,
    ): Single<List<Response>>
}

fun GeocodingApi(
    baseUrl: String,
    apiKey: String,
): GeocodingApi {
    val client = OkHttpClient.Builder()
        .addInterceptor(GeocodingApiInterceptor(apiKey))
        .build()

    val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    return retrofit.create()
}