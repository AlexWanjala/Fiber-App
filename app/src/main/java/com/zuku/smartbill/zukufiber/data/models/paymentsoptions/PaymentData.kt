import com.google.gson.annotations.SerializedName

data class PaymentData (

	@SerializedName("paymentsoptions") val paymentsoptions : Paymentsoptions,
	@SerializedName("paymentoptionsdatalist") val paymentoptionsdatalist : List<Paymentoptionsdatalist>
)