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
        }

        binding.bottomNavigationView.setupWithNavController(
            supportFragmentManager.findFragmentById(binding.navHostFragment.id)
            !!.findNavController()
        )

        setContentView(binding.root)
    }
}