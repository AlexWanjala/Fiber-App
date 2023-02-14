package com.zuku.smartbill.zukufiber.ui.adapter

import Payments
import Paymethods
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.launchSTK
import com.zuku.smartbill.zukufiber.data.services.save
import com.zuku.smartbill.zukufiber.ui.MainActivity
import com.zuku.smartbill.zukufiber.ui.PaymentsActivity
import kotlinx.android.synthetic.main.bottom_sheet_plans.*


class SubPaymentMethodsAdapter(private val context: Context, private val dataSet: List<Payments>) :
        RecyclerView.Adapter<SubPaymentMethodsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView



        init {
            // Define click listener for the ViewHolder's View.
            tvItem = view.findViewById(R.id.tvItem)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_button, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvItem.text = dataSet[position].payment
        if(dataSet[position].payment.contains("FIBER")) viewHolder.tvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_interent_, 0, 0, 0)
        if(dataSet[position].payment.contains("PHONE"))
            viewHolder.tvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_telephone_, 0, 0, 0)

        viewHolder.tvItem.setOnClickListener {
            if(dataSet[position].method=="STK"){

                if (context is MainActivity) {
                (context as MainActivity).stkPayments(dataSet[position].desc)
                }
           }else if (dataSet[position].method=="SIMTOOLKIT"){
                 save(context,"paybill",dataSet[position].desc.split(":")[1])
                //paybill: String, accNo: String
                launchSTK(context)
            }
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}
