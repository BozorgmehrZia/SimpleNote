package ir.sharif.simplenote.di

import android.content.Context
import ir.sharif.simplenote.data.network.HeaderInterceptor
import ir.sharif.simplenote.data.services.AuthService
import ir.sharif.simplenote.data.services.NoteService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://192.168.6.63:8000/api/"
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor { TokenManagerInstance.getTokenManager()?.getAccessToken() })
            .build()
    }

    private var _authApi: AuthService? = null
    private var _noteApi: NoteService? = null

    val authApi: AuthService
        get() = _authApi ?: throw IllegalStateException("RetrofitInstance not initialized. Call initialize() first.")

    val noteApi: NoteService
        get() = _noteApi ?: throw IllegalStateException("RetrofitInstance not initialized. Call initialize() first.")

    fun initialize(context: Context) {
        // Initialize TokenManagerInstance
        TokenManagerInstance.initialize(context)
        
        // Create API instances after tokenManager is initialized
        _authApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
            
        _noteApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteService::class.java)
    }
}


