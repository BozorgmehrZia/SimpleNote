package ir.sharif.simplenote.data.model

import ir.sharif.simplenote.di.TokenManagerInstance

object TokenStore {
    var accessToken: String? = null
        get() = field ?: TokenManagerInstance.getTokenManager()?.getAccessToken()
        set(value) {
            field = value
        }
    
    var refreshToken: String? = null
        get() = field ?: TokenManagerInstance.getTokenManager()?.getRefreshToken()
        set(value) {
            field = value
        }

    fun clear() {
        accessToken = null
        refreshToken = null
        TokenManagerInstance.getTokenManager()?.clearTokens()
    }

    fun setFromLoginResponse(response: LoginResponse) {
        accessToken = response.access
        refreshToken = response.refresh
        // Also save to TokenManager for persistence
        TokenManagerInstance.getTokenManager()?.saveTokens(response.access, response.refresh)
    }
    
    fun saveTokens(access: String, refresh: String) {
        accessToken = access
        refreshToken = refresh
        TokenManagerInstance.getTokenManager()?.saveTokens(access, refresh)
    }
}
