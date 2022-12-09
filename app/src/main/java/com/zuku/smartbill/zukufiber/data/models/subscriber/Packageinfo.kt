package com.zuku.smartbill.zukufiber.data.models.subscriber

import com.google.gson.annotations.SerializedName

data class Packageinfo (

	@SerializedName("pid") val pid : Int,
	@SerializedName("subid") val subid : Int,
	@SerializedName("billthru") val billthru : String,
	@SerializedName("dueamt") val dueamt : Int,
	@SerializedName("buckamt") val buckamt : Int,
	@SerializedName("currentpack") val currentpack : String,
	@SerializedName("lastpack") val lastpack : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("updatedate") val updatedate : String
)