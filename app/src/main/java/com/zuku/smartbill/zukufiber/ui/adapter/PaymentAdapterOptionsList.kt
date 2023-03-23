package com.zuku.smartbill.zukufiber.ui.adapter

import Paymentoptionsdatalist
import Procedure
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.launchSTK
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.message_box.view.*


class PaymentAdapterOptionsList(private val context: Context, private val dataSet: List<Paymentoptionsdatalist>) :
        RecyclerView.Adapter<PaymentAdapterOptionsList.ViewHolder>() {

    var selectedPosition = -1
    private var mSelectedItem = -1
    private var lastCheckedRB: RadioButton? = null



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listView: TextView


        init {
            // Define click listener for the ViewHolder's View.
            listView = view.findViewById(R.id.tvPaymentOption)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.payment_option_links, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]


        viewHolder.listView.text =item.paymentoptionsdata.option

        viewHolder.listView.setOnClickListener{
            showMessageBox(context, item.procedure)
        }
        viewHolder.listView.
        setOnLongClickListener {
                if(item.paymentoptionsdata.method=="STK"){
                    if (context is MainActivity) {
                        (context as MainActivity).stkPayments(item.paymentoptionsdata.option)
                    }
                } else if (item.paymentoptionsdata.method=="SIMTOOLKIT"){

                    launchSTK(context)

                } else if (item.paymentoptionsdata.method=="DIAL"){

                    //Check permission
                    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    }else{
                        //Dial
                        val string =item.paymentoptionsdata.dial
                        val ussd = string.replace("#", Uri.encode("#"))
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: $ussd"))
                        context.startActivity(intent)

                    }
                }
                true
            }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun showMessageBox(context: Context, procedures: List<Procedure>){

        val messageBoxView = LayoutInflater.from(context).inflate(R.layout.message_box, null)
        val messageBoxBuilder = AlertDialog.Builder(context).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()

        messageBoxView.tvHeader.text ="Payment procedure"

        val arrayAdapter: ArrayAdapter<*>
        val array = arrayListOf<String>()
        for (procedure in procedures){
            array.add(procedure.procedure)
        }
        // access the listView from xml file
        arrayAdapter = ArrayAdapter(context , R.layout.list_item_view, array)
        messageBoxView.list_view.adapter = arrayAdapter
        messageBoxView.list_view.adapter = arrayAdapter
        messageBoxView.list_view.divider = null;

        //setting text values
        messageBoxView.imageView.setOnClickListener { messageBoxInstance.dismiss()}

    }

}
