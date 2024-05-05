package dev.arkhamd.wheatherapp.ui.weather.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.arkhamd.data.RequestResult
import dev.arkhamd.data.WeatherRepository
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.wheatherapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val applicationContext: Context
): ViewModel() {
    private val _weatherInfo: MutableLiveData<WeatherResult<WeatherInfo>> by lazy {
        MutableLiveData<WeatherResult<WeatherInfo>>(WeatherResult.Loading())
    }
    val weatherInfo: LiveData<WeatherResult<WeatherInfo>>
        get() = _weatherInfo

    fun update(latitude: Float, longitude: Float) {
        val disposable = weatherRepository.getWeather(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                when (response) {
                    is RequestResult.DatabaseSuccess -> {
                        for (hourInfo in response.data!!.hourWeatherInfo) {
                            hourInfo.weatherCondition = translateConditions(hourInfo.weatherCondition)
                            hourInfo.weatherConditionIconId = setCondIcon(hourInfo.weatherCondition)
                        }
                        for (dayInfo in response.data!!.dayWeatherInfo) {
                            dayInfo.condMain = translateConditions(dayInfo.condMain)
                            dayInfo.iconId = setCondIcon(dayInfo.condMain)
                        }

                        _weatherInfo.postValue(
                            WeatherResult.Database(response.data!!)
                        )
                    }

                    is RequestResult.ApiSuccess -> {
                        for (weatherInfo in response.data!!.hourWeatherInfo) {
                            weatherInfo.weatherCondition = translateConditions(weatherInfo.weatherCondition)
                            weatherInfo.weatherConditionIconId = setCondIcon(weatherInfo.weatherCondition)
                        }
                        for (dayInfo in response.data!!.dayWeatherInfo) {
                            dayInfo.condMain = translateConditions(dayInfo.condMain)
                            dayInfo.iconId = setCondIcon(dayInfo.condMain)
                        }

                        _weatherInfo.postValue(
                            WeatherResult.Api(response.data!!)
                        )
                    }

                    is RequestResult.Error -> {
                        _weatherInfo.postValue(
                            WeatherResult.Error()
                        )
                    }
                }
            }
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
            else -> applicationContext.getString(R.string.undefined)
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

sealed class WeatherResult<E>(val data: E?) {
    class Loading<E>: WeatherResult<E>(null)
    class Database<E>(data: E): WeatherResult<E>(data)
    class Api<E>(data: E): WeatherResult<E>(data)
    class Error<E>: WeatherResult<E>(null)
}