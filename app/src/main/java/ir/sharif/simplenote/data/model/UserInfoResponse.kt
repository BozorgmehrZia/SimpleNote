package ir.sharif.simplenote.data.model

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
) {
    fun toUserInfo(): UserInfo {
        return UserInfo(email, "$firstName $lastName", username)
    }
}
