package dev.arkhamd.wheatherapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.arkhamd.wheatherapp.databinding.ActivityMainBinding
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        weatherViewModel.update(binding.cityText.text.toString())

        weatherViewModel.weatherInfo.observe(this) { weather ->
            if (weather is WeatherResult.Api) {
                binding.weatherState.setImageResource(R.drawable.weather_real)
            }

            if (weather.data != null) {
                setTimeOfData(binding, weather.data.hourWeatherInfo[0].time)
                binding.mainInfo.shimmerLayout.hideShimmer()
            } else {
                binding.mainInfo.shimmerLayout.startShimmer()
            }

        }

        binding.bottomNavigationView.setupWithNavController(
            supportFragmentManager.findFragmentById(binding.navHostFragment.id)
            !!.findNavController()
        )

        setContentView(binding.root)
    }

    private fun setTimeOfData(
        binding: ActivityMainBinding,
        timestamp: Long
    ) {
        val dateFormatDayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateFormatDayAndMonth = SimpleDateFormat("dd MMMM", Locale.getDefault())
        val dateFormatTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        dateFormatDayOfWeek.timeZone = TimeZone.getTimeZone("UTC")
        dateFormatDayAndMonth.timeZone = TimeZone.getTimeZone("UTC")
        dateFormatTime.timeZone = TimeZone.getTimeZone("UTC")

        val date = Date(timestamp * 1000)

        val dayOfWeekText = dateFormatDayOfWeek.format(date)
        val dayAndMonthText = dateFormatDayAndMonth.format(date)
        val timeText = dateFormatTime.format(date)

        binding.mainInfo.apply {
            dayOfWeek.text = dayOfWeekText.replaceFirstChar { it.uppercase() }
            dayAndMonth.text = dayAndMonthText
            time.text = timeText
        }
    }
}