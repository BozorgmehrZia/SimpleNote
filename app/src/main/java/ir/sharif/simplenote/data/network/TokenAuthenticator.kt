package ir.sharif.simplenote.data.network

import ir.sharif.simplenote.data.model.TokenStore
import ir.sharif.simplenote.di.AuthRepositoryInstance
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) { // Prevent multiple refresh calls

            if (responseCount(response) >= 2) return null // Prevent infinite loop

            val currentRefreshToken = TokenStore.refreshToken

            if (currentRefreshToken.isNullOrEmpty()) {
                TokenStore.clear()
                return null
            }

            val newTokens = runBlocking {
                try {
                    val refreshResponse = AuthRepositoryInstance.authRepository.refresh(currentRefreshToken)
                    TokenStore.accessToken = refreshResponse.body()?.access
                    refreshResponse
                } catch (e: Exception) {
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
