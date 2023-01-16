package com.zuku.smartbill.zukufiber.ui

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import kotlinx.android.synthetic.main.activity_package_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*

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