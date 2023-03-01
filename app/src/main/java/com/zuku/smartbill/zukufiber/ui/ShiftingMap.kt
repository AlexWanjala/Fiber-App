package com.zuku.smartbill.zukufiber.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.zuku.smartbill.zukufiber.R
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.GeoPoint
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.adapter.PlacesResultAdapter
import kotlinx.android.synthetic.main.activity_shifting_map.*
import java.io.IOException
import java.util.*


class ShiftingMap : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var locationPermissionGranted = false
    lateinit var start: LatLng
    lateinit var end: LatLng
    private var zoom =16.0f

    var markerOptions = MarkerOptions()

    private lateinit var locationManager: LocationManager
    var gpsStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifting_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tv_submit.setOnClickListener { finish() }

        getLocationPermission()
        getDeviceLocation()
        init()

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val success= mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));

        if (!success) {
            Log.e(ContentValues.TAG, "Style parsing failed.")
        }

        mMap.setOnCameraIdleListener {
            val coords = mMap.cameraPosition.target
            //Toast.makeText(this,"djjd",Toast.LENGTH_LONG).show()
            getAddress()
            tv_submit.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
        }
        mMap.setOnCameraMoveListener {
            tv_submit.visibility = View.GONE
            toolbar.visibility = View.GONE
        }




    }

    private fun getAddress(){
        val lat = mMap.cameraPosition.target.latitude
        val lng = mMap.cameraPosition.target.longitude
        // Initializing Geocoder
        val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
        var addressString= ""

        // Reverse-Geocoding starts
        try {
            val addressList: List<Address> = mGeocoder.getFromLocation(lat,lng, 1)!!

            // use your lat, long value here
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("\n")
                }

                // Various Parameters of an Address are appended
                // to generate a complete Address
                if (address.premises != null)
                    sb.append(address.premises).append(", ")

                sb.append(address.subAdminArea).append("\n")
                sb.append(address.locality).append(", ")
                sb.append(address.adminArea).append(", ")
                sb.append(address.countryName).append(", ")
                sb.append(address.featureName)

                // StringBuilder sb is converted into a string
                // and this value is assigned to the
                // initially declared addressString string.
                addressString = sb.toString()
                runOnUiThread {  tvAddress.text = addressString.replace("null","") }
                save(this,"address",tvAddress.text.toString())
                save(this,"latLng","${lat}, $lng")
                Log.d("###",tvAddress.text.toString())
            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
        }

    }
    private fun getLocationPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
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
                            println("## lastKnownLocation is null check GPS ")
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
    private fun checkGpsStatus() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsStatus) {
            Toast.makeText(this,"Enable GPS location",Toast.LENGTH_LONG).show()
            gpsStatus()
        }else{
            getLocationPermission()
        }
    }
    private fun gpsStatus() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
    override fun onResume() {
        super.onResume()
    }
    //Search Places
    private lateinit var mPlacesClient: PlacesClient
    private lateinit var placeAdapter: PlacesResultAdapter
    private fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress!!, 5000)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.getLatitude()
            location.getLongitude()
            p1 = GeoPoint(
                (location.getLatitude()) as Double,
                (location.getLongitude()) as Double
            )
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    private fun init() {
        val apiKey = "AIzaSyAC5kUIdmqqXtarw83GU-DmNO_ykpfPkNA"
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }

        val placesClient = Places.createClient(this)
        placeAdapter = PlacesResultAdapter(this) { prediction ->

            try {
                et_search_bar.setText(prediction.getFullText(null).toString())
                val startAddress =  et_search_bar.text.toString().replace("[^a-zA-Z0-9]","")
                val latLong = getLocationFromAddress(startAddress)

                try {
                    start = LatLng(latLong!!.latitude, latLong.longitude)
                    updateMap(LatLng(start.latitude, start.longitude))
                    et_search_bar.text.clear()

                }catch (ex: NullPointerException){

                }
            }catch (ex: IOException){
                runOnUiThread { Toast.makeText(this,"Kindly restart the app",Toast.LENGTH_LONG).show() }
            }

        }
        rv_place_results.layoutManager = LinearLayoutManager(this)
        rv_place_results.adapter = placeAdapter

        et_search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {

                if (text.toString().isNotEmpty()) {
                    placeAdapter.filter.filter(text.toString())
                    if (rv_place_results.isGone) {
                        rv_place_results.visibility = View.VISIBLE
                    }
                } else {
                    if (rv_place_results.isVisible) {
                        rv_place_results.visibility = View.GONE
                    }
                }
            }
        })

    }
    fun round(x: Int): Int {

        if (x % 10 === 0) x
        else (x / 10 + 1) * 10

        return x
    }


}