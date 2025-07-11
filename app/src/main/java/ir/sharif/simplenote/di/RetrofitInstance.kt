package ir.sharif.simplenote.di

import ir.sharif.simplenote.data.network.HeaderInterceptor
import ir.sharif.simplenote.data.services.AuthService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://192.168.6.63:8000/api/"
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor { null })
        .build()

    val api: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}


