package ir.sharif.simplenote.di

import android.content.Context
import ir.sharif.simplenote.data.network.TokenManager

object TokenManagerInstance {
    private var tokenManager: TokenManager? = null

    fun initialize(context: Context) {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun getTokenManager(): TokenManager? = tokenManager
}
