package com.zuku.smartbill.zukufiber.ui

import android.app.Activity
import android.app.AlertDialog
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
import com.zuku.smartbill.zukufiber.data.services.save
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Field

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        image_close6.setOnClickListener { finish() }

        getInfo()
        edTaxPin.text.append(getValue(this,"taxpin"))
        edPhoneNumber.text.append(getValue(this,"cellcont"))
        edEmail.text.append(getValue(this,"emcont"))


        tvUpdatePlan.setOnClickListener { updateProfile() }
        logout.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Would you like to logout? or Just to exit?")


        builder.setPositiveButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.setNegativeButton("Logout") { dialog, which ->
            startActivity(Intent(this,Login ::class.java))
            save(this,"login","false")
            finishAffinity()
        }

        builder.setNeutralButton("Exit") { dialog, which ->
            finishAffinity()
        }
        builder.show()
    }
    private fun Activity.openWebPage(url: String?) = url?.let {

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
    private fun updateProfile(){
        progress_circular2.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){

            val result = api.updateProfile(
                "updateProfile",
                getValue(this@Profile,"subid").toString(),
                getValue(this@Profile,"subdb").toString(),
                edTaxPin.text.toString(),
                edEmail.text.toString(),
                edPhoneNumber.text.toString(),
                getValue(this@Profile,"cid").toString())

            runOnUiThread {   progress_circular2.visibility = View.GONE
            Toast.makeText(this@Profile,result.message,Toast.LENGTH_LONG).show()
            }
        }

    }


   /* save(this,"cid", item.subcontacts.cid)
    save(this,"cellcont", item.subcontacts.cellcont)
    save(this,"emcont", item.subcontacts.cellcont)
    save(this,"taxpin", item.subcontacts.cellcont)*/
}