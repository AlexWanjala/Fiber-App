package com.zuku.smartbill.zukufiber.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_location)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getShops()
        getLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val success= mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));

        if (!success) {
            Log.e(ContentValues.TAG, "Style parsing failed.")
        }

    }

    private fun getShops(){
        lifecycleScope.launch(Dispatchers.IO){

            val result =  api.getShops("getShops")
            if(result.success){
                runOnUiThread {

                    for (shop in result.data.shops){


                        val latLng  = LatLng(shop.ping.split(",")[0].toDouble(),shop.ping.split(",")[1].toDouble())


                        val height = 30
                        val width = 30
                        val bitmaps = resources.getDrawable(R.drawable.image_pin) as BitmapDrawable
                        val b = bitmaps.bitmap
                        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

                        // Start marker
                        var options = MarkerOptions()
                        options.position(latLng)
                        options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        options.title("${shop.shop_name} ${shop.address} ${shop.open_hours}").describeContents()
                        mMap.addMarker(options)
                    }

                }

            }else{

            }

        }

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                println("## getDevice location")
                val locationResult = LocationServices.getFusedLocationProviderClient(this)
                locationResult.lastLocation.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {


                            var latLng= LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLng, 9.0f
                                )
                            )

                        }else{
                            println("## lastKnownLocation is null check GPS ")
                        }
                    } else {
                        println("## Current location is null. Using defaults."+ task.exception)
                    }
                }
            }else{
                println("## locationPermissionNotGranted")
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    private fun getLocationPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
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

}