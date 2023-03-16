import com.google.gson.annotations.SerializedName

data class Paymentsoptions (

	@SerializedName("options") val options : String,
	@SerializedName("id") val id : Int,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("status") val status : Int
)