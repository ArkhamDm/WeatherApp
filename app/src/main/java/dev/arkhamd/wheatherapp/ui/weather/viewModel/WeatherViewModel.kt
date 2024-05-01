package dev.arkhamd.wheatherapp.ui.weather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arkhamd.data.RequestResult
import dev.arkhamd.data.WeatherRepository
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.wheatherapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
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
            "Thunderstorm" -> "Гроза"
            "Drizzle" -> "Мелкий дождь"
            "Rain" -> "Дождь"
            "Snow" -> "Снег"
            "Atmosphere" -> "Смог"
            "Clear" -> "Ясно"
            "Clouds" -> "Облачно"
            else -> "Неизвестность"
        }
    }

    private fun setCondIcon(condition: String): Int {
        return when (condition) {
            "Гроза" -> R.drawable.thunderstorm
            "Мелкий дождь" -> R.drawable.drizzle
            "Дождь" -> R.drawable.rain
            "Снег" -> R.drawable.snow
            "Туман" -> R.drawable.mist
            "Ясно" -> R.drawable.sun
            "Облачно" -> R.drawable.cloudy
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