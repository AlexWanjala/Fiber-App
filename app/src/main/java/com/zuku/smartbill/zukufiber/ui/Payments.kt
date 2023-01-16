package com.zuku.smartbill.zukufiber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.ui.adapter.PaymentAdapter
import kotlinx.android.synthetic.main.activity_payments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Payments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)



        image_close.setOnClickListener { finish() }

        paymentsoptions()
    }

    fun paymentsoptions(){
        lifecycleScope.launch(Dispatchers.IO){

            val result = api.paymentsoptions("paymentsoptions", getValue(this@Payments,"subdb").toString())

            if(result.success){

             runOnUiThread {
                 val adapter = PaymentAdapter(this@Payments,result.data.paymentData)
                 recycler_view.adapter = adapter
                 recycler_view.layoutManager = LinearLayoutManager(this@Payments) }

            }else{

            }
        }

    }

}