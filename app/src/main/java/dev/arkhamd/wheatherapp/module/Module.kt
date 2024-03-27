package dev.arkhamd.wheatherapp.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.arkhamd.lib.GeocodingApi
import dev.arkhamd.weaherdatabase.WeatherDatabase
import dev.arkhamd.weatherapi.WeatherApi
import dev.arkhamd.wheatherapp.BuildConfig
import dev.arkhamd.wheatherapp.BuildConfig.API_KEY
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return WeatherApi(
            baseUrl = BuildConfig.WEATHER_BASE_URL,
            apiKey = API_KEY,
        )
    }

    @Provides
    @Singleton
    fun provideGeocodingApi(): GeocodingApi {
        return GeocodingApi(
            baseUrl = BuildConfig.GEOCODING_BASE_URL,
            apiKey = BuildConfig.API_KEY
        )
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return WeatherDatabase(context)
    }
}