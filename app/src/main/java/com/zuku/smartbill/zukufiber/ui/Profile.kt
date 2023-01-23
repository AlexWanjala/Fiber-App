package com.zuku.smartbill.zukufiber.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        getInfo()
    }

    fun Activity.openWebPage(url: String?) = url?.let {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

     private fun getInfo(){
      lifecycleScope.launch(Dispatchers.IO){

          val result =  api.getInfo("getInfo", getValue(this@Profile,"subdb").toString())

          if(result.success){
              runOnUiThread {
                  whatsapp.setOnClickListener {
                      val url = "https://api.whatsapp.com/send?phone="+result.data.appinfo.whatsapp
                      val i = Intent(Intent.ACTION_VIEW)
                      i.data = Uri.parse(url)
                      startActivity(i)
                  }
                  call.setOnClickListener {
                      val dialIntent = Intent(Intent.ACTION_DIAL)
                      dialIntent.data = Uri.parse("tel:" + result.data.appinfo.phone)
                      startActivity(dialIntent)
                  }
                  info.setOnClickListener {

                      openWebPage(result.data.appinfo.info)

                  }
                  faq.setOnClickListener {
                   //   Toast.makeText(this@Profile,"hhss",Toast.LENGTH_LONG).show()
                      openWebPage(result.data.appinfo.faq)
                  }
                  facebook.setOnClickListener {

                      openWebPage(result.data.appinfo.facebook)

                  }
                  twitter.setOnClickListener {

                      openWebPage(result.data.appinfo.twitter)

                  }
              }

          }else{
              runOnUiThread {
                  whatsapp.visibility = View.GONE
                  call.visibility = View.GONE
                  info.visibility = View.GONE
                  faq.visibility = View.GONE
              }
          }

      }

  }
}