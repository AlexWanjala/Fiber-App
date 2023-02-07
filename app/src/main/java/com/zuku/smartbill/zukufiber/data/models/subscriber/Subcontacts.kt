import com.google.gson.annotations.SerializedName
data class Subcontacts (

	@SerializedName("cid") val cid : String,
	@SerializedName("subid") val subid : String,
	@SerializedName("zukuphone") val zukuphone : String,
	@SerializedName("cellcont") val cellcont : String,
	@SerializedName("emcont") val emcont : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("taxpin") val taxpin : String
)