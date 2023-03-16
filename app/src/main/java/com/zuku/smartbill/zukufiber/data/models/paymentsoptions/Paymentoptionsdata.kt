import com.google.gson.annotations.SerializedName

data class Paymentoptionsdata (

	@SerializedName("id") val id : Int,
	@SerializedName("optionid") val optionid : String,
	@SerializedName("option") val option : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("method") val method : String,
	@SerializedName("dial") val dial : String,
	@SerializedName("paymentOption") val paymentOption : String
)