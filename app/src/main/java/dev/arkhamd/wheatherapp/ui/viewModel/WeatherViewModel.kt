package dev.arkhamd.wheatherapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arkhamd.data.RequestResult
import dev.arkhamd.data.WeatherRepository
import dev.arkhamd.data.model.WeatherInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    private val _weatherInfo: MutableLiveData<WeatherResult<List<WeatherInfo>>> by lazy {
        MutableLiveData<WeatherResult<List<WeatherInfo>>>(WeatherResult.Loading())
    }
    val weatherInfo: LiveData<WeatherResult<List<WeatherInfo>>>
        get() = _weatherInfo

    fun update(city: String) {
        val disposable = weatherRepository.getWeather(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                when (response) {
                    is RequestResult.DatabaseSuccess -> {
                        _weatherInfo.postValue(
                            WeatherResult.Database(response.data!!)
                        )
                    }

                    is RequestResult.ApiSuccess -> {
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
}

sealed class WeatherResult<E>(val data: E?) {
    class Loading<E>: WeatherResult<E>(null)
    class Database<E>(data: E): WeatherResult<E>(data)
    class Api<E>(data: E): WeatherResult<E>(data)
    class Error<E>: WeatherResult<E>(null)
}