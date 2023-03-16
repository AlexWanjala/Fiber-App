import com.google.gson.annotations.SerializedName

data class Paymentoptionsdatalist (

	@SerializedName("paymentoptionsdata") val paymentoptionsdata : Paymentoptionsdata,
	@SerializedName("procedure") val procedure : List<Procedure>
)