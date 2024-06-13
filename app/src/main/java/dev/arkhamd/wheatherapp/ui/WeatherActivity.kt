package dev.arkhamd.wheatherapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.ActivityWeatherBinding
import dev.arkhamd.wheatherapp.ui.weather.notification.WeatherWorker
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherResult
import dev.arkhamd.wheatherapp.ui.weather.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)

        val cords = loadCords()
        saveCords(cords[0], cords[1])

        weatherViewModel.update(latitude =  cords[0], longitude =  cords[1])

        setPostNotificationPermission { scheduleDailyNotification(applicationContext) }

        weatherViewModel.weatherInfo.observe(this) { weather ->
            if (weather.data != null) {
                if (weather is WeatherResult.Api) {
                    binding.weatherState.setImageResource(R.drawable.weather_real)
                    Toast.makeText(this, getString(R.string.successfully), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.error_get_weather), Toast.LENGTH_SHORT).show()
                }

                binding.cityText.text = weather.data.cityInfo.name.replace(' ', '\n')
                setTimeOfData(binding, weather.data.hourWeatherInfo[0].time)

                binding.swipeRefresh.isRefreshing = false
                binding.mainInfo.shimmerLayout.hideShimmer()
            } else {
                binding.mainInfo.shimmerLayout.startShimmer()
            }

        }

        binding.cityLayout.setOnClickListener {
            val intent = Intent(baseContext, MapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.swipeRefresh.setOnRefreshListener {
            weatherViewModel.update(latitude = cords[0], longitude = cords[1])
        }

        binding.bottomNavigationView.setupWithNavController(
            supportFragmentManager.findFragmentById(binding.navHostFragment.id)
            !!.findNavController()
        )

        setContentView(binding.root)
    }

    private fun setTimeOfData(
        binding: ActivityWeatherBinding,
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

    private fun saveCords(latitude: Float, longitude: Float) {
        val sharedPreferences =
            baseContext.getSharedPreferences("cords_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putFloat("lat", latitude)
        editor.putFloat("lon", longitude)

        editor.apply()
    }

    private fun loadCords(): FloatArray {
        val sharedPreferences =
            baseContext.getSharedPreferences("cords_prefs", Context.MODE_PRIVATE)

        return intent.getFloatArrayExtra("cords")
            ?: floatArrayOf(
                sharedPreferences.getFloat("lat", -1f),
                sharedPreferences.getFloat("lon", -1f)
            ).takeIf { it[0] != -1f }
            ?: floatArrayOf(55.065304f, 60.108337f).also {
                Toast.makeText(baseContext,
                    getString(R.string.error_get_geo),
                    Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun setPostNotificationPermission(
        onSuccess: () -> Unit
    ) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) onSuccess()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> onSuccess()
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            onSuccess()
        }
    }

    private fun scheduleDailyNotification(context: Context) {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val initialDelay = if (targetTime.before(currentTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1)
            targetTime.timeInMillis - currentTime.timeInMillis
        } else {
            targetTime.timeInMillis - currentTime.timeInMillis
        }

        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WeatherWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}

