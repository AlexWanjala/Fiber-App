package com.roberts.smsretriverapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sms)



        //uncomment this to generate your app hash string. You can view the hash string on your log cat when you run the app
        /*val appSignatureHelper = SignatureHelper(this)
        Log.d("SIGNATURE",appSignatureHelper.appSignature.toString())*/
        smsClient = SmsRetriever.getClient(this)
        initSmsListener()
        sendSMS("0719401837","Your Sms Retriever Api code is: 6647 "+SignatureHelper(this@MainActivity).appSignature.toString())

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
                Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
        initSmsListener()
    }
}