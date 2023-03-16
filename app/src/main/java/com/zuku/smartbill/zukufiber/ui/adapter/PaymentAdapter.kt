package com.zuku.smartbill.zukufiber.ui.adapter

import PaymentData
import Procedure
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.api
import com.zuku.smartbill.zukufiber.data.services.launchSTK
import com.zuku.smartbill.zukufiber.ui.MainActivity
import kotlinx.android.synthetic.main.activity_package_details.*
import kotlinx.android.synthetic.main.message_box.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
               /* viewHolder.tvView.setBackgroundColor(Color.parseColor("#D3DBDE"))
                viewHolder.layout.setBackgroundColor(Color.parseColor("#D3DBDE"))*/
            }
        }

        // use array-adapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val array = arrayListOf<String>()
        for (data in dataSet[position2].paymentoptionsdatalist){
            array.add(data.paymentoptionsdata.option)
        }
        // access the listView from xml file
        arrayAdapter = ArrayAdapter(context , R.layout.list_item_view, array)
        viewHolder.listView.adapter = arrayAdapter
        viewHolder.listView.divider = null;
        viewHolder.listView.setOnItemClickListener{ parent, view, index, id ->
            showMessageBox(context, dataSet[position2].paymentoptionsdatalist[index].procedure)
        }
        viewHolder.listView.onItemLongClickListener = object :OnItemLongClickListener{
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                index: Int,
                id: Long
            ): Boolean {
                if(dataSet[position2].paymentoptionsdatalist[index].paymentoptionsdata.method=="STK"){
                    if (context is MainActivity) {
                        (context as MainActivity).stkPayments(dataSet[position2].paymentoptionsdatalist[index].paymentoptionsdata.option)
                    }
                }
                else if (dataSet[position2].paymentoptionsdatalist[index].paymentoptionsdata.method=="SIMTOOLKIT"){

                    launchSTK(context)

                } else if (dataSet[position2].paymentoptionsdatalist[index].paymentoptionsdata.method=="DIAL"){

                    //Check permission
                    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    }else{
                        //Dial
                        val string =dataSet[position2].paymentoptionsdatalist[index].paymentoptionsdata.dial
                        val ussd = string.replace("#", Uri.encode("#"))
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: $ussd"))
                        context.startActivity(intent)

                    }
                }
                return true
            }

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    @RequiresApi(Build.VERSION_CODES.O)
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
