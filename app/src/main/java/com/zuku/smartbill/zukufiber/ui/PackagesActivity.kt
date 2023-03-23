package com.zuku.smartbill.zukufiber.ui

import PackageItems
import Packages
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.Const
import com.zuku.smartbill.zukufiber.ui.adapter.PackageAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter
import kotlinx.android.synthetic.main.activity_packages.*

class PackagesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)
        image_close.setOnClickListener { finish() }
        initRecyclerViewRadio(Const.ConstHolder.INSTANCE.getPackages()!!)
    }


    private fun initRecyclerViewRadio(arrayList: List<Packages>){
        //Packages
        runOnUiThread {
           val adapter = PackageAdapter(this,arrayList)
            recycler_view_radio.adapter = adapter
            recycler_view_radio.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false)
        }

    }

     fun initRecyclerView(packageItems: List<PackageItems>){
        //Packages
        val adapter = PackagesAdapter(this,packageItems)
        recycler_view2.adapter = adapter
        recycler_view2.layoutManager = GridLayoutManager(this, 2)
    }

}