package dev.arkhamd.wheatherapp.ui.weather.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.arkhamd.data.RequestResult
import dev.arkhamd.data.WeatherRepository
import dev.arkhamd.wheatherapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted context: Context, @Assisted workerParameters: WorkerParameters,
    private val weatherRepository: WeatherRepository,
    private val weatherNotification: WeatherNotification
): Worker(context, workerParameters) {
    override fun doWork(): Result {
        val cords = getCords()

        try {
            val weatherData = weatherRepository.getWeather(cords.first, cords.second)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingGet()

            return if (weatherData is RequestResult.ApiSuccess) {
                weatherData.data!!.hourWeatherInfo.map {
                    it.weatherCondition = translateConditions(it.weatherCondition)
                    it.weatherConditionIconId = setCondIcon(it.weatherCondition)
                }

                weatherNotification.showWeatherNotification(weatherData.data!!.hourWeatherInfo.subList(0, 4))
                Result.success()
            } else {
                Log.d("WeatherWorker", "Error reciving data")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("WeatherWorker", "Error Weather Worker", e)
            Result.failure()
        }

        return Result.success()
    }

    private fun getCords(): Pair<Float, Float> {
        val sharedPreferences =
            applicationContext.getSharedPreferences("cords_prefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("lat", -1f)
        val lon = sharedPreferences.getFloat("lon", -1f)

        return Pair(lat, lon)
    }

    private fun translateConditions(condition: String): String {
        return when (condition) {
            "Thunderstorm" -> applicationContext.getString(R.string.thunderstorm)
            "Drizzle" -> applicationContext.getString(R.string.drizzle)
            "Rain" -> applicationContext.getString(R.string.rain)
            "Snow" -> applicationContext.getString(R.string.snow)
            "Atmosphere" -> applicationContext.getString(R.string.atmosphere)
            "Clear" -> applicationContext.getString(R.string.clear)
            "Clouds" -> applicationContext.getString(R.string.clouds)
            else -> applicationContext.getString(R.string.mainInfoDayOfWeek)
        }
    }

    private fun setCondIcon(condition: String): Int {
        return when (condition) {
            applicationContext.getString(R.string.thunderstorm) -> R.drawable.thunderstorm
            applicationContext.getString(R.string.drizzle) -> R.drawable.drizzle
            applicationContext.getString(R.string.rain) -> R.drawable.rain
            applicationContext.getString(R.string.snow) -> R.drawable.snow
            applicationContext.getString(R.string.atmosphere) -> R.drawable.mist
            applicationContext.getString(R.string.clear) -> R.drawable.sun
            applicationContext.getString(R.string.clouds) -> R.drawable.cloudy
            else -> R.drawable.weather_warning
        }
    }
}