package com.zuku.smartbill.zukufiber.ui.landing

import android.content.Intent
import android.content.res.Configuration
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class Login : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var countries: MutableList<String> = ArrayList()

    var country =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        image_close2.setOnClickListener { finish() }

        tv_get_otp.setOnClickListener {
            if (edPhone.text.isEmpty()){
                Toast.makeText(this,"Please input registered phone number",Toast.LENGTH_LONG).show()
            }else{
                if(country=="" || country =="SELECT COUNTY"){
                    Toast.makeText(this,"Select County",Toast.LENGTH_LONG).show()
                }else{

                }
                getSubscriber()
            }
        }

        countries.add("SELECT COUNTY")
        countries.add("KENYA")
        countries.add("UGANDA")
        countries.add("TANZANIA")
        countries.add("MALAWAI")
        countries.add("ZAMBIA")


        val aa = ArrayAdapter(this@Login, R.layout.spinner_right_aligned, countries)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(mySpinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@Login
            prompt = R.string.select_acc.toString()
            gravity = Gravity.CENTER

        }


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent != null) {
            country = parent.getItemAtPosition(position).toString()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

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
                        Toast.makeText(this@Login, response.data.subDetailsResponse[0].packageinfo.subdb.toString(),Toast.LENGTH_LONG).show()
                        if(country=="KENYA" || country=="ZAMBIA"){
                            startActivity(Intent(this@Login, OTP::class.java).putExtra("country",country).putExtra("phoneNumber", edPhone.text.toString()))
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
                runOnUiThread {
                    image3.setImageDrawable(resources.getDrawable(R.drawable.zuku_logo_white))
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //   Toast.makeText(this,"LIGHT",Toast.LENGTH_LONG).show()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }


}