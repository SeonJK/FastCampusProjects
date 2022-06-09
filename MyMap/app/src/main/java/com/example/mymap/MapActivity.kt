package com.example.mymap

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymap.databinding.ActivityMapBinding
import com.example.mymap.model.Place
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity(), MapView.CurrentLocationEventListener {
    val TAG: String = "로그"

    companion object {
        private const val REQUEST_CODE = 101
        private const val GPS_REQUEST_CODE = 103
    }

    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var itemMapPoint: MapPoint
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // activityReusltLauncher 초기화
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // 정상적인 결과코드를 반환하지 않았을 경우 예외처리
                if (result.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "GPS 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 정상적으로 GPS 권한이 허용되었을 경우
                    if (checkLocationServiceStatus()) {
                        Log.d(TAG, "MapActivity - GPS 활성화되었음")
                        checkPermission()
                    }
                }
            }

        checkPermission()

        initFloatingActionButton()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용되었을 때
                    initMapView()
                } else {
                    // 권한이 거부되었을 때
                    Toast.makeText(this, "권한이 허용되지 않았습니다. 위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // 비정상적인 requestCode가 들어왔을 때 동작
            }
        }
    }

    private fun checkLocationServiceStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission() {
        // 권한 요청
        when {
            // 권한이 허용되어 있을 때
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            -> {
                initMapView()
            }

            // 권한이 허용되지 않았을 때 교육용 팝업을 띄운 후 권한 팝업을 띄움
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                openPermissionPopup() // 교육용 팝업 기능 함수
            }

            // 권한 요청
            else -> {
                requestPermissions(requiredPermission, REQUEST_CODE)
            }
        }
    }

    private fun initMapView() {
        // 지도 레이아웃 생성
        mapView = MapView(this)
        binding.mapView.addView(mapView)

        // 인텐트로 넘어온 place데이터 변수 저장
        val model = intent.getParcelableExtra<Place>("place")
        itemMapPoint = MapPoint.mapPointWithGeoCoord(model!!.longitude, model.latitude)
        // 지도의 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(itemMapPoint, 2, true)

        // 지도에 마커 찍기
        val marker = MapPOIItem().apply {
            itemName = model.buildingName
            tag = 0
            mapPoint = itemMapPoint
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.BluePin
        }
        mapView.addPOIItem(marker)
    }

    private fun initFloatingActionButton() {
        binding.currenLocationFAB.setOnClickListener {
            mapView.setCurrentLocationEventListener(this)
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            mapView.setShowCurrentLocationMarker(true)
        }
    }

    private fun openPermissionPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한 알림")
            .setMessage("위치를 받아오기 위해 권한이 필요합니다.")
            .setPositiveButton("허용") { _,_ ->
                val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activityResultLauncher.launch(callGPSSettingIntent)
            }
            .setNegativeButton("거부") { dialog,_ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    override fun onCurrentLocationUpdate(p0: MapView?, mapPoint: MapPoint?, accuracyInMeters: Float) {
        val mapPointGeo = mapPoint?.mapPointGeoCoord
        Log.d(TAG, String.format("MapActivity - onCurrentLocationUpdate() called :: (%f, %f)",
            mapPointGeo?.latitude, mapPointGeo?.longitude))
        itemMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo!!.longitude, mapPointGeo.latitude)
        mapView.setMapCenterPoint(itemMapPoint, true)
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
        TODO("Not yet implemented")
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
        TODO("Not yet implemented")
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
        TODO("Not yet implemented")
    }
}