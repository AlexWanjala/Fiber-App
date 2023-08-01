package com.zuku.smartbill.zukufiber.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_package_details.*
import kotlinx.android.synthetic.main.activity_shift_request.*
import kotlinx.android.synthetic.main.activity_shift_request.image_close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_shift_request.layoutMain as layoutMain1
import java.text.SimpleDateFormat
import java.util.*

class ShiftRequest : AppCompatActivity() {
    var cal: Calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_request)

        select_map.setOnClickListener {

            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                checkGPSEnable()
            }else{
                startActivity(Intent(this,ShiftingMap::class.java))
            }

        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        image_close.setOnClickListener { finish() }
        ed_date.setOnClickListener {

            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        tv_submit.setOnClickListener {
            if(ed_date.text.equals("") ||  select_map.text.equals("") || edDes.text.equals("")){
                Toast.makeText(this,"Make sure you select date,Shifting Address and Additional Information",Toast.LENGTH_LONG).show()
            }else{
                shiftRequest()
            }
        }
    }

    private fun checkGPSEnable() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id
                ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun shiftRequest(){
      progress_circular4.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){

           val result =  api.shiftRequest("shiftRequest",
               getValue(this@ShiftRequest,"subName").toString(),
               ed_date.text.toString(),
                getValue(this@ShiftRequest,"address").toString(),
                getValue(this@ShiftRequest,"latLng").toString(),
                edDes.text.toString(),
               getValue(this@ShiftRequest,"subapt").toString(),
               getValue(this@ShiftRequest,"phoneNumber").toString(),
               getValue(this@ShiftRequest,"subid").toString(),
               getValue(this@ShiftRequest,"subdb").toString()
           )
            runOnUiThread {
                progress_circular4.visibility = View.GONE
                Toast.makeText(this@ShiftRequest,result.message,Toast.LENGTH_LONG).show() }

            if(result.success){
                finish()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateInView() {
        //25 Nov 2022
        val myFormat = "dd MMMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        ed_date.setText(sdf.format(cal.time))
    }
    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {//Night Mode
                    layoutMain1.background = resources.getDrawable(R.drawable.background_dark_one)
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light Mode
                layoutMain1.background = resources.getDrawable(R.drawable.background_light_one)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onResume() {
        if (!getValue(this,"address").equals("")){
            select_map.setText(getValue(this,"address").toString())
        }

        super.onResume()
        currentTheme()

    }






}