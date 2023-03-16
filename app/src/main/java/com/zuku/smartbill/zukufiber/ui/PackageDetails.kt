package com.zuku.smartbill.zukufiber.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.data.services.launchSTK
import kotlinx.android.synthetic.main.activity_package_details.*
import kotlinx.android.synthetic.main.message_box.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

class PackageDetails : AppCompatActivity() {

    var cal = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)


        tvTitle.text =  intent.getStringExtra("packageName").toString()
        tvSPeed.text =   intent.getStringExtra("item").toString()
        tvAmount.text =  intent.getStringExtra("currency").toString()+" "+ intent.getStringExtra("price").toString()
        tvDes.text =   intent.getStringExtra("des").toString()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        tv_close.setOnClickListener { finish() }
        image_close.setOnClickListener { finish() }
        tv_date.setOnClickListener {
            DatePickerDialog(this@PackageDetails,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()


        }

        tvPackage.setOnClickListener {
            changePlanRequest()
        }
        tvDes.setOnClickListener { showMessageBox() }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMessageBox(){

        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.message_box, null)
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()

        messageBoxView.tvHeader.text ="${intent.getStringExtra("item")} Channels"

        lifecycleScope.launch(Dispatchers.IO){

            val result =  api.getChannels(
                "getChannels", intent.getStringExtra("item").toString())
            if(result.success){
                runOnUiThread {
                    progress_circular2.visibility = View.GONE
                    // use array-adapter and define an array
                    val arrayAdapter: ArrayAdapter<*>
                    val array = arrayListOf<String>()
                    for (data in result.data.channels){
                        array.add("${data.channel_code} ${data.channel_name}")
                    }
                    // access the listView from xml file
                    arrayAdapter = ArrayAdapter(this@PackageDetails , R.layout.list_item_view, array)
                    messageBoxView.list_view.adapter = arrayAdapter
                    messageBoxView.list_view.adapter = arrayAdapter
                    messageBoxView.list_view.divider = null;
                }

            }else{
                runOnUiThread {  progress_circular2.visibility = View.GONE
                    Toast.makeText(this@PackageDetails,result.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        //setting text values
        messageBoxView.imageView.setOnClickListener { messageBoxInstance.dismiss()}

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateInView() {
        //25 Nov 2022
        val myFormat = "dd MMMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_date!!.text =  sdf.format(cal.time)
    }

    private fun changePlanRequest(){
        progress_circular2.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){

           val result =  api.changePlanRequest(
               "changePlanRequest",
               tvTitle.text.toString(),
               tv_date.text.toString(),
               tvSPeed.text.toString()+" "+ tvDes.text.toString(),
               tvAmount.text.toString(),
               getValue(this@PackageDetails,"subid").toString(),
               getValue(this@PackageDetails,"subdb").toString(),
               getValue(this@PackageDetails,"subName").toString(),
               getValue(this@PackageDetails,"phoneNumber").toString(),
               getValue(this@PackageDetails,"currentPackage").toString()
           )
            if(result.success){
                runOnUiThread {
                    progress_circular2.visibility = View.GONE
                    Toast.makeText(this@PackageDetails,result.message,Toast.LENGTH_LONG).show()
                    finish()
                }

            }else{
                runOnUiThread {  progress_circular2.visibility = View.GONE
                    Toast.makeText(this@PackageDetails,result.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}