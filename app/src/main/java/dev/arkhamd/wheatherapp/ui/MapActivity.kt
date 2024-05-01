package dev.arkhamd.wheatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.arkhamd.wheatherapp.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}