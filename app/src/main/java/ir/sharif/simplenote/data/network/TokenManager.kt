package ir.sharif.simplenote.data.network

import android.content.SharedPreferences
import android.util.Log

class TokenManager(private val sharedPreferences: SharedPreferences) {
    
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
    
    fun saveTokens(accessToken: String, refreshToken: String) {
        Log.d("TokenManager", "Saving tokens")
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .putString(REFRESH_TOKEN_KEY, refreshToken)
            .apply()
    }
    
    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
    
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }
    
    fun clearTokens() {
        Log.d("TokenManager", "Clearing tokens")
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()
    }
    
    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}
