package dev.arkhamd.wheatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.data.model.WeatherInfo
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentHourlyBinding
import dev.arkhamd.wheatherapp.databinding.WeatherItemRadioButtonBinding
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherViewModel

class HourlyFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentHourlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHourlyBinding.inflate(inflater)

        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->

            when (weather) {
                is WeatherResult.Api -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data
                        )

                    }
                    binding.weatherMainInfo.shimmerLayout.hideShimmer()
                }
                is WeatherResult.Database -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data
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
        weatherData: List<WeatherInfo>
    ) {
        changeMainWeatherInfo(
            binding,
            temperature = weatherData[0].mainInfo.temp.toString(),
            humidity = weatherData[0].mainInfo.humidity.toString(),
            windSpeed = weatherData[0].windInfo.speed.toInt().toString(),
            temperatureFeelsLike = weatherData[0].mainInfo.feelsLike.toString()
        )

        changeHourData(
            binding,
            hoursData = weatherData
        )

        setOnImagesClickListener(
            binding,
            weatherData = weatherData
        )
    }

    private fun changeMainWeatherInfo(
        binding: FragmentHourlyBinding,
        temperature: String,
        humidity: String,
        windSpeed: String,
        temperatureFeelsLike: String
    ) {
        binding.weatherMainInfo.apply {
            tempInfo.text = temperature
            humidityInfo.text = humidity
            windSpeedInfo.text = windSpeed
            tempFeelsInfo.text = temperatureFeelsLike
        }
    }

    private fun changeHourData(binding: FragmentHourlyBinding, hoursData: List<WeatherInfo>) {
        for (hour in 1 .. 6) {
            val weatherHourInfo = hoursData[hour]

            when (hour) {
                1 -> {
                    binding.weatherHour1.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
                2 -> {
                    binding.weatherHour2.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
                3 -> {
                    binding.weatherHour3.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
                4 -> {
                    binding.weatherHour4.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
                5 -> {
                    binding.weatherHour5.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
                else -> {
                    binding.weatherHour6.apply {
                        timeText.text = weatherHourInfo.timeTxt.substring(11, 16)
                        discriptionText.text = weatherHourInfo.weatherDescription.condMain
                    }
                }
            }
        }
    }

    private fun setOnImagesClickListener(
        binding: FragmentHourlyBinding,
        weatherData: List<WeatherInfo>
    ) {
        binding.weatherHour1.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[1]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

            setChecked(binding, binding.weatherHour1)
        }
        binding.weatherHour2.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[2]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

            setChecked(binding, binding.weatherHour2)
        }
        binding.weatherHour3.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[3]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

            setChecked(binding, binding.weatherHour3)
        }
        binding.weatherHour4.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[4]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

            setChecked(binding, binding.weatherHour4)
        }
        binding.weatherHour5.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[5]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

            setChecked(binding, binding.weatherHour5)
        }
        binding.weatherHour6.fullLayout.setOnClickListener {
            val weatherHourData = weatherData[6]
            changeMainWeatherInfo(
                binding,
                temperature = weatherHourData.mainInfo.temp.toString(),
                humidity = weatherHourData.mainInfo.humidity.toString(),
                windSpeed = weatherHourData.windInfo.speed.toInt().toString(),
                temperatureFeelsLike = weatherHourData.mainInfo.feelsLike.toString()
            )

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