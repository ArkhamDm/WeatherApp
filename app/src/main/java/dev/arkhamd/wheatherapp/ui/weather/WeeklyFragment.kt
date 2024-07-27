package dev.arkhamd.wheatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.data.model.DayWeatherInfo
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentWeeklyBinding
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeeklyFragment : Fragment() {
    lateinit var binding: FragmentWeeklyBinding
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyBinding.inflate(inflater)

        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->

            when (weather) {
                is WeatherResult.Api, is WeatherResult.Database -> {
                    if (weather.data != null) {
                        setLoadedData(
                            binding,
                            weather.data.dayWeatherInfo
                        )
                    }

                    hideShimmer(binding)
                }
                is WeatherResult.Error -> {
                    Toast.makeText(context,
                        getString(R.string.error_weather_data), Toast.LENGTH_SHORT).show()
                }
                is WeatherResult.Loading -> {
                    startShimmer(binding)
                }
            }
        }

        return binding.root
    }

    private fun setLoadedData(
        binding: FragmentWeeklyBinding,
        dayData: List<DayWeatherInfo>
    ) {
        mapOf(
            binding.dayWeather1 to 0,
            binding.dayWeather2 to 1,
            binding.dayWeather3 to 2,
            binding.dayWeather4 to 3,
            binding.dayWeather5 to 4
        ).forEach { (dayWeatherBinding, dayDataIndex) ->
            dayWeatherBinding.apply {
                dayData[dayDataIndex].apply {
                    dayTemperatureText.text = temp
                    dayWeatherIcon.setImageResource(iconId)
                    dayWeatherDescText.text = condMain
                    dayHumidityText.text = humidity + "%"
                    dayWindSpeedText.text = windSpeed + " " + getString(R.string.metres_in_second)
                    dayDateText.text = getDate(time)
                    dayWeekdayText.text = getWeekDay(time)
                }
            }
        }
    }

    private fun getWeekDay(timestamp: Long): String {
        val dateFormatDayOfWeek = SimpleDateFormat("EE", Locale.getDefault())
        val date = Date(timestamp * 1000)

        return dateFormatDayOfWeek.format(date).uppercase()
    }

    private fun getDate(timestamp: Long): String {
        val dateFormatDate = SimpleDateFormat("dd.MM", Locale.getDefault())
        val date = Date(timestamp * 1000)

        return dateFormatDate.format(date)
    }

    private fun startShimmer(
        binding: FragmentWeeklyBinding
    ) {
        binding.apply {
            listOf(
                dayWeather1, dayWeather2, dayWeather3, dayWeather4, dayWeather5
            ).forEach { it.shimmerLayout.startShimmer() }
        }
    }
    private fun hideShimmer(
            binding: FragmentWeeklyBinding
    ) {
        binding.apply {
            listOf(
                dayWeather1, dayWeather2, dayWeather3, dayWeather4, dayWeather5
            ).forEach { it.shimmerLayout.hideShimmer() }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeeklyFragment()
    }
}
