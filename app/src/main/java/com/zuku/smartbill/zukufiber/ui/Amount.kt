package com.zuku.smartbill.zukufiber.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.Const
import kotlinx.android.synthetic.main.activity_amount.*

class Amount : AppCompatActivity(), View.OnClickListener {
    lateinit var value: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount)

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
        value =""

        tvDescription.text = intent.getStringExtra("desc").toString()
        tvAmountDue.text = intent.getStringExtra("amountDue").toString()
        tvAccNo.text = "Acc No."+ intent.getStringExtra("accNo").toString()
    }

    private fun setText(text: String){
        value += text
        updateEditText()
    }
    private fun updateEditText(){
        tv_amount.text = ""
        tv_amount.text = value
        if(value.isEmpty()){
            tv_currency.visibility = View.GONE
            tv_currency_disabled.visibility = View.VISIBLE

            btnNext.visibility = View.GONE
            btnNextDisabled.visibility = View.VISIBLE
        }else{
            tv_currency.visibility = View.VISIBLE
            tv_currency_disabled.visibility = View.GONE

            btnNext.visibility = View.VISIBLE
            btnNextDisabled.visibility = View.GONE
        }
    }
    private fun removeText(){
        value= value.dropLast(1)
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
            R.id.tvBack -> {removeText()}
            R.id.btnNext ->{validate()}
        }
    }
    private fun validate(){
        if(tv_amount.text.isEmpty()){
            Toast.makeText(this,"Amount Required", Toast.LENGTH_LONG).show()
        }else{
            startActivity(Intent(this@Amount, PhoneNumber::class.java)
                .putExtra("amount",tv_amount.text)
                .putExtra("accNo",intent.getStringExtra("accNo").toString())
                .putExtra("speed",intent.getStringExtra("speed").toString())
                .putExtra("desc",intent.getStringExtra("desc").toString())
            )
        }

    }

}