package ir.sharif.simplenote.util

import android.util.Log
import com.google.gson.Gson
import ir.sharif.simplenote.data.model.ErrorResponse
import retrofit2.Response

fun <T> parseErrorMessage(response: Response<T>): String {
    return try {
        val errorJson = response.errorBody()?.string()
        Log.d("error response", errorJson?:"")
        val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
        errorResponse.errors.firstOrNull()?.detail ?: "Unknown error"
    } catch (e: Exception) {
        "Unknown error"
    }
}
