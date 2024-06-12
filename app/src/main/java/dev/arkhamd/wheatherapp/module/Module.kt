package dev.arkhamd.wheatherapp.module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import dev.arkhamd.wheatherapp.ui.WeatherActivity
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
            apiKey = API_KEY
        )
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return WeatherDatabase(context)
    }

    @Provides
    @Singleton
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        val intent = Intent(context, WeatherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, "Weather Channel ID")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Weather Channel ID",
                "Weather Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        return notificationManager
    }
}