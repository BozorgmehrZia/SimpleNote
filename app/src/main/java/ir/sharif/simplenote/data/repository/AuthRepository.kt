package ir.sharif.simplenote.data.repository

import ir.sharif.simplenote.data.model.ChangePasswordRequest
import ir.sharif.simplenote.data.model.LoginRequest
import ir.sharif.simplenote.data.model.RefreshRequest
import ir.sharif.simplenote.data.model.RegisterRequest
import ir.sharif.simplenote.data.services.AuthService

class AuthRepository(
    private val authService: AuthService
) {
    suspend fun login(username: String, password: String) =
        authService.login(LoginRequest(username, password))

    suspend fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String
    ) = authService.register(RegisterRequest(username, password, email, firstName, lastName))

    suspend fun refresh(refresh: String) =
        authService.refresh(RefreshRequest(refresh))

    suspend fun userInfo() = authService.userInfo()

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ) = authService.changePassword(ChangePasswordRequest(oldPassword, newPassword))
}
