package com.zuku.smartbill.zukufiber.ui.adapter

import Invoices
import Substrans
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
import kotlinx.android.synthetic.main.activity_transaction_reponse.*


class TransactionAdapter(private val context: Context, private val dataSet:  List<Invoices>) :
        RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhone: TextView
        val tv_amount: TextView
        val tdate: TextView
        val tv_payment_type: TextView
        var layoutTransaction: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View.
            tvPhone = view.findViewById(R.id.tv_phone)
            tv_amount = view.findViewById(R.id.tv_amount)
            tdate = view.findViewById(R.id.tdate)
            tv_payment_type = view.findViewById(R.id.tv_payment_type)
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
        var list = dataSet[position]
        viewHolder.tvPhone.text = list.invoice_no
        viewHolder.tv_payment_type.text = list.customer_name
        viewHolder.tv_amount.text = list.currency+" "+ list.value
        viewHolder.tdate.text =list.invoice_date
        viewHolder.layoutTransaction.setOnClickListener {
            context.startActivity(Intent(context, TransactionResponse::class.java)
            .putExtra("id",list.id.toString())
            .putExtra("customer_name",list.customer_name)
            .putExtra("accNo",list.invoice_no)
            .putExtra("amount", list.currency+" "+ list.value)
            .putExtra("invoice_date",list.invoice_date)
            .putExtra("speed",list.invoice_date)
        ) }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
