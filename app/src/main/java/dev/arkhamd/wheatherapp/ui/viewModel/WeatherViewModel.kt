package dev.arkhamd.wheatherapp.ui.viewModel

import android.content.ContentValues.TAG
import android.util.Log
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
import javax.inject.Singleton

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    private val _weatherInfo: MutableLiveData<WeatherResult<List<WeatherInfo>>> by lazy {
        MutableLiveData<WeatherResult<List<WeatherInfo>>>(WeatherResult.OtherError())
    }
    val weatherInfo: LiveData<WeatherResult<List<WeatherInfo>>>
        get() = _weatherInfo

    fun update(city: String) {
        val disposable = weatherRepository.getWeather(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    when (response) {
                        is RequestResult.DatabaseSuccess -> {
                            _weatherInfo.postValue(
                                WeatherResult.Database(response.data)
                            )
                        }
                        is RequestResult.ApiSuccess -> {
                            _weatherInfo.postValue(
                                WeatherResult.Api(response.data)
                            )
                        }
                        is RequestResult.ApiError -> {
                            _weatherInfo.postValue(
                                WeatherResult.ApiError(response.data)
                            )
                        }
                    }
                },
                {
                    _weatherInfo.postValue(
                        WeatherResult.OtherError()
                    )
                    Log.e(TAG, it.message.toString())
                },
            )
    }
}

sealed class WeatherResult<E>(val data: List<WeatherInfo>?) {
    class Database<E>(data: List<WeatherInfo>): WeatherResult<E>(data)
    class Api<E>(data: List<WeatherInfo>): WeatherResult<E>(data)
    class ApiError<E>(data: List<WeatherInfo>): WeatherResult<E>(data)
    class OtherError<E>: WeatherResult<E>(null)
}