package com.zuku.smartbill.zukufiber.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.bottom_sheet_plans.*
import kotlinx.android.synthetic.main.radio_group.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private var accounts = arrayOf("12345678", "9876654", "9987272")
    private val arrayList: Array<String> = arrayOf("10:2,899","20:4,399","60:6,299","100:8,299","200:10,299","500:15,299")
    private val arrayList2: Array<String> = arrayOf("20:4,899","30:5,399","70:7,299","200:10,299","400:12,299","600:16,299")
    private val arrayList3: Array<String> = arrayOf("30:5,899","40:6,399","808,299","300:11,299","500:12,299","700:7,299")
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val aa = ArrayAdapter(this, R.layout.spinner_right_aligned, accounts)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(mySpinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@MainActivity
            prompt = R.string.select_acc.toString()
            gravity = Gravity.CENTER

        }
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

        tv_change_plan.setOnClickListener {   toggleBottomSheet() }
        ll_package.setOnClickListener { startActivity(Intent(this, Packages::class.java)) }
        ll_package2.setOnClickListener { startActivity(Intent(this, Packages::class.java)) }
        ll_package3.setOnClickListener { startActivity(Intent(this, Packages::class.java)) }
        radio_1.isChecked = true

        tv_see_all.setOnClickListener { startActivity(Intent(this, Packages::class.java)) }
        tv_pay.setOnClickListener { startActivity(Intent(this, Payments::class.java)) }
        image_statement.setOnClickListener { startActivity(Intent(this, Transactions::class.java)) }
        tv_top_up.setOnClickListener { startActivity(Intent(this, Amount::class.java)) }
        chat.setOnClickListener { startActivity(Intent(this, ShiftRequest::class.java)) }
        profile.setOnClickListener { startActivity(Intent(this, Profile::class.java)) }


        initRecyclerView(arrayList)
        radio_group.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.radio_1 -> { initRecyclerView(arrayList)
                    }
                    R.id.radio_2 -> {
                        initRecyclerView(arrayList2)
                    } R.id.radio_3 -> {
                        initRecyclerView(arrayList3)
                    }
                }
            }
        }

    }

    private fun initRecyclerView(arrayList:Array<String> ){
        //Packages
        val adapter = PackagesAdapter(this,arrayList)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
      //  Toast.makeText(this, p0?.getItemAtPosition(p2).toString(),Toast.LENGTH_LONG).show()

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
}