package ir.sharif.simplenote.di

import ir.sharif.simplenote.data.network.AuthInterceptor
import ir.sharif.simplenote.data.network.TokenAuthenticator
import ir.sharif.simplenote.data.services.AuthService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://192.168.6.63:8000/api/"
//    private const val BASE_URL = "http://10.0.2.2:8000/api/"
    private const val BASE_URL = "https://simple.darkube.app/api/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .authenticator(TokenAuthenticator())
        .build()

    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}


