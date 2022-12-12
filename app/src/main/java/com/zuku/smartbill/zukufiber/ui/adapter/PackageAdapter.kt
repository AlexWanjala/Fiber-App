package com.zuku.smartbill.zukufiber.ui.adapter

import Packages
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.MainActivity


class PackageAdapter(private val context: Context, private val dataSet: List<Packages>) :
        RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    var selectedPosition = -1
    private var mSelectedItem = -1
    private var lastCheckedRB: RadioButton? = null



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radio_1: RadioButton


        init {
            // Define click listener for the ViewHolder's View.
            radio_1 = view.findViewById(R.id.radio_1)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.radio_group, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position];

        viewHolder.radio_1.text = item.packageName.toString().lowercase().capitalize()

        viewHolder.radio_1.setOnClickListener {
            if (lastCheckedRB != null) {
                lastCheckedRB?.isChecked = false
            }
            //store the clicked radiobutton
            lastCheckedRB = viewHolder.radio_1


            if (context is MainActivity) {
                (context as MainActivity).selectedPackageItem(position)
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
