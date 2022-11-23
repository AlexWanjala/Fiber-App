package com.zuku.smartbill.zukufiber.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_login2.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        register.setOnClickListener { startActivity(Intent(this,com.zuku.smartbill.zukufiber.ui.landing.Login::class.java)) }
        login.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
    }
}