package com.zuku.smartbill.zukufiber.ui.adapter

import PaymentData
import Paymethods
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.launchSTK
import com.zuku.smartbill.zukufiber.ui.MainActivity
import com.zuku.smartbill.zukufiber.ui.PackagesActivity
import com.zuku.smartbill.zukufiber.ui.Payments


class PaymentMethodsAdapter(private val context: Context, private val dataSet:  List<Paymethods>) :
        RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPaymentMethod: TextView
        val tvDes: TextView
        val layoutPaymentMethod: LinearLayoutCompat


        init {
            // Define click listener for the ViewHolder's View.
            tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod)
            layoutPaymentMethod = view.findViewById(R.id.layoutPaymentMethod)
            tvDes = view.findViewById(R.id.tvDes)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_payment_methods, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvPaymentMethod.text = dataSet[position].payment
        viewHolder.tvDes.text = dataSet[position].desc
        viewHolder.layoutPaymentMethod.setOnClickListener {

            if(dataSet[position].method=="STK"){
                if (context is MainActivity) {
                    (context as MainActivity).stkPayments()

                }
            }else if (dataSet[position].method=="SIMTOOLKIT"){

                launchSTK(context)

            }else if (dataSet[position].method=="PAYMENTS"){

                context.startActivity(Intent(context, Payments::class.java))

            }else if (dataSet[position].method=="DIAL"){

                val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // Log.i(TAG, "Permission to record denied")
                    Toast.makeText(context,"Permission to record denied",Toast.LENGTH_LONG).show()

                    requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)

                }else{

                    val callIntent = Intent(Intent.ACTION_CALL,   (context as MainActivity).ussdToCallableUri(dataSet[position].dial))
                    context.startActivity(callIntent)
                    /*val ussd = "*544*2" + Uri.encode("#")
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: $ussd"))
                    startActivity(intent)*/
                }

            }else{
                Toast.makeText(context, dataSet[position].method+"Not available",Toast.LENGTH_LONG).show()
            }


        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
