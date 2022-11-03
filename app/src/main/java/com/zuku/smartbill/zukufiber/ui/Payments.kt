package com.zuku.smartbill.zukufiber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.adapter.PaymentAdapter
import kotlinx.android.synthetic.main.activity_payments.*


class Payments : AppCompatActivity() {

    private val arrayList: Array<String> = arrayOf("Zuku Fiber","Zuku TV","Zuku Phone Airtime","Mobile Banking","Direct Banking")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        val adapter = PaymentAdapter(this,arrayList)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        image_close.setOnClickListener { finish() }
    }

}