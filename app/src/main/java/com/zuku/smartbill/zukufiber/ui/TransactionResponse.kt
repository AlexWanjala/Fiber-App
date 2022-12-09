package com.zuku.smartbill.zukufiber.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_transaction_reponse.*

class TransactionResponse : AppCompatActivity() {
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val TAG ="TransactionResponse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_reponse)
        close.setOnClickListener { finish() }
        btn_download.setOnClickListener {
            startActivity(Intent(this,PDFViewer::class.java))

        }

     /*   val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyAPI::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            val response = api.getComments()
            if(response.isSuccessful){
                for (comment in response.body()!!){
                    Log.d(TAG,comment.toString())
                }
            }
        }*/
    }
}