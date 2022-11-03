package com.zuku.smartbill.zukufiber.ui.adapter

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


class PackagesAdapter(private val context: Context, private val dataSet: Array<String>) :
        RecyclerView.Adapter<PackagesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSpeed: TextView
        val tvAmount: TextView
        var clPackage: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View.
            tvSpeed = view.findViewById(R.id.tv_speed)
            tvAmount = view.findViewById(R.id.tv_amount)
            clPackage = view.findViewById(R.id.cl_package)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_package, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvSpeed.text =  "MBPS "+ dataSet[position].toString().split(":")[0]
        viewHolder.tvAmount.text =  "KES " + dataSet[position].toString().split(":")[1]
        viewHolder.clPackage.setOnClickListener {
            context.startActivity(Intent(context, PackageDetails::class.java)) }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
