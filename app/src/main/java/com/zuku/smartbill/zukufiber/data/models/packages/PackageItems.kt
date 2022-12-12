import com.google.gson.annotations.SerializedName



data class PackageItems (

	@SerializedName("package") val packageName : String,
	@SerializedName("item") val item : String,
	@SerializedName("price") val price : Int,
	@SerializedName("currency") val currency : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("id") val id : Int,
	@SerializedName("des") val des : String
)