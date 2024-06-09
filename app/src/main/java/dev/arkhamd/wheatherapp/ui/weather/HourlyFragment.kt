package dev.arkhamd.wheatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.data.model.HourWeatherInfo
import dev.arkhamd.wheatherapp.databinding.FragmentHourlyBinding
import dev.arkhamd.wheatherapp.ui.weather.recyclerView.SemiCircularLayoutManager
import dev.arkhamd.wheatherapp.ui.weather.recyclerView.WeatherAdapter
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherViewModel

class HourlyFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentHourlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHourlyBinding.inflate(inflater)

        binding.recyclerView.layoutManager = SemiCircularLayoutManager()

        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->
            if (weather.data != null) {
                setLoadedData(
                    binding,
                    weather.data.hourWeatherInfo
                )

                binding.recyclerView.adapter =
                    WeatherAdapter(weather.data.hourWeatherInfo.subList(0, 6)) { index ->
                        val hourWeatherData = weather.data.hourWeatherInfo[index]
                        changeMainWeatherInfo(binding,
                            iconId =  hourWeatherData.weatherConditionIconId,
                            temperature = hourWeatherData.temp,
                            humidity = hourWeatherData.humidity,
                            windSpeed = hourWeatherData.windSpeed,
                            temperatureFeelsLike = hourWeatherData.feelsLike
                        )
                    }
                binding.weatherMainInfo.shimmerLayout.hideShimmer()
            } else {
                binding.weatherMainInfo.shimmerLayout.startShimmer()
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


    companion object {
        @JvmStatic
        fun newInstance() = HourlyFragment()
    }
}