package com.zuku.smartbill.zukufiber.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.getValue
import com.zuku.smartbill.zukufiber.ui.adapter.TransactionAdapter
import kotlinx.android.synthetic.main.activity_packages.*
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.activity_transactions.image_close
import kotlinx.android.synthetic.main.activity_transactions.layoutMain
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
          val result = api.getInvoices("getInvoices",subid, getValue(this@Transactions,"subdb").toString())
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
    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {//Night Mode
                    layoutMain.background = resources.getDrawable(R.drawable.background_dark_one)
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light Mode
                layoutMain.background = resources.getDrawable(R.drawable.background_light_one)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        currentTheme()

    }

}