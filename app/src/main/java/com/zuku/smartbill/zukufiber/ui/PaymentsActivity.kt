package com.zuku.smartbill.zukufiber.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.ui.adapter.PaymentAdapter
import kotlinx.android.synthetic.main.activity_payments.*
import kotlinx.android.synthetic.main.activity_payments.layoutMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PaymentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)
        image_close.setOnClickListener { finish() }

        paymentsoptions()
    }

    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {//Night Mode
                    layoutMain.background = resources.getDrawable(R.drawable.background_dark_one)
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light Mode
                layoutMain.background = resources.getDrawable(R.drawable.background_light_one)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentTheme()

    }

    private fun paymentsoptions(){
        lifecycleScope.launch(Dispatchers.IO){
            val result = api.paymentsoptions("paymentsoptions",
                getValue(this@PaymentsActivity,"subdb").toString().replace(" ",""),"version"
            )
            if(result.success){
             runOnUiThread {
                 val adapter = PaymentAdapter(this@PaymentsActivity,result.data.paymentData)
                 recycler_view.adapter = adapter
                 recycler_view.layoutManager = LinearLayoutManager(this@PaymentsActivity)
             }
            }else{
                runCatching { Toast.makeText(this@PaymentsActivity,result.message,Toast.LENGTH_LONG) }
            }
        }

    }




}