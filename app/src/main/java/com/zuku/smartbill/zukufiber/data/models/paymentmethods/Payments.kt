import com.google.gson.annotations.SerializedName


data class Payments (

	@SerializedName("id") val id : Int,
	@SerializedName("payment") val payment : String,
	@SerializedName("method") val method : String,
	@SerializedName("dial") val dial : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("desc") val desc : String,
	@SerializedName("paymentid") val paymentid : String
)