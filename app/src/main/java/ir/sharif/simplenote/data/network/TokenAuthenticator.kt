package ir.sharif.simplenote.data.network

import android.util.Log
import ir.sharif.simplenote.data.model.TokenStore
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.di.TokenManagerInstance
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) { // Prevent multiple refresh calls
            Log.d("TokenAuthenticator", "Attempting to refresh token. Response code: ${response.code()}")

            if (responseCount(response) >= 2) {
                Log.d("TokenAuthenticator", "Too many refresh attempts, giving up")
                return null // Prevent infinite loop
            }

            val currentRefreshToken = TokenStore.refreshToken
            Log.d("TokenAuthenticator", "Current refresh token: ${currentRefreshToken?.take(20)}...")

            if (currentRefreshToken.isNullOrEmpty()) {
                Log.d("TokenAuthenticator", "No refresh token available, clearing tokens")
                TokenStore.clear()
                return null
            }

            val newTokens = runBlocking {
                try {
                    Log.d("TokenAuthenticator", "Calling refresh API...")
                    val refreshResponse = AuthRepositoryInstance.authRepository.refresh(currentRefreshToken)
                    Log.d("TokenAuthenticator", "Refresh response code: ${refreshResponse.code()}")
                    
                    if (refreshResponse.isSuccessful) {
                        val newAccessToken = refreshResponse.body()?.access
                        Log.d("TokenAuthenticator", "New access token received: ${newAccessToken?.take(20)}...")
                        
                        if (newAccessToken != null) {
                            // Save new access token, keep the same refresh token
                            TokenStore.accessToken = newAccessToken
                            TokenStore.refreshToken = currentRefreshToken
                            TokenManagerInstance.getTokenManager()?.saveTokens(newAccessToken, currentRefreshToken)
                            Log.d("TokenAuthenticator", "Tokens saved successfully")
                        }
                    } else {
                        Log.e("TokenAuthenticator", "Refresh failed: ${refreshResponse.code()} - ${refreshResponse.errorBody()?.string()}")
                    }
                    refreshResponse
                } catch (e: Exception) {
                    Log.e("TokenAuthenticator", "Exception during refresh: ${e.message}", e)
                    null
                }
            }

            return newTokens?.let {
                response.request().newBuilder()
                    .header("Authorization", "Bearer ${it.body()?.access}")
                    .build()
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var res = response
        while (res.priorResponse() != null) {
            count++
            res = res.priorResponse()!!
        }
        return count
    }
}
