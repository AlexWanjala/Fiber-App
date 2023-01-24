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


class PaymentMethodsAdapter(private val context: Context, private val dataSet:  List<Paymethods>) :
        RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPaymentMethod: TextView
        val layoutPaymentMethod: LinearLayoutCompat


        init {
            // Define click listener for the ViewHolder's View.
            tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod)
            layoutPaymentMethod = view.findViewById(R.id.layoutPaymentMethod)

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
        viewHolder.tvPaymentMethod.text = dataSet[position].paymentCategory.payment
      //  viewHolder.tvDes.text = dataSet[position].desc
        viewHolder.layoutPaymentMethod.setOnClickListener {

            if(dataSet[position].payments.size>1){

                showDialog(context,dataSet[position].payments)

            }else{
                if (dataSet[position].payments[0].method=="PAYMENTS"){

                    context.startActivity(Intent(context, PaymentsActivity::class.java))

                }
            }

         /*   if(dataSet[position].method=="STK"){
                showDialog(context)
               *//* if (context is MainActivity) {
                    (context as MainActivity).stkPayments()

                }*//*
            }
            else if (dataSet[position].method=="SIMTOOLKIT"){

                launchSTK(context)

            }
            else if (dataSet[position].method=="PAYMENTS"){

                context.startActivity(Intent(context, Payments::class.java))

            }
            else if (dataSet[position].method=="DIAL"){

                val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)

                }else{

                    val string =dataSet[position].dial
                    val ussd = string.replace("#",Uri.encode("#"))
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: $ussd"))
                    context.startActivity(intent)
                }

            }
            else{
                Toast.makeText(context, dataSet[position].method+"Not available",Toast.LENGTH_LONG).show()
            }
*/
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun showDialog(activity: Context, payments : List<Payments>) {

        val dialog = Dialog(activity)
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)


        var recyclerView2: RecyclerView? = null
        recyclerView2 = dialog.findViewById(R.id.recyclerView2)

      var tvCancel: TextView? = null
        tvCancel = dialog.findViewById(R.id.tvCancel)
        tvCancel.setOnClickListener {  dialog.dismiss() }

        val adapter = SubPaymentMethodsAdapter(context,payments)
        recyclerView2?.adapter = adapter
        recyclerView2?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        dialog.show()

    }


}
