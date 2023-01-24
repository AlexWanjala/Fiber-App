import com.google.gson.annotations.SerializedName



data class PaymentCategory (

	@SerializedName("id") val id : Int,
	@SerializedName("payment") val payment : String,
	@SerializedName("subdb") val subdb : String
)