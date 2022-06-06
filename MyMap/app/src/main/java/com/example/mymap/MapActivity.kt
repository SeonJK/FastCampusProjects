package com.example.mymap

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymap.databinding.ActivityMapBinding
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_CODE = 101
    }

    private lateinit var binding: ActivityMapBinding

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions(requiredPermission, REQUEST_CODE)

        val mapView = MapView(this)
        binding.mapView.addView(mapView)

//        val model = intent.getParcelableExtra<LocationLatLng>("LatLng")
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(model.latitude, model.longitude), true)
    }
}