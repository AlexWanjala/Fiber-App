import com.google.gson.annotations.SerializedName

data class Procedure (

	@SerializedName("id") val id : Int,
	@SerializedName("optionid") val optionid : String,
	@SerializedName("procedure") val procedure : String,
	@SerializedName("paymentOption") val paymentOption : String
)