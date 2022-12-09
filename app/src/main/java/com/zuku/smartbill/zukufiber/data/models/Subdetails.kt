import com.google.gson.annotations.SerializedName


data class Subdetails (

	@SerializedName("sid") val sid : Int,
	@SerializedName("subid") val subid : Int,
	@SerializedName("subname") val subname : String,
	@SerializedName("substatus") val substatus : String,
	@SerializedName("subaddress") val subaddress : String,
	@SerializedName("subapt") val subapt : String,
	@SerializedName("subcity") val subcity : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("supdate") val supdate : String
)