package com.zuku.smartbill.zukufiber.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.TransactionResponse


class TransactionAdapter(private val context: Context, private val dataSet: Array<String>) :
        RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhone: TextView
        var layoutTransaction: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View.
            tvPhone = view.findViewById(R.id.tv_phone)
            layoutTransaction = view.findViewById(R.id.layout_transaction)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_transaction, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.layoutTransaction.setOnClickListener { context.startActivity(Intent(context, TransactionResponse::class.java)) }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
