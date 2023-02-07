package com.zuku.smartbill.zukufiber.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.Const
import com.zuku.smartbill.zukufiber.data.services.api

import kotlinx.android.synthetic.main.activity_phone_number.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PhoneNumber : AppCompatActivity(), View.OnClickListener {
    lateinit var value: String
    var amount : String =""
    var accNo : String =""
    var paybill : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        image_close.setOnClickListener { finish() }
        tv0.setOnClickListener(this)
        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        tv4.setOnClickListener(this)
        tv5.setOnClickListener(this)
        tv6.setOnClickListener(this)
        tv7.setOnClickListener(this)
        tv8.setOnClickListener(this)
        tv9.setOnClickListener(this)
        tvBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        tvPlus.setOnClickListener(this)

        value = ""
        amount =  intent.getStringExtra("amount").toString()
        accNo =  intent.getStringExtra("accNo").toString()
        paybill = intent.getStringExtra("desc").toString().split(":")[1]

    }

    private fun setText(text: String) {
        value += text
        updateEditText()
    }

    private fun updateEditText() {
        tv_phone.text = ""
        tv_phone.text = value
        if (value.isEmpty()) {
            btnNext.visibility = View.GONE
            btnNextDisabled.visibility = View.VISIBLE
        } else {

            btnNext.visibility = View.VISIBLE
            btnNextDisabled.visibility = View.GONE
        }
    }

    private fun removeText() {
        value = value.dropLast(1)
        updateEditText()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv0 -> {
                setText("0")
            }
            R.id.tv1 -> {
                setText("1")
            }
            R.id.tv2 -> {
                setText("2")
            }
            R.id.tv3 -> {
                setText("3")
            }
            R.id.tv4 -> {
                setText("4")
            }
            R.id.tv5 -> {
                setText("5")
            }
            R.id.tv6 -> {
                setText("6")
            }
            R.id.tv7 -> {
                setText("7")
            }
            R.id.tv8 -> {
                setText("8")
            }
            R.id.tv9 -> {
                setText("9")
            }

            R.id.tvPlus -> {
                setText("+")
            }
            R.id.tvBack -> {
                removeText()
            }
            R.id.btnNext -> {
                validate()
            }
        }
    }

    private fun validate() {
        if (tv_phone.text.isEmpty()) {
            Toast.makeText(this, "Amount Required", Toast.LENGTH_LONG).show()
        } else {
            sendPrompt(accNo,amount,tv_phone.text.toString(),paybill.toString());
        }

    }
    private fun sendPrompt(accNo: String,amount: String,phoneNumber: String, paybill: String){
        tv_message.text ="Initiating Payment Prompt.."
        progress_circular.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){
            val result = api.sendPrompt("sendPrompt", accNo,amount, phoneNumber,paybill)
            runOnUiThread {
                progress_circular.visibility = View.GONE
                tv_message.text =result.message }
            if (result.success){
                checkPayment(accNo)
            }

        }

    }
    private fun checkPayment(accNo: String){
        lifecycleScope.launch(Dispatchers.IO){
            val result = api.checkPayment("checkPayment",accNo)

            if(result.success){
                runOnUiThread { tv_message.text ="Payment received" }
                Const.ConstHolder.INSTANCE.getInstance().setPush(result.data.push)
                if(result.data.push.callback_returned=="PAID"){
                    startActivity(Intent(this@PhoneNumber, TransactionResponse::class.java)
                        .putExtra("accNo",accNo)
                        .putExtra("amount",result.data.push.amount)
                        .putExtra("paymentCode",result.data.push.transaction_code)
                        .putExtra("speed",intent.getStringExtra("speed"))
                    )
                }else if(result.data.push.callback_returned=="PENDING"){
                    runOnUiThread { tv_message.text ="Waiting for payment.." }
                    TimeUnit.SECONDS.sleep(2L)
                    checkPayment(accNo)
                }else{
                    runOnUiThread { tv_message.text = result.data.push.message}
                }

            }
            else{
                runOnUiThread { tv_message.text ="Waiting for payment.." }
                TimeUnit.SECONDS.sleep(2L)
                checkPayment(accNo)
            }

        }
    }


}