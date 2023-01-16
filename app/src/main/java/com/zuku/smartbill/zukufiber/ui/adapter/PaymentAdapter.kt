package com.zuku.smartbill.zukufiber.ui.adapter

import PaymentData
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R



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
        val listView: ListView

        init {
            // Define click listener for the ViewHolder's View.
            tvView = view.findViewById(R.id.tv_view)
            listLayout = view.findViewById(R.id.list_layout)
            layout = view.findViewById(R.id.layout)
            listView  = view.findViewById(R.id.list_view)
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
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvView.text = dataSet[position].paymentsoptions.options
        viewHolder.tvView.setOnClickListener {
            if(viewHolder.listLayout.visibility==View.VISIBLE){
                viewHolder.listLayout.visibility = View.GONE
                viewHolder.tvView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_black, 0)
                viewHolder.tvView.setBackgroundColor(Color.parseColor("#F6FBFD"))
                viewHolder.layout.setBackgroundColor(Color.parseColor("#F6FBFD"))
            }else{
                viewHolder.listLayout.visibility = View.VISIBLE
                viewHolder.tvView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_black, 0)
                viewHolder.tvView.setBackgroundColor(Color.parseColor("#D3DBDE"))
                viewHolder.layout.setBackgroundColor(Color.parseColor("#D3DBDE"))
            }
        }

        // use array-adapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val array = arrayListOf<String>()
        for (data in dataSet[position].paymentoptionsdata){
            array.add(data.option)
        }
        // access the listView from xml file
        arrayAdapter = ArrayAdapter(context , R.layout.list_item_view, array)
        viewHolder.listView.adapter = arrayAdapter
        viewHolder.listView.divider = null;
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
