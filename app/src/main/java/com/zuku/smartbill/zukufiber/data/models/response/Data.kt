package com.zuku.smartbill.zukufiber.data.models.response

import com.zuku.smartbill.zukufiber.data.models.subscriber.SubDetailsResponse
import com.google.gson.annotations.SerializedName


data class Data (

	@SerializedName("subDetailsResponse") val subDetailsResponse : List<SubDetailsResponse>
)