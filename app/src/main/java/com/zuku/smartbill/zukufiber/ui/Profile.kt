package com.zuku.smartbill.zukufiber.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.zuku.smartbill.zukufiber.data.services.API
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.landing.Browser
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_shifting_map.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Field

class Profile : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        image_close6.setOnClickListener { finish() }

        getInfo()
        edTaxPin.text.append(getValue(this,"taxpin"))
        edPhoneNumber.text.append(getValue(this,"cellcont"))
        edEmail.text.append(getValue(this,"emcont"))


        tvUpdatePlan.setOnClickListener { updateProfile() }
        logout.setOnClickListener {
            logout()
        }

        getLocationPermission()
        getShops()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val success= mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));

        if (!success) {
            Log.e(ContentValues.TAG, "Style parsing failed.")
        }

    }

    private fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Would you like to logout? or Just to exit?")


        builder.setPositiveButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.setNegativeButton("Logout") { dialog, which ->
            startActivity(Intent(this,Login ::class.java))
            save(this,"login","false")
            finishAffinity()
        }

        builder.setNeutralButton("Exit") { dialog, which ->
            finishAffinity()
        }
        builder.show()
    }
    private fun Activity.openWebPage(url: String?) = url?.let {

        startActivity(Intent(this@Profile,Browser::class.java).putExtra("link",url))

      /*  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)*/
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
    private fun getInfo(){
      lifecycleScope.launch(Dispatchers.IO){

          val result =  api.getInfo("getInfo", getValue(this@Profile,"subdb").toString())

          if(result.success){
              runOnUiThread {
                  whatsapp.setOnClickListener {
                      val url = "https://api.whatsapp.com/send?phone="+result.data.appinfo.whatsapp
                      val i = Intent(Intent.ACTION_VIEW)
                      i.data = Uri.parse(url)
                      startActivity(i)
                  }
                  call.setOnClickListener {
                      val dialIntent = Intent(Intent.ACTION_DIAL)
                      dialIntent.data = Uri.parse("tel:" + result.data.appinfo.phone)
                      startActivity(dialIntent)
                  }
                  info.setOnClickListener {

                      openWebPage(result.data.appinfo.info)

                  }
                  faq.setOnClickListener {
                   //   Toast.makeText(this@Profile,"hhss",Toast.LENGTH_LONG).show()
                      openWebPage(result.data.appinfo.faq)
                  }
                  facebook.setOnClickListener {

                      openWebPage(result.data.appinfo.facebook)

                  }
                  twitter.setOnClickListener {

                      openWebPage(result.data.appinfo.twitter)

                  }
              }

          }else{
              runOnUiThread {
                  whatsapp.visibility = View.GONE
                  call.visibility = View.GONE
                  info.visibility = View.GONE
                  faq.visibility = View.GONE
              }
          }

      }

  }
    private fun updateProfile(){
        progress_circular2.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){

            val result = api.updateProfile(
                "updateProfile",
                getValue(this@Profile,"subid").toString(),
                getValue(this@Profile,"subdb").toString(),
                edTaxPin.text.toString(),
                edEmailAdd.text.toString(),
                edPhoneNumbeAddr.text.toString(),
                getValue(this@Profile,"cid").toString())

            runOnUiThread {   progress_circular2.visibility = View.GONE
            Toast.makeText(this@Profile,result.message,Toast.LENGTH_LONG).show()
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