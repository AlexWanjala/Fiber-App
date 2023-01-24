package com.zuku.smartbill.zukufiber.ui

import PackageItems
import Packages
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.*
import com.zuku.smartbill.zukufiber.ui.adapter.PackageAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PaymentMethodsAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.bottom_sheet_plans.*
import kotlinx.android.synthetic.main.bottom_sheet_plans.recycler_view2
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.radio_group.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


//ALex boey


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private var accounts: MutableList<String> = ArrayList()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var accNo: String = ""
    private var subdb: String =""
    lateinit var adapter : PackageAdapter




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    bottomSheet.animate()
                        .translationY(0f)
                }
                if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(20f)
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(20f)
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(-20f)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        tv_change_plan.setOnClickListener {
            startActivity(Intent(this@MainActivity, PackagesActivity::class.java))
        }

        tv_see_all.setOnClickListener { startActivity(Intent(this, PackagesActivity::class.java)) }
        tv_pay.setOnClickListener {


            if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),1)
                Toast.makeText(this,"Please Allow",Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }else{
                toggleBottomSheet()
            }


        }
        image_statement.setOnClickListener {
            startActivity(Intent(this, Transactions::class.java)
            .putExtra("subdb",subdb)
            .putExtra("subid",accNo)
        ) }

        tv_top_up.setOnClickListener {

            if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),1)
                Toast.makeText(this,"Please Allow",Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }else{
                startActivity(Intent(this, PaymentsActivity::class.java))
            }


        }
        chat.setOnClickListener {
            save(this,"address","")
            startActivity(Intent(this, ShiftRequest::class.java))
        }
        profile.setOnClickListener {
           // Toast.makeText(this,"coming soon",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Profile::class.java))
        }
        if(isOnline(this)){
            getSubscriber()
        }else{
            Toast.makeText(this,"You are offline",Toast.LENGTH_LONG).show()
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        try {
          //  startActivityForResult(intent, PERMISSION_REQUEST_CODE)
        } catch (e: Exception) {
         /*   showDialog(
                getString(R.string.permission_error_title),
                getString(R.string.permission_error_text)
            )*/
        }
    }

     fun initRecyclerView(packageItems: List<PackageItems>){
        //Packages
        val adapter = PackagesAdapter(this,packageItems)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun initRecyclerViewRadio(arrayList: List<Packages>){
        //Packages
        runOnUiThread {
            adapter = PackageAdapter(this,arrayList)
            recycler_view_radio.adapter = adapter
            recycler_view_radio.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        }

    }

     fun stkPayments(desc: String){

        startActivity(Intent(this, Amount::class.java)
            .putExtra("amountDue",tvAmountDue.text)
            .putExtra("accNo",accNo)
            .putExtra("speed",  tvSPeed.text)
            .putExtra("desc",  desc))

     }

    private fun requestOverlayPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.SYSTEM_ALERT_WINDOW)

        if (permission != PackageManager.PERMISSION_GRANTED) {
           // Log.i(TAG, "Permission to record denied")
        }
    }



    private fun getpaymentmethods (){
       lifecycleScope.launch(Dispatchers.IO){

           val result = api.getpaymentmethods("getpaymentmethods", getValue(this@MainActivity,"subdb").toString())
           if(result.success){
               runOnUiThread {
                   val adapter = PaymentMethodsAdapter(this@MainActivity,result.data.paymethods)
                   recycler_view2.adapter = adapter
                   recycler_view2.layoutManager = LinearLayoutManager(this@MainActivity)
               }

           }else{
               runOnUiThread { Toast.makeText(this@MainActivity,result.message,Toast.LENGTH_LONG).show()}
           }

       }

    }

    override fun onResume() {
        requestOverlayPermission()
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        getPackageInfo( p0?.getItemAtPosition(p2).toString());
       // Toast.makeText(this, p0?.getItemAtPosition(p2).toString(),Toast.LENGTH_LONG).show()

    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
      //  Toast.makeText(this, p0?.getItemAtPosition(1).toString(),Toast.LENGTH_LONG).show()

    }
    private fun toggleBottomSheet(){
        runOnUiThread {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                getpaymentmethods()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSubscriber(){

        lifecycleScope.launch(Dispatchers.IO) {

            val response = api.getSubscriber("getSubscriber", getValue(this@MainActivity,"phoneNumber").toString())
            runOnUiThread {
                if(response.success){
                    Const.ConstHolder.INSTANCE.setJson4Kotlin_Base(response)

                    for (item in response.data.subDetailsResponse){
                        getPackageInfo(response.data.subDetailsResponse[0].packageinfo.subid.toString())
                        accounts.add(item.packageinfo.subid.toString())

                    }

                    runOnUiThread {
                        val aa = ArrayAdapter(this@MainActivity, R.layout.spinner_right_aligned, accounts)
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        with(mySpinner)
                        {
                            adapter = aa
                            setSelection(0, false)
                            onItemSelectedListener = this@MainActivity
                            prompt = R.string.select_acc.toString()
                            gravity = Gravity.CENTER

                        }
                    }

                }else{

                }
            }
        }



    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPackageInfo(subid :String){
        accNo = subid
        try {
            for (item in Const.ConstHolder.INSTANCE.getJson4Kotlin_Base()?.data!!.subDetailsResponse){
                if(subid==item.packageinfo.subid.toString()){
                    subdb = item.subdetails.subdb
                    save(this,"subdb",subdb)
                    save(this,"subName",item.subdetails.subname)
                    save(this,"subapt",item.subdetails.subapt)
                    save(this,"subid",item.subdetails.subid.toString())
                    updatedate.text = item.packageinfo.updatedate
                    tvName.text = item.subdetails.subname
                    tvBalance.text =  kotlin.math.abs(item.packageinfo.buckamt).toString()
                    tvAmountDue.text = item.packageinfo.dueamt.toString()
                    if(item.packageinfo.billthru == null){
                        tvDays.text = "Due";
                    }else{
                        tvDays.text = dateDiff(item.packageinfo.billthru)
                    }

                    tvUntil.text = "Until "+ item.packageinfo.billthru
                    tvDes.text = item.packageinfo.lastpack
                    save(this,"currentPackage",item.packageinfo.lastpack)
                    var speed = item.packageinfo.lastpack.filter { it.isDigit() }
                    if(speed.length > 3){
                        speed = speed.drop(1);
                    }
                    tvSPeed.text =  speed

                    getPackages(item.packageinfo.subdb)

                }
            }
        }catch (ex: NullPointerException){

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateDiff(finalDate: String): String {
        try {
            if(finalDate==null){
                return "Due";
            }
            else{
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val currentDate = LocalDateTime.now().format(formatter);

                val date1: Date
                val date2: Date
                val dates = SimpleDateFormat("yyyy-MM-dd")
                date1 = dates.parse(currentDate)
                date2 = dates.parse(finalDate)
                val difference: Long = abs(date1.time - date2.time)
                val differenceDates = difference / (24 * 60 * 60 * 1000)
                val dayDifference = differenceDates.toString();
                if(differenceDates>30){
                    return "Due";
                }else{
                    return dayDifference;
                }

            }
        }catch (NullPointerException : NullPointerException){
            return "Due";
        }
    }

    private fun getPackages(subid :String){
        lifecycleScope.launch(Dispatchers.IO){
            val response =  api.getPackages("getPackages",subid)
            if(response.success){
                Const.ConstHolder.INSTANCE.setPackages(response.data.packages)
                initRecyclerViewRadio(response.data.packages)

            }else{
                Toast.makeText(this@MainActivity,response.message,Toast.LENGTH_LONG).show()
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}