package com.zuku.smartbill.zukufiber.data.models.response


import Packages
import Push
import Substrans
import com.zuku.smartbill.zukufiber.data.models.subscriber.SubDetailsResponse
import com.google.gson.annotations.SerializedName


data class Data (

	@SerializedName("subDetailsResponse") val subDetailsResponse : List<SubDetailsResponse>,
	@SerializedName("Push") val push : Push,
	@SerializedName("packages") val packages : List<Packages>,
	@SerializedName("substrans") val substrans : List<Substrans>
)