package com.zuku.smartbill.zukufiber.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zuku.smartbill.zukufiber.R


class ShiftingMap : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var locationPermissionGranted = false
    lateinit var start: LatLng
    lateinit var end: LatLng
    private var zoom =16.0f

    var markerOptions = MarkerOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifting_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraIdleListener { OnCameraIdleListener {
            val centerLatLang = mMap.projection.visibleRegion.latLngBounds.center

            Log.d("##", "lat{$centerLatLang.longitude} long{$centerLatLang.longitude}")
        } }

    }
    private fun getLocationPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                println("## getDevice location")
                val locationResult = LocationServices.getFusedLocationProviderClient(this)
                locationResult.lastLocation.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        println("## Current location is okay ")
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            println("## lastKnownLocation lat{$lastKnownLocation.longitude} long{$lastKnownLocation.longitude}")
                            updateMap(LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude))
                        }else{
                            println("## lastKnownLocation null ")
                        }
                    } else {
                        println("## Current location is null. Using defaults."+ task.exception)
                        mMap.moveCamera(CameraUpdateFactory
                             .newLatLngZoom(LatLng(
                                 -1.3071984948612911, 36.82471844447788
                             ), zoom))
                         mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }else{
                println("## locationPermissionNotGranted")
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            requestCode -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    println("##locationPermissionRequested has been granted")
                    getDeviceLocation()

                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }
    private fun updateMap(latLng : LatLng){
        start = latLng
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, zoom
            )
        )
    }
    private fun placeMarkerOnMap(location: LatLng) {

        val height = 31
        val width = 19
        val bitmaps = resources.getDrawable(R.drawable.arrow_down_black) as BitmapDrawable
        val b = bitmaps.bitmap
        //   val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

        markerOptions = MarkerOptions().position(location)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b))
        mMap.addMarker(markerOptions)


    /*    try {
            val address = getAddress(location)
            tvAddress.text =  address
        }catch (ex: IOException){
            tvAddress.text = ""
        }
*/

        // updateLocationUI()

    }

}