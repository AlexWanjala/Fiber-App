import com.google.gson.annotations.SerializedName

data class Paymethods (

	@SerializedName("paymentCategory") val paymentCategory : PaymentCategory,
	@SerializedName("payments") val payments : List<Payments>
)