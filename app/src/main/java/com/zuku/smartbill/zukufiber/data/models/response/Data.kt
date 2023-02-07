package com.zuku.smartbill.zukufiber.data.models.response


import Appinfo
import Packages
import PaymentData
import Paymethods
import Push
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
	@SerializedName("paymethods") val paymethods : List<Paymethods>

)