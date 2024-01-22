package id.syafii.kupilchat.domain


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("font_color")
    val fontColor: String?,
    @SerializedName("font_preference")
    val fontPreference: String?
)