package dev.arkhamd.wheatherapp

import android.app.Application
import android.content.Context
import android.content.Intent
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import dev.arkhamd.wheatherapp.ui.MapActivity
import dev.arkhamd.wheatherapp.ui.WeatherActivity

@HiltAndroidApp
class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)

        val sharedPreferences =
            applicationContext.getSharedPreferences("cords_prefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("lat", -1f)
        val lon = sharedPreferences.getFloat("lon", -1f)

        val intent = if (lat == -1f) {
            Intent(applicationContext, MapActivity::class.java)
        } else {
            Intent(applicationContext, WeatherActivity::class.java)
                .putExtra("cords", floatArrayOf(lat, lon))
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}