package com.zuku.smartbill.zukufiber.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        register.setOnClickListener { startActivity(Intent(this,com.zuku.smartbill.zukufiber.ui.landing.Login::class.java)) }
        login.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        if(getValue(this,"login").equals("true")){
            register.visibility = View.GONE
            if(isOnline(this)){
               // getInfo()
            }else{
                Toast.makeText(this,"You are offline",Toast.LENGTH_LONG).show()
            }


        }else{
            startActivity(Intent(this,com.zuku.smartbill.zukufiber.ui.landing.Login::class.java))
            finish()
        }
        super.onResume()
    }
}