package com.zuku.smartbill.zukufiber.data.models.subscriber

import com.google.gson.annotations.SerializedName

data class SubDetailsResponse (

	@SerializedName("packageinfo") val packageinfo : Packageinfo,
	@SerializedName("subdetails") val subdetails : Subdetails
)