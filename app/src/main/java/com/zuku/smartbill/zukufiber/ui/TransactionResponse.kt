package com.zuku.smartbill.zukufiber.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.rajat.pdfviewer.PdfViewerActivity
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

            startActivity(

                PdfViewerActivity.launchPdfFromUrl( //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    this,
                    "http://62.8.88.236/${intent.getStringExtra("id").toString()}/print",                                // PDF URL in String format
                    intent.getStringExtra("id").toString(),                        // PDF Name/Title in String format
                    "",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = false                    // This param is true by defualt.
                )
            )


        }

        tvPackage.text = intent.getStringExtra("customer_name")
        tvAccNo.text = intent.getStringExtra("accNo").toString()
        tvAmount.text = intent.getStringExtra("amount").toString()
        paymentCode.text = intent.getStringExtra("customer_email").toString()
        tvSPeed.text =  intent.getStringExtra("speed").toString()

    }

}



