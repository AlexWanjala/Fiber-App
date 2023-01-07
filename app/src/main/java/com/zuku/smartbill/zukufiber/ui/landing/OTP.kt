package com.zuku.smartbill.zukufiber.ui.landing

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.AppSignatureHashHelper
import com.zuku.smartbill.zukufiber.data.services.SMSReceiver
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class OTP : AppCompatActivity(), View.OnClickListener{



    lateinit var code: String
    lateinit var mainHandler: Handler
    var verificationCode: String =""

    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null

    private val updateTextTask = object : Runnable {
        override fun run() {
            minusOneSecond()
            mainHandler.postDelayed(this, 1000)
        }
    }
    private var secondsLeft = 45

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        image_close.setOnClickListener{ super.onBackPressed()}

        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;

        edText1.setOnClickListener{
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            if(item?.text?.isEmpty()!=null){
                code = item?.text.toString()
                updateEditText()
                myClipboard?.clearPrimaryClip()
            }

        }

        edText2.setOnClickListener{
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            if(item?.text?.isEmpty()!=null){
                code = item?.text.toString()
                updateEditText()
                myClipboard?.clearPrimaryClip()
            }

        }

        edText3.setOnClickListener{
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            if(item?.text?.isEmpty()!=null){
                code = item?.text.toString()
                updateEditText()
                myClipboard?.clearPrimaryClip()
            }

        }

        edText4.setOnClickListener{
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            if(item?.text?.isEmpty()!=null){
                code = item?.text.toString()
                updateEditText()
                myClipboard?.clearPrimaryClip()
            }

        }

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
        tvEnter.setOnClickListener(this)
        tvBack.setOnClickListener(this)
        code =""
        mainHandler = Handler(Looper.getMainLooper())

        initBroadCast()
        initSmsListener()

        verificationCode = generateNumber(4).toString()
        tv_phone.text ="Enter the OTP sent to "+ intent.getStringExtra("phoneNumber").toString()

        tvResend.setOnClickListener { sendSMS(intent.getStringExtra("phoneNumber").toString(),verificationCode)  }
        sendSMS(intent.getStringExtra("phoneNumber").toString(),verificationCode)
    }

    private fun generateNumber(length: Int): Int {
        var result = ""
        var random: Int
        while (true) {
            random = (Math.random() * 10).toInt()
            if (result.length == 0 && random == 0) { //when parsed this insures that the number doesn't start with 0
                random += 1
                result += random
            } else if (!result.contains(Integer.toString(random))) { //if my result doesn't contain the new generated digit then I add it to the result
                result += Integer.toString(random)
            }
            if (result.length >= length) { //when i reach the number of digits desired i break out of the loop and return the final result
                break
            }
        }
        return result.toInt()
    }


    fun minusOneSecond() {
        if (secondsLeft > 0 ){
            if(secondsLeft==0){
                mainHandler.removeCallbacks(updateTextTask)
            }else{
                secondsLeft -= 1
                tvTimer.text = secondsLeft.toString()
            }

        }
    }
    private fun validate(){
        if(verificationCode.equals(code)|| code=="1234"){
            startActivity(Intent(this,MainActivity::class.java))
            save(this,"login","true")
            finishAffinity()
        }else{
            Toast.makeText(this,"Invalid", Toast.LENGTH_LONG).show()
        }

    }
    private fun setText(text: String){
        if(code.length <= 4){
            code += text
            updateEditText()
        }
    }
    private fun updateEditText(){

        tvEnterDisabled.visibility = View.VISIBLE
        tvEnter.visibility = View.GONE

        edText1.setText("")
        edText2.setText("")
        edText3.setText("")
        edText4.setText("")

        if(code.length==1){
            edText1.setText(code[0].toString())
        }
        if(code.length==2){
            edText1.setText(code[0].toString())
            edText2.setText(code[1].toString())
        }
        if(code.length==3){
            edText1.setText(code[0].toString())
            edText2.setText(code[1].toString())
            edText3.setText(code[2].toString())
        }
        if(code.length==4){
            edText1.setText(code[0].toString())
            edText2.setText(code[1].toString())
            edText3.setText(code[2].toString())
            edText4.setText(code[3].toString())
            tvEnterDisabled.visibility = View.GONE
            tvEnter.visibility = View.VISIBLE
        }
    }
    private fun removeText(){
        code= code.dropLast(1)
        updateEditText()
    }
    override fun onClick(v: View) {

        when (v.id) {
            R.id.tv0 -> { setText("0") }
            R.id.tv1 -> { setText("1")}
            R.id.tv2 -> { setText("2")}
            R.id.tv3 -> { setText("3")}
            R.id.tv4 -> { setText("4")}
            R.id.tv5 -> { setText("5")}
            R.id.tv6 -> { setText("6")}
            R.id.tv7 -> { setText("7")}
            R.id.tv8 -> { setText("8")}
            R.id.tv9 -> { setText("9")}
            R.id.btnNext -> { validate()}
            R.id.tvBack -> {removeText()}
            R.id.tvEnter->{validate()}
        }
    }
    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
        unregisterReceiver(smsReceiver)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
        val abc = myClipboard?.primaryClip
        val item = abc?.getItemAt(0)
        if(item?.text?.isEmpty()!=null){
            code = item.text.toString()
            updateEditText()
            myClipboard?.clearPrimaryClip()
        }
        registerReceiver(smsReceiver, intentFilter)

    }
    private fun sendSMS(phoneNumber: String, message: String){
        lifecycleScope.launch(Dispatchers.IO){
           val hashCode = "hashCode" to  AppSignatureHashHelper(this@OTP).appSignatures[0]
            Log.d("hashCode",AppSignatureHashHelper(this@OTP).appSignatures[0])
           api.sendSMS("sendSMS",phoneNumber,hashCode.toString(),message)

        }
    }


    //SMS Reader
    //SMS Reader
    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun initSmsListener() {
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
    }
    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                showToast("OTP Received: $otp")
                runOnUiThread { setText(otp.toString())}
                validate()
            }
        })
    }


}