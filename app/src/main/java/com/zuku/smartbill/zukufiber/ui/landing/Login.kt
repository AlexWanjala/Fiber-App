package com.zuku.smartbill.zukufiber.ui.landing

import android.content.Intent
import android.content.res.Configuration
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.android.synthetic.main.activity_login.layoutMain as layoutMain1

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        image_close2.setOnClickListener { finish() }

        tv_get_otp.setOnClickListener {
            if (edPhone.text.isEmpty()){
                Toast.makeText(this,"Please input registered phone number",Toast.LENGTH_LONG).show()
            }else{
                getSubscriber()
            }
        }
    }
    private fun getSubscriber() {
        progress_circular.visibility = View.VISIBLE

       lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = api.getSubscriber("getSubscriber", edPhone.text.toString())
                runOnUiThread {
                    progress_circular.visibility = View.GONE
                    if (response.success) {
                        save(this@Login,"phoneNumber",edPhone.text.toString())
                        if(response.data.subDetailsResponse[0].packageinfo.subdb.contains("Ke")){
                            startActivity(Intent(this@Login, OTP::class.java).putExtra("phoneNumber", edPhone.text.toString()))
                        }else{
                            startActivity(Intent(this@Login, MainActivity::class.java))
                            save(this@Login,"login","true")
                            finishAffinity()
                        }

                    } else {
                        Toast.makeText(this@Login, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (ex: Exception) {
                runOnUiThread { Toast.makeText(this@Login, ex.message, Toast.LENGTH_LONG).show() }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentTheme()
    }

    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {//Night Mode
                    layoutMain1.background = resources.getDrawable(R.drawable.background_dark_one)
                    val logoImageView =findViewById<ImageView>(R.id.image4)
                    logoImageView.setImageResource(R.drawable.logo_white)
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light Mode
                layoutMain1.background = resources.getDrawable(R.drawable.background_light)
                val logoImageView =findViewById<ImageView>(R.id.image4)
                logoImageView.setImageResource(R.drawable.logo_black)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }
}