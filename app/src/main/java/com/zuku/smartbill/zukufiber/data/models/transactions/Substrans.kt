import com.google.gson.annotations.SerializedName


data class Substrans (

	@SerializedName("tid") val tid : String,
	@SerializedName("subid") val subid : String,
	@SerializedName("descrip") val descrip : String,
	@SerializedName("amt") val amt : String,
	@SerializedName("tdate") val tdate : String,
	@SerializedName("service") val service : String,
	@SerializedName("db") val db : String
)