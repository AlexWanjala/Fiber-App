package com.zuku.smartbill.zukufiber.ui.adapter

import PaymentData
import Procedure
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.activity_package_details.*
import kotlinx.android.synthetic.main.activity_payments.*
import kotlinx.android.synthetic.main.message_box.view.*


class PaymentAdapter(private val context: Context, private val dataSet:  List<PaymentData>) :
        RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvView: TextView
        val listLayout: LinearLayout
        val layout: LinearLayoutCompat
        val recycler_view: RecyclerView

        init {
            // Define click listener for the ViewHolder's View.
            tvView = view.findViewById(R.id.tv_view)
            listLayout = view.findViewById(R.id.list_layout)
            layout = view.findViewById(R.id.layout)
            recycler_view  = view.findViewById(R.id.recycler_view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_payment_options, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position2: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvView.text = dataSet[position2].paymentsoptions.options
        viewHolder.tvView.setOnClickListener {
            if(viewHolder.listLayout.visibility==View.VISIBLE){
                viewHolder.listLayout.visibility = View.GONE
                viewHolder.tvView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_black, 0)
              /*  viewHolder.tvView.setBackgroundColor(Color.parseColor("#F6FBFD"))
                viewHolder.layout.setBackgroundColor(Color.parseColor("#F6FBFD"))*/
            }else{
                viewHolder.listLayout.visibility = View.VISIBLE
                viewHolder.tvView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_black, 0)
               /*viewHolder.tvView.setBackgroundColor(Color.parseColor("#D3DBDE"))
                viewHolder.layout.setBackgroundColor(Color.parseColor("#D3DBDE"))*/
            }
        }



        val adapter = PaymentAdapterOptionsList(context,dataSet[position2].paymentoptionsdatalist)
        viewHolder.recycler_view.adapter = adapter
        viewHolder.recycler_view.layoutManager = LinearLayoutManager(context)



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
