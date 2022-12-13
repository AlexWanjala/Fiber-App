package com.zuku.smartbill.zukufiber.ui

import PackageItems
import Packages
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.Const
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.ui.adapter.PackageAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.bottom_sheet_plans.*
import kotlinx.android.synthetic.main.radio_group.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//ALex


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

        tv_change_plan.setOnClickListener {   startActivity(Intent(this, PackagesActivity::class.java)) }
        ll_package.setOnClickListener { startActivity(Intent(this, PackagesActivity::class.java)) }
        ll_package2.setOnClickListener { startActivity(Intent(this, PackagesActivity::class.java)) }
        ll_package3.setOnClickListener { startActivity(Intent(this, PackagesActivity::class.java)) }


        tv_see_all.setOnClickListener { startActivity(Intent(this, PackagesActivity::class.java)) }
        tv_pay.setOnClickListener { startActivity(Intent(this, Payments::class.java)) }
        image_statement.setOnClickListener { startActivity(Intent(this, Transactions::class.java)
            .putExtra("subdb",subdb)
            .putExtra("subid",accNo)
        ) }
        tv_top_up.setOnClickListener { startActivity(Intent(this, Amount::class.java)
            .putExtra("amountDue",tvAmountDue.text)
            .putExtra("accNo",accNo)
            .putExtra("speed",  tvSPeed.text)
        ) }
        chat.setOnClickListener { startActivity(Intent(this, ShiftRequest::class.java)) }
        profile.setOnClickListener { startActivity(Intent(this, Profile::class.java)) }

        getSubscriber()


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
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSubscriber(){

        lifecycleScope.launch(Dispatchers.IO) {

            val response = api.getSubscriber("getSubscriber","0722387708")
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
}