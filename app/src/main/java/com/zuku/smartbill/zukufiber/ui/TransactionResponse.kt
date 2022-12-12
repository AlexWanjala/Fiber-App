package com.zuku.smartbill.zukufiber.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.Const

import kotlinx.android.synthetic.main.activity_transaction_reponse.*

class TransactionResponse : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_reponse)
        close.setOnClickListener { finish() }
        btn_download.setOnClickListener {
            startActivity(Intent(this,PDFViewer::class.java))

        }

        tvAccNo.text = intent.getStringExtra("accNo").toString()
        tvAmount.text = intent.getStringExtra("amount").toString()
        paymentCode.text = intent.getStringExtra("paymentCode").toString()
        tvSPeed.text =  intent.getStringExtra("speed").toString()

    }

}



