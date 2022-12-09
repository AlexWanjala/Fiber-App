import com.google.gson.annotations.SerializedName


data class Data (

	@SerializedName("subDetailsResponse") val subDetailsResponse : List<SubDetailsResponse>
)