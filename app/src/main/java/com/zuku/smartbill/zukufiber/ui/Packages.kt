package com.zuku.smartbill.zukufiber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter2
import kotlinx.android.synthetic.main.activity_packages.*
import kotlinx.android.synthetic.main.radio_group.*

class Packages : AppCompatActivity() {

    private val arrayList: Array<String> = arrayOf("10:2,899","20:4,399","60:6,299","100:8,299","200:10,299","500:15,299")
    private val arrayList2: Array<String> = arrayOf("20:4,899","30:5,399","70:7,299","200:10,299","400:12,299","600:16,299")
    private val arrayList3: Array<String> = arrayOf("30:5,899","40:6,399","80:8,299","300:11,299","500:12,299","700:7,299")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)
        image_close.setOnClickListener { finish() }
        radio_1.isChecked = true

        initRecyclerView(arrayList)
        radio_group.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.radio_1 -> { initRecyclerView(arrayList)
                    }
                    R.id.radio_2 -> {
                        initRecyclerView(arrayList2)
                    } R.id.radio_3 -> {
                    initRecyclerView(arrayList3)
                }
                }
            }
        }

    }

    private fun initRecyclerView(arrayList:Array<String> ){
        //Packages
        val adapter = PackagesAdapter2(this,arrayList)
        recycler_view2.adapter = adapter
        recycler_view2.layoutManager = GridLayoutManager(this, 2)
    }

}