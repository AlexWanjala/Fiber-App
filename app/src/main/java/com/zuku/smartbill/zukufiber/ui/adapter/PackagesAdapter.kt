package com.zuku.smartbill.zukufiber.ui.adapter

import PackageItems
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.ui.PackageDetails
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.MainActivity


class PackagesAdapter(private val context: Context, private val dataSet: List<PackageItems>) :
        RecyclerView.Adapter<PackagesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var tvSpeed: TextView
        lateinit var tv_currency: TextView
        lateinit var tvAmount: TextView
        lateinit var clPackage: ConstraintLayout
        lateinit var tvDes: TextView


        init {
            if (view.context is MainActivity){
                // Define click listener for the ViewHolder's View.
                tvSpeed = view.findViewById(R.id.tv_speed)
                tvAmount = view.findViewById(R.id.tv_amount)
                clPackage = view.findViewById(R.id.cl_package)
            }else{
                tv_currency = view.findViewById(R.id.tv_currency)
                tvAmount = view.findViewById(R.id.tv_amount)
                tvSpeed = view.findViewById(R.id.tv_speed)
                tvDes = view.findViewById(R.id.tvDes)
                clPackage = view.findViewById(R.id.cl_package)
            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        if (context is MainActivity) {
            return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_package, viewGroup, false))
        }else{
            return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_package_, viewGroup, false))
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val list = dataSet[position];

        if (context is MainActivity) {
            viewHolder.tvSpeed.text = list.item
            viewHolder.tvAmount.text =  "${list.currency} ${list.price}"
            viewHolder.clPackage.setOnClickListener {
                context.startActivity(Intent(context, PackageDetails::class.java)
                    .putExtra("packageName",list.packageName)
                    .putExtra("item",list.item)
                    .putExtra("price",list.price)
                    .putExtra("des",list.des)
                    .putExtra("currency",list.currency)

                )
            }
        }else{
            viewHolder.tv_currency.text = list.currency
            viewHolder.tvAmount.text = list.price
            viewHolder.tvSpeed.text = list.item
            viewHolder.tvDes.text = list.des
            viewHolder.clPackage.setOnClickListener {
                context.startActivity(Intent(context, PackageDetails::class.java)
                    .putExtra("packageName",list.packageName)
                    .putExtra("item",list.item)
                    .putExtra("price",list.price)
                    .putExtra("des",list.des)
                    .putExtra("currency",list.currency)

                )
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
