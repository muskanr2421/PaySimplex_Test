package com.example.paysimplextest.presentation.main.map

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.media.audiofx.PresetReverb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.widget.Toast
import com.example.paysimplextest.R
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

class MapActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationEngine: LocationEngine
    private lateinit var callback: LocationChangeListeningCallback

    private lateinit var locationManager: LocationManager
    var gpsStatus = false

    lateinit var mapView:MapView

    val ACCESS_TOKEN = "pk.eyJ1IjoibXVza2FuciIsImEiOiJjbDk0MXJxNHgwNGdmM3BuMzV3aDJ3dW52In0.QbQ00p_Ki8-3ijsuQScxAA"

    val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.hide()

        Mapbox.getInstance(this, ACCESS_TOKEN)

        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        callback = LocationChangeListeningCallback()
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                .useDefaultLocationEngine(false)
                .build()
            map.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true // Enable to make component visible
                cameraMode = CameraMode.TRACKING                        // Set the component's camera mode
                renderMode = RenderMode.COMPASS                         // Set the component's render mode
            }
            if (gpsStatus){
                initLocationEngine()
            }else{
                Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show()
                finish()
                startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }


    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        locationEngine.requestLocationUpdates(request, callback,mainLooper)
        locationEngine.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Permission not granted!!", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle {
                enableLocationComponent(it)
            }
        } else {
            Toast.makeText(this, "Permission not granted!! app will be EXIT", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                this.finish()
            }, 3000)
        }
    }

    private inner class LocationChangeListeningCallback :
        LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?) {
            result?.lastLocation ?: return
            if (result.lastLocation != null){
                val lat = result.lastLocation?.latitude!!
                val lng = result.lastLocation?.longitude!!
                val latLng = LatLng(lat, lng)

                if (result.lastLocation != null) {
                    map.locationComponent.forceLocationUpdate(result.lastLocation)
                    val position = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15.0) //disable this for not follow zoom
                        .tilt(10.0)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
//                    Toast.makeText(requireContext(), "Location update : $latLng", Toast.LENGTH_SHORT).show()
                }
            }

        }

        override fun onFailure(exception: Exception) {}
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if (gpsStatus){
            Toast.makeText(this, "Fetching the current location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
//        // Prevent leaks
//        locationEngine.removeLocationUpdates(callback)
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}