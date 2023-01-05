package com.zuku.smartbill.zukufiber.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.getValue
import kotlinx.android.synthetic.main.activity_login2.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        register.setOnClickListener { startActivity(Intent(this,com.zuku.smartbill.zukufiber.ui.landing.Login::class.java)) }
        login.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }

        whatsapp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=254777320323"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        call.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "0205222222")
            startActivity(dialIntent)
        }
        info.setOnClickListener { openWebPage("https://zuku.co.ke") }
        faq.setOnClickListener { openWebPage("https://zuku.co.ke/faq") }
    }

    fun Activity.openWebPage(url: String?) = url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    override fun onResume() {
        if(getValue(this,"login").equals("true")){
            register.visibility = View.GONE

        }else{
            startActivity(Intent(this,com.zuku.smartbill.zukufiber.ui.landing.Login::class.java))
            finish()
        }
        super.onResume()
    }
}