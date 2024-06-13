package dev.arkhamd.wheatherapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.Map
import dev.arkhamd.wheatherapp.R
import dev.arkhamd.wheatherapp.databinding.FragmentMapBinding
import dev.arkhamd.wheatherapp.ui.map.dialog.MapOnSelectSpotDialog


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: Map
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val geoObjectTapListener = GeoObjectTapListener {
        val point = it.geoObject.geometry.firstOrNull()?.point ?: return@GeoObjectTapListener true
        map.cameraPosition.run {
            val position = CameraPosition(point, zoom, azimuth, tilt)
            map.move(position, SMOOTH_ANIMATION, null)
        }

        val selectionMetadata =
            it.geoObject.metadataContainer.getItem(GeoObjectSelectionMetadata::class.java)
        map.selectGeoObject(selectionMetadata)

        val latitude = it.geoObject.geometry[0].point!!.latitude.toFloat()
        val longitude = it.geoObject.geometry[0].point!!.longitude.toFloat()
        val cords = floatArrayOf(latitude, longitude)
        MapOnSelectSpotDialog.newInstance(cords).show(parentFragmentManager, "dialog")

        true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        MapKitFactory.initialize(this.context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        setCoarseLocationPermission()

        map = binding.yandexMap.mapWindow.map
        map.move(START_POSITION, START_ANIMATION, null)
        map.addTapListener(geoObjectTapListener)

        return binding.root
    }

    private fun moveMap(lat: Double, lon: Double, map: Map) {
        val position = CameraPosition(
            Point(lat, lon),
            13f,
            0f,
            30f
        )
        map.move(position, START_ANIMATION, null)
    }

    private fun setCoarseLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->
                            if (location != null) moveMap(location.latitude, location.longitude, map)
                        }
                        .addOnFailureListener { _ ->
                            Toast.makeText(this.context,
                                getString(R.string.location_error), Toast.LENGTH_SHORT).show()
                        }
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) moveMap(location.latitude, location.longitude, map)
                    }
                    .addOnFailureListener { _ ->
                        Toast.makeText(this.context,
                            getString(R.string.location_error), Toast.LENGTH_SHORT).show()
                    }
            }
            else -> {
                // ask for permission
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.yandexMap.onStart()
    }

    override fun onStop() {
        binding.yandexMap.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    companion object {
        private val START_ANIMATION = Animation(Animation.Type.LINEAR, 1f)
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)

        private val START_POSITION = CameraPosition(
            Point(55.065304, 60.108337),
            14f,
            0f,
            30f
        )

        fun newInstance() = MapFragment()
    }

}