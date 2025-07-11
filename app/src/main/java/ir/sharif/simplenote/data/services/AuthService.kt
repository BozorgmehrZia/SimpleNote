package ir.sharif.simplenote.data.services

import ir.sharif.simplenote.data.model.LoginRequest
import ir.sharif.simplenote.data.model.RefreshRequest
import ir.sharif.simplenote.data.model.RegisterRequest
import ir.sharif.simplenote.data.model.LoginResponse
import ir.sharif.simplenote.data.model.RefreshResponse
import ir.sharif.simplenote.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body request: RefreshRequest): Response<RefreshResponse>
}
