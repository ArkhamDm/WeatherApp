package dev.arkhamd.wheatherapp.ui.weather

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentNowBinding
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherViewModel

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
                is WeatherResult.Api, is WeatherResult.Database -> {
                    if (weather.data != null) {
                        changeMainWeatherInfo(
                            binding,
                            iconId = weather.data.hourWeatherInfo[0].weatherConditionIconId,
                            temperature = weather.data.hourWeatherInfo[0].temp,
                            humidity = weather.data.hourWeatherInfo[0].humidity,
                            windSpeed = weather.data.hourWeatherInfo[0].windSpeed,
                            temperatureFeelsLike = weather.data.hourWeatherInfo[0].feelsLike
                        )
                        setLottiAnim(binding, weather.data.hourWeatherInfo[0].weatherCondition)
                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }

                is WeatherResult.Error -> {
                    Toast.makeText(context,
                        getString(R.string.error_weather_data), Toast.LENGTH_SHORT).show()
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

    private fun setLottiAnim(
        binding: FragmentNowBinding,
        weatherDescription: String
    ) {
        when (weatherDescription) {
            getString(R.string.rain) -> {
                binding.imageView.setColorFilter(Color.argb(48, 0, 0, 0))
                binding.lottieAnimationView
                    .setAnimationFromUrl("https://lottie.host/85864202-c1bf-454b-8e26-6491e79bfbcb/8QNz6W1a9G.json")
            }
            getString(R.string.snow) -> {
                binding.lottieAnimationView
                    .setAnimationFromUrl("https://lottie.host/632e3e6e-d433-4ab1-8c35-0e7d070f449c/MYtPmfSTAM.json")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NowFragment()
    }
}