package com.zuku.smartbill.zukufiber.ui.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.MyAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private val BASE_URL = "https://b2d6-41-212-37-138.eu.ngrok.io/apis/fiber-app-api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //  tv_get_otp.setOnClickListener { startActivity(Intent(this,OTP::class.java)) }
        tv_get_otp.setOnClickListener { getSubscriber() }
    }


    private fun getSubscriber(){

           val api = Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(MyAPI::class.java)

         lifecycleScope.launch(Dispatchers.IO) {
          val response = api.getSubscriber("getSubscriber","0100844789")
          Log.d("######", response.message.toString())
         }



    }
}