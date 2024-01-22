package id.syafii.kupilchat.domain


import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("discovery_keys")
    val discoveryKeys: List<String?>?,
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("has_ever_logged_in")
    val hasEverLoggedIn: Boolean?,
    @SerializedName("is_online")
    val isOnline: Boolean?,
    @SerializedName("last_seen_at")
    val lastSeenAt: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("preferred_languages")
    val preferredLanguages: List<Any?>?,
    @SerializedName("profile_url")
    val profileUrl: String?,
    @SerializedName("user_id")
    val userId: String?
)