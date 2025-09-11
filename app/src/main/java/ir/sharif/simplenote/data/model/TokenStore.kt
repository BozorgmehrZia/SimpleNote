package ir.sharif.simplenote.data.model

object TokenStore {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun clear() {
        accessToken = null
        refreshToken = null
    }

    fun setFromLoginResponse(response: LoginResponse) {
        accessToken = response.access
        refreshToken = response.refresh
    }
}
