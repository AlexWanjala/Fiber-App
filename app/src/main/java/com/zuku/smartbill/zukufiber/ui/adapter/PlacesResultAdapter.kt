package com.zuku.smartbill.zukufiber.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.zuku.smartbill.zukufiber.R
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_place.view.*

class PlacesResultAdapter(var mContext: Context, val onClick: (prediction: AutocompletePrediction) -> Unit )
    :
    RecyclerView.Adapter<PlacesResultAdapter.ViewHolder>(), Filterable {
    private var mResultList: ArrayList<AutocompletePrediction>? = arrayListOf()
    private val placesClient: PlacesClient = Places.createClient(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            if(mResultList!!.size==0){
                Toast.makeText(mContext,"Kindly search the address again",Toast.LENGTH_SHORT).show()
            }else{
                if(mResultList?.get(position)==null){
                    Toast.makeText(mContext,"Kindly Search you address",Toast.LENGTH_SHORT).show()
                }else{
                    onClick(mResultList?.get(position)!!)
                }
            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val res = mResultList?.get(position)
            itemView.apply {
                tv_address_title.text = res?.getPrimaryText(null)
                tv_address_sub_title .text = res?.getSecondaryText(null)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                mResultList = getPredictions(constraint)
                if (mResultList != null) {
                    results.values = mResultList
                    results.count = mResultList!!.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {

            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        val result: ArrayList<AutocompletePrediction> = arrayListOf()
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                .setCountries("KE","UG","MW","TZ")
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                result.addAll(response.autocompletePredictions)
                notifyDataSetChanged()
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {

                }
            }
        return result
    }
}