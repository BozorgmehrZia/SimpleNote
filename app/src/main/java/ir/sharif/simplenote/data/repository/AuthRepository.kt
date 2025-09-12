package ir.sharif.simplenote.data.repository

import ir.sharif.simplenote.data.local.UserDao
import ir.sharif.simplenote.data.local.UserEntity
import ir.sharif.simplenote.data.model.ChangePasswordRequest
import ir.sharif.simplenote.data.model.LoginRequest
import ir.sharif.simplenote.data.model.RefreshRequest
import ir.sharif.simplenote.data.model.RegisterRequest
import ir.sharif.simplenote.data.model.UserInfo
import ir.sharif.simplenote.data.services.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val authService: AuthService,
    private val userDao: UserDao
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
    
    // Offline-first user info methods
    suspend fun getUserInfoOffline(): UserInfo? {
        return userDao.getUserInfo()?.toUserInfo()
    }
    
    fun getUserInfoFlow(): Flow<UserInfo?> {
        return userDao.getUserInfoFlow().map { it?.toUserInfo() }
    }
    
    suspend fun saveUserInfoOffline(userInfo: UserInfo, isSynced: Boolean = false) {
        val userEntity = UserEntity.fromUserInfo(userInfo, isSynced)
        userDao.insertUserInfo(userEntity)
    }
    
    suspend fun updateUserInfoSyncStatus(isSynced: Boolean) {
        userDao.updateSyncStatus(isSynced)
    }
    
    suspend fun clearUserInfo() {
        userDao.deleteUserInfo()
    }
    
    suspend fun getCurrentUserId(): String? {
        return userDao.getUserInfo()?.username
    }
}
