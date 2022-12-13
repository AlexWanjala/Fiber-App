package com.zuku.smartbill.zukufiber.ui.landing

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.ui.MainActivity
import com.zuku.smartbill.zukufiber.ui.TransactionResponse
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OTP : AppCompatActivity(), View.OnClickListener{

    lateinit var code: String
    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            minusOneSecond()
            mainHandler.postDelayed(this, 1000)
        }
    }
    private var secondsLeft = 45

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

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

      /*  Handler(Looper.getMainLooper()).postDelayed(
            {
                setText(getValue(this@OTP,"verificationCode").toString())
            },
            3000 // value in milliseconds
        )
*/
      /*  initBroadCast()
        initSmsListener()*/

       // sendSMS()
    }

    fun minusOneSecond() {
        if (secondsLeft > 0 ){
            if(secondsLeft==0){
                mainHandler.removeCallbacks(updateTextTask)
            }else{
                secondsLeft -= 1

            }

        }
    }
    private fun validate(){
        startActivity(Intent(this,MainActivity::class.java))
      /*  if(getValue(this,"verificationCode").equals(code)){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            Toast.makeText(this,"Invalid", Toast.LENGTH_LONG).show()
        }*/

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

    }
    private fun sendSMS(phoneNumber: String, message: String){
        lifecycleScope.launch(Dispatchers.IO){
          var result =   api.sendSMS("sendSMS",phoneNumber,message)
            if(result.success){

            }else{
                Toast.makeText(this@OTP,result.message,Toast.LENGTH_LONG).show()
            }
        }
    }

/*
    //SMS Reader
    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null

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
*/

}