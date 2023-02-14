package com.zuku.smartbill.zukufiber.ui

import android.annotation.SuppressLint
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.ui.adapter.TransactionAdapter
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.activity_transactions.image_close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Transactions : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        image_close.setOnClickListener { finish() }
        getSubsTrans(intent.getStringExtra("subid").toString())
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getSubsTrans(subid :String){
        progress_circular.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO){
          val result = api.getInvoices("getInvoices",subid)
            runOnUiThread { progress_circular.visibility = View.GONE }
            if(result.success){
               runOnUiThread {
                   val adapter = TransactionAdapter(this@Transactions,result.data.invoices)
                   recycler_view.adapter = adapter
                   recycler_view.layoutManager = LinearLayoutManager(this@Transactions)
               }

            }else{
                runOnUiThread { Toast.makeText(this@Transactions,result.message,Toast.LENGTH_LONG).show()}
            }
        }

    }
}