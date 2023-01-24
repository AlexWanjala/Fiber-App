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

        viewHolder.tvItem.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.stat_notify_more, 0, 0, 0);

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}
