package com.zuku.smartbill.zukufiber.ui.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_get_otp.setOnClickListener { startActivity(Intent(this,OTP::class.java)) }
    }
}