package com.zuku.smartbill.zukufiber.ui

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_package_details.*
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateInView() {
        //25 Nov 2022
        val myFormat = "dd MMMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_date!!.text =  sdf.format(cal.time)
    }
}