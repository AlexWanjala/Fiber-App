package com.zuku.smartbill.zukufiber.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.statusBarTransparent
import kotlinx.android.synthetic.main.activity_browser.*

class Browser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        statusBarTransparent(this)
        webView.loadUrl(intent.getStringExtra("link").toString())
        webView.getSettings().javaScriptEnabled = true;
        webView.settings.javaScriptCanOpenWindowsAutomatically = true;
    }


}