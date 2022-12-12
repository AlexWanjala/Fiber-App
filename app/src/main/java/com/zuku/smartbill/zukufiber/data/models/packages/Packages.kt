import com.google.gson.annotations.SerializedName


data class Packages (

	@SerializedName("packageName") val packageName : String,
	@SerializedName("packageItems") val packageItems : List<PackageItems>
)