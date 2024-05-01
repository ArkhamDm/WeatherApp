package dev.arkhamd.wheatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                is WeatherResult.Api -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data.dayWeatherInfo
                        )

                    }

                    hideShimmer(binding)
                }
                is WeatherResult.Database -> {
                    if (weather.data != null) {

                        setLoadedData(
                            binding,
                            weather.data.dayWeatherInfo
                        )

                    }

                    hideShimmer(binding)
                }
                is WeatherResult.Error -> {

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
        binding.dayWeather1.apply {
            dayData[0].apply {
                dayTemperatureText.text = temp
                dayWeatherIcon.setImageResource(iconId)
                dayWeatherDescText.text = condMain
                dayHumidityText.text = humidity + "%"
                dayWindSpeedText.text = windSpeed + " " + getString(R.string.metres_in_second)
                dayDateText.text = getDate(time)
                dayWeekdayText.text = getWeekDay(time)
            }
        }
        binding.dayWeather2.apply {
            dayData[1].apply {
                dayTemperatureText.text = temp
                dayWeatherIcon.setImageResource(iconId)
                dayWeatherDescText.text = condMain
                dayHumidityText.text = humidity + "%"
                dayWindSpeedText.text = windSpeed + " " + getString(R.string.metres_in_second)
                dayDateText.text = getDate(time)
                dayWeekdayText.text = getWeekDay(time)
            }
        }
        binding.dayWeather3.apply {
            dayData[2].apply {
                dayTemperatureText.text = temp
                dayWeatherIcon.setImageResource(iconId)
                dayWeatherDescText.text = condMain
                dayHumidityText.text = humidity + "%"
                dayWindSpeedText.text = windSpeed + " " + getString(R.string.metres_in_second)
                dayDateText.text = getDate(time)
                dayWeekdayText.text = getWeekDay(time)
            }
        }
        binding.dayWeather4.apply {
            dayData[3].apply {
                dayTemperatureText.text = temp
                dayWeatherIcon.setImageResource(iconId)
                dayWeatherDescText.text = condMain
                dayHumidityText.text = humidity + "%"
                dayWindSpeedText.text = windSpeed + " " + getString(R.string.metres_in_second)
                dayDateText.text = getDate(time)
                dayWeekdayText.text = getWeekDay(time)
            }
        }
        if (dayData.size > 4) {
            binding.dayWeather5.apply {
                dayData[4].apply {
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
            dayWeather1.shimmerLayout.startShimmer()
            dayWeather2.shimmerLayout.startShimmer()
            dayWeather3.shimmerLayout.startShimmer()
            dayWeather4.shimmerLayout.startShimmer()
            dayWeather5.shimmerLayout.startShimmer()
        }
    }
    private fun hideShimmer(
            binding: FragmentWeeklyBinding
    ) {
        binding.apply {
            dayWeather1.shimmerLayout.hideShimmer()
            dayWeather2.shimmerLayout.hideShimmer()
            dayWeather3.shimmerLayout.hideShimmer()
            dayWeather4.shimmerLayout.hideShimmer()
            dayWeather5.shimmerLayout.hideShimmer()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeeklyFragment()
    }
}