package com.zuku.smartbill.zukufiber.data.models.response


import Appinfo
import Channels
import Invoices
import Packages
import PaymentData
import Paymethods
import Push
import Shops
import SubDetailsResponse
import Substrans
import com.google.gson.annotations.SerializedName


data class Data (
	@SerializedName("subDetailsResponse") val subDetailsResponse : List<SubDetailsResponse>,
	@SerializedName("Push") val push : Push,
	@SerializedName("packages") val packages : List<Packages>,
	@SerializedName("substrans") val substrans : List<Substrans>,
	@SerializedName("appinfo") val appinfo : Appinfo,
	@SerializedName("paymentData") val paymentData : List<PaymentData>,
	@SerializedName("paymethods") val paymethods : List<Paymethods>,
	@SerializedName("Invoices") val invoices : List<Invoices>,
	@SerializedName("channels") val channels : List<Channels>,
	@SerializedName("shops") val shops : List<Shops>
)