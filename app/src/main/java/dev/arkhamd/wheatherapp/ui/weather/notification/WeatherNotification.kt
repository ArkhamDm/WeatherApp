package dev.arkhamd.wheatherapp.ui.weather.notification

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.arkhamd.data.model.HourWeatherInfo
import javax.inject.Inject

class WeatherNotification @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) {
    @SuppressLint("MissingPermission")
    fun showWeatherNotification(weatherInfo: List<HourWeatherInfo>) {
        val bigWeatherText = weatherInfo.joinToString("\n") { hour ->
            "${hour.timeTxt.subSequence(11, 16)} - ${hour.weatherCondition} ${hour.temp}"
        }
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(bigWeatherText)

        notificationManager.notify(0, notificationBuilder
            .setSmallIcon(weatherInfo[0].weatherConditionIconId)
            .setContentTitle("Прогноз на день!")
            .setContentText("${weatherInfo[0].weatherCondition} ${weatherInfo[0].temp}")
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        )
    }
}