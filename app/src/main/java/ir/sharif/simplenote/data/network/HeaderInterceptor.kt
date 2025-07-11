package ir.sharif.simplenote.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        Log.d("request", original.toString())
        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .header("Content-Type", "application/json")
        tokenProvider()?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
