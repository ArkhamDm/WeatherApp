package dev.arkhamd.wheatherapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)
    }
}