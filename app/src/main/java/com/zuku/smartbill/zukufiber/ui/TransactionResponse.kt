package com.zuku.smartbill.zukufiber.ui

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.rajat.pdfviewer.PdfViewerActivity
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_transaction_reponse.*
import kotlinx.android.synthetic.main.activity_transaction_reponse.layoutMain
import kotlinx.android.synthetic.main.activity_transaction_reponse.tvSPeed

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
        val email = intent.getStringExtra("customer_email").toString()
        if (email.isEmpty() || email==null || email=="null"){
            paymentCode.text ="#"
        }else{
            paymentCode.text = email
        }

        tvSPeed.text =  intent.getStringExtra("speed").toString()

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



}



