package dev.arkhamd.wheatherapp.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.wheatherapp.databinding.FragmentNowBinding
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherViewModel

class NowFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentNowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowBinding.inflate(inflater)

        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->
            Log.d(TAG, weather.toString())
            when (weather) {
                is WeatherResult.Api -> {
                    if (weather.data != null) {
                        changeMainWeatherInfo(
                            binding,
                            iconId = weather.data.hourWeatherInfo[0].weatherConditionIconId,
                            temperature = weather.data.hourWeatherInfo[0].temp,
                            humidity = weather.data.hourWeatherInfo[0].humidity,
                            windSpeed = weather.data.hourWeatherInfo[0].windSpeed,
                            temperatureFeelsLike = weather.data.hourWeatherInfo[0].feelsLike
                        )
                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }

                is WeatherResult.Database -> {
                    if (weather.data != null) {
                        changeMainWeatherInfo(
                            binding,
                            iconId = weather.data.hourWeatherInfo[0].weatherConditionIconId,
                            temperature = weather.data.hourWeatherInfo[0].temp,
                            humidity = weather.data.hourWeatherInfo[0].humidity,
                            windSpeed = weather.data.hourWeatherInfo[0].windSpeed,
                            temperatureFeelsLike = weather.data.hourWeatherInfo[0].feelsLike
                        )
                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }

                is WeatherResult.Error -> {

                }

                is WeatherResult.Loading -> {
                    binding.weatherMainInfo.shimmerLayout.stopShimmer()
                }
            }
        }

        return binding.root

    }

    private fun changeMainWeatherInfo(
        binding: FragmentNowBinding,
        iconId: Int,
        temperature: String,
        humidity: String,
        windSpeed: String,
        temperatureFeelsLike: String
    ) {
        binding.weatherMainInfo.apply {
            weatherIcon.setImageResource(iconId)
            tempInfo.text = temperature
            humidityInfo.text = humidity
            windSpeedInfo.text = windSpeed
            tempFeelsInfo.text = temperatureFeelsLike
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NowFragment()
    }
}