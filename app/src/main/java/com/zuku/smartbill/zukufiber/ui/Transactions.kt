package com.zuku.smartbill.zukufiber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.adapter.TransactionAdapter
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.activity_transactions.image_close

class Transactions : AppCompatActivity() {

    private val arrayList: Array<String> = arrayOf("20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM","20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM","20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM","20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM",
        "20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM","20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM","20 Jan, 08:47 AMr","20 Jan, 08:47 AM","20 Jan, 08:47 AM")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        image_close.setOnClickListener { finish() }

        val adapter = TransactionAdapter(this,arrayList)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
    }
}