package com.zuku.smartbill.zukufiber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_pdfviewer.*

class PDFViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)

        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        val url = "https://api.paysol.co.ke/invoice.pdf"
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }
}