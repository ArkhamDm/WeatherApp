package dev.arkhamd.wheatherapp.ui.weather.notification

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dev.arkhamd.data.WeatherRepository
import javax.inject.Inject

class WeatherWorkerFactory @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val weatherNotification: WeatherNotification
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker =
        WeatherWorker(appContext, workerParameters, weatherRepository, weatherNotification)
}