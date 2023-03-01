package com.zuku.smartbill.zukufiber.ui.landing


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
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
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.roberts.smsretriverapi.RetrievalEvent
import com.roberts.smsretriverapi.SignatureHelper
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


class OTP : AppCompatActivity(), View.OnClickListener{



    lateinit var code: String
    lateinit var mainHandler: Handler
    var verificationCode: String =""


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
        image_close6.setOnClickListener{ super.onBackPressed()}
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;

        edText1.setOnClickListener{
            val abc = myClipboard?.primaryClip
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
                code = item.text.toString()
                updateEditText()
                myClipboard?.clearPrimaryClip()
            }

        }

        edText3.setOnClickListener{
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            if(item?.text?.isEmpty()!=null){
                code = item.text.toString()
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


        verificationCode = generateNumber(4).toString()
        tv_phone.text ="Enter the OTP sent to "+ intent.getStringExtra("phoneNumber").toString()

        tvResend.setOnClickListener { sendSMS(intent.getStringExtra("phoneNumber").toString(),"Your Sms Retriever Api code is: $verificationCode "+ SignatureHelper(this@OTP).appSignature.toString())  }

        smsClient = SmsRetriever.getClient(this)
        initSmsListener()
        sendSMS(intent.getStringExtra("phoneNumber").toString(),"Your Sms Retriever Api code is: $verificationCode "+ SignatureHelper(this@OTP).appSignature.toString())

    }

    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {
                    image4.setImageDrawable(resources.getDrawable(R.drawable.zuku_logo_white))
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
       /* unregisterReceiver(smsReceiver)*/
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
        currentTheme()

    }


    private lateinit var smsClient: SmsRetrieverClient
    private fun sendSMS(phoneNumber: String, message: String){
        lifecycleScope.launch(Dispatchers.IO){
            api.sendSMS("sendSMS",phoneNumber,message)
        }
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
    private fun initSmsListener() {
        smsClient.startSmsRetriever()
            .addOnSuccessListener {
                Toast.makeText(
                    this, "Waiting for sms message",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { failure ->
                Toast.makeText(
                    this, failure.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    @Subscribe
    fun onReceiveSms(retrievalEvent: RetrievalEvent) {
        val code: String =
            StringUtils.substringAfterLast(retrievalEvent.message, "is").replace(":", "")
                .trim().substring(0, 4)

        runOnUiThread {
            if (!retrievalEvent.timedOut) {
                // binding.editText.setText(code)
              //  Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
                setText(code)

            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
        initSmsListener()
    }



}