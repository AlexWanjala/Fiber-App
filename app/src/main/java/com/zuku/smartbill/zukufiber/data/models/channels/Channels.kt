import com.google.gson.annotations.SerializedName

data class Channels (
	@SerializedName("id") val id : Int,
	@SerializedName("channel_code") val channel_code : Int,
	@SerializedName("package") val packageName : String,
	@SerializedName("subdb") val subdb : String,
	@SerializedName("channel_name") val channel_name : String
)