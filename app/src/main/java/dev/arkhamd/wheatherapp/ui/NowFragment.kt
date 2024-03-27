package dev.arkhamd.wheatherapp.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.arkhamd.wheatherapp.databinding.FragmentHourlyBinding
import dev.arkhamd.wheatherapp.databinding.FragmentNowBinding
import dev.arkhamd.wheatherapp.ui.viewModel.WeatherViewModel

class NowFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentNowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowBinding.inflate(inflater)

        Log.d(ContentValues.TAG, weatherViewModel.toString())
        weatherViewModel.weatherInfo.observe(this.viewLifecycleOwner) { weather ->
            if (weather.data != null) {

                changeMainWeatherInfo(
                    binding,
                    temperature = weather.data[0].mainInfo.temp.toString(),
                    humidity = weather.data[0].mainInfo.humidity.toString(),
                    windSpeed = weather.data[0].windInfo.speed.toString()
                )

            }
        }

        return binding.root

    }

    private fun changeMainWeatherInfo(
        binding: FragmentNowBinding,
        temperature: String,
        humidity: String,
        windSpeed: String
    ) {
        binding.weatherMainInfo.apply {
            tempInfo.text = temperature
            humidityInfo.text = humidity
            windSpeedInfo.text = windSpeed
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NowFragment()
    }
}