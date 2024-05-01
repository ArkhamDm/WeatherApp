package dev.arkhamd.wheatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.data.model.HourWeatherInfo
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentHourlyBinding
import dev.arkhamd.wheatherapp.databinding.WeatherItemRadioButtonBinding
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherViewModel

class HourlyFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentHourlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHourlyBinding.inflate(inflater)

        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->

            when (weather) {
                is WeatherResult.Api -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data.hourWeatherInfo
                        )

                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }
                is WeatherResult.Database -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data.hourWeatherInfo
                        )

                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }
                is WeatherResult.Error -> {

                }
                is WeatherResult.Loading -> {
                    binding.weatherMainInfo.shimmerLayout.startShimmer()
                }
            }
        }



        return binding.root
    }

    private fun setLoadedData(
        binding: FragmentHourlyBinding,
        hourData: List<HourWeatherInfo>
    ) {
        changeMainWeatherInfo(
            binding,
            iconId = hourData[0].weatherConditionIconId,
            temperature = hourData[0].temp,
            humidity = hourData[0].humidity,
            windSpeed = hourData[0].windSpeed,
            temperatureFeelsLike = hourData[0].feelsLike
        )

        changeHourData(
            binding,
            hourData = hourData
        )

        setOnImagesClickListener(
            binding,
            hourData = hourData
        )
    }

    private fun changeMainWeatherInfo(
        binding: FragmentHourlyBinding,
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

    private fun changeHourData(binding: FragmentHourlyBinding, hourData: List<HourWeatherInfo>) {
        for (hour in 1 .. 6) {
            val weatherHourInfo = hourData[hour]

            when (hour) {
                1 -> {
                    binding.weatherHour1.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
                2 -> {
                    binding.weatherHour2.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
                3 -> {
                    binding.weatherHour3.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
                4 -> {
                    binding.weatherHour4.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
                5 -> {
                    binding.weatherHour5.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
                else -> {
                    binding.weatherHour6.apply {
                        hourWeatherIcon.setImageResource(weatherHourInfo.weatherConditionIconId)
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherCondition
                    }
                }
            }
        }
    }

    private fun setOnImagesClickListener(
        binding: FragmentHourlyBinding,
        hourData: List<HourWeatherInfo>
    ) {
        binding.weatherHour1.fullLayout.setOnClickListener {
            hourData[1].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour1)
        }
        binding.weatherHour2.fullLayout.setOnClickListener {
            hourData[2].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour2)
        }
        binding.weatherHour3.fullLayout.setOnClickListener {
            hourData[3].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour3)
        }
        binding.weatherHour4.fullLayout.setOnClickListener {
            hourData[4].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour4)
        }
        binding.weatherHour5.fullLayout.setOnClickListener {
            hourData[5].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour5)
        }
        binding.weatherHour6.fullLayout.setOnClickListener {
            hourData[6].apply {
                changeMainWeatherInfo(
                    binding,
                    iconId = weatherConditionIconId,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    temperatureFeelsLike = feelsLike
                )
            }

            setChecked(binding, binding.weatherHour6)
        }
    }

    private fun setChecked(
        fragmentHourlyBinding: FragmentHourlyBinding,
        weatherItemBinding: WeatherItemRadioButtonBinding
    ) {
        fragmentHourlyBinding.apply {
            weatherHour1.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
            weatherHour2.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
            weatherHour3.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
            weatherHour4.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
            weatherHour5.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
            weatherHour6.circleShapeImage.setImageResource(R.drawable.cirle_weather_item)
        }

        weatherItemBinding.circleShapeImage.setImageResource(R.drawable.cirle_weather_item_selected)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HourlyFragment()
    }
}