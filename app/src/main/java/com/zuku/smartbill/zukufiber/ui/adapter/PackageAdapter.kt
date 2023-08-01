package com.zuku.smartbill.zukufiber.ui.adapter

import Packages
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.ui.MainActivity
import com.zuku.smartbill.zukufiber.ui.PackagesActivity
import kotlinx.android.synthetic.main.activity_packages.*
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.item_payment_options.*
import kotlinx.android.synthetic.main.item_payment_options.recycler_view


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
        val item = dataSet[position]

        //Load the 1st item
        if(position==0){
            if (context is MainActivity) {
                //(context as MainActivity).initRecyclerView(item.packageItems)

                //Check the 1st item
                viewHolder.radio_1.isChecked = true
                if (lastCheckedRB != null) {
                    lastCheckedRB?.isChecked = false
                }
                lastCheckedRB = viewHolder.radio_1

            }
            if (context is PackagesActivity) {
                if (item.packageItems==null || item.packageItems.isEmpty()){
                   // Toast.makeText(context,"No further packages available check later",Toast.LENGTH_LONG).show()
                    (context as PackagesActivity).recycler_view2.adapter = null
                }else{
                    (context as PackagesActivity).initRecyclerView(item.packageItems)

                    //Check the 1st item
                    viewHolder.radio_1.isChecked = true
                    if (lastCheckedRB != null) {
                        lastCheckedRB?.isChecked = false
                    }
                    lastCheckedRB = viewHolder.radio_1
                }

            }
        }
        viewHolder.radio_1.text = item.packageName.toString().lowercase().capitalize()
        viewHolder.radio_1.setOnClickListener {
            if (lastCheckedRB != null) {
                lastCheckedRB?.isChecked = false
            }
            //store the clicked radiobutton
            lastCheckedRB = viewHolder.radio_1


            if (context is MainActivity) {
                if (item.packageItems==null || item.packageItems.isEmpty()){
                   // Toast.makeText(context,"No further packages available check later",Toast.LENGTH_LONG).show()
                    (context as MainActivity).recycler_view.adapter = null
                }else{
                  //  (context as MainActivity).initRecyclerView(item.packageItems)
                }

            }

            if (context is PackagesActivity) {
                if (item.packageItems==null || item.packageItems.isEmpty()){
                   // Toast.makeText(context,"No further packages available check later",Toast.LENGTH_LONG).show()
                    (context as PackagesActivity).recycler_view2.adapter = null
                }else{
                    (context as PackagesActivity).initRecyclerView(item.packageItems)
                }

            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
