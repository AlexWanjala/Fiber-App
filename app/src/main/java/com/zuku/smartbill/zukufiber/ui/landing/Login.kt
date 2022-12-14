package com.zuku.smartbill.zukufiber.ui.landing

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.save
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                        startActivity(Intent(this@Login, OTP::class.java).putExtra("phoneNumber", edPhone.text.toString()))
                    } else {
                        Toast.makeText(this@Login, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (ex: Exception) {
                runOnUiThread { Toast.makeText(this@Login, ex.message, Toast.LENGTH_LONG).show() }

            }
        }
    }
}