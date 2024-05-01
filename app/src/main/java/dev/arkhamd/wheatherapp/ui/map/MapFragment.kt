package dev.arkhamd.wheatherapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.Map
import dev.arkhamd.wheatherapp.databinding.FragmentMapBinding
import dev.arkhamd.wheatherapp.ui.map.dialog.MapOnSelectSpotDialog

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: Map

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

        map = binding.yandexMap.mapWindow.map
        map.move(
            START_POSITION, START_ANIMATION, null
        )
        map.addTapListener(geoObjectTapListener)

        return binding.root
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

    /*private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity().parent,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestCode
            )
            false
        } else {
            true
        }
    }*/

    companion object {
        private val START_ANIMATION = Animation(Animation.Type.LINEAR, 1f)
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)

        private val START_POSITION = CameraPosition(
            Point(59.938732, 30.316229),
            8f,
            0f,
            30f
        )

        fun newInstance() = MapFragment()
    }

}