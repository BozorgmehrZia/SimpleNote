package ir.sharif.simplenote.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import ir.sharif.simplenote.data.model.Resource
import ir.sharif.simplenote.data.model.UserInfo
import ir.sharif.simplenote.data.model.TokenStore
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.NoteRepositoryInstance
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository) :
    ViewModel() {
    private val _userState = MutableStateFlow<Resource<UserInfo>>(Resource.Loading)
    val userState: StateFlow<Resource<UserInfo>> = _userState.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        // Load user info from local database first (offline-first approach)
        loadUserOffline()
    }

    private fun loadUserOffline() {
        viewModelScope.launch {
            try {
                val localUserInfo = authRepository.getUserInfoOffline()
                if (localUserInfo != null) {
                    _userState.value = Resource.Success(localUserInfo)
                } else {
                    _userState.value = Resource.Error("No user data available offline")
                }
            } catch (e: Exception) {
                _userState.value = Resource.Error("Error loading offline user data")
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                
                // Try to fetch from server
                val response = authRepository.userInfo()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val userInfo = body.toUserInfo()
                        // Save to local database
                        authRepository.saveUserInfoOffline(userInfo, isSynced = true)
                        _userState.value = Resource.Success(userInfo)
                    } else {
                        _userState.value = Resource.Error(parseErrorMessage(response))
                    }
                } else {
                    // If server request fails, show offline data if available
                    val localUserInfo = authRepository.getUserInfoOffline()
                    if (localUserInfo != null) {
                        _userState.value = Resource.Success(localUserInfo)
                    } else {
                        _userState.value = Resource.Error(parseErrorMessage(response))
                    }
                }
            } catch (e: Exception) {
                // If network error, show offline data if available
                val localUserInfo = authRepository.getUserInfoOffline()
                if (localUserInfo != null) {
                    _userState.value = Resource.Success(localUserInfo)
                } else {
                    _userState.value = Resource.Error("Network error - no offline data available")
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    
    fun refreshUser() {
        loadUser()
    }

    fun dismissError() {
        // Load offline data when dismissing error
        loadUserOffline()
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                // Clear current user from NoteRepository
                NoteRepositoryInstance.noteRepository.setCurrentUser(null)
                // Clear tokens using TokenStore
                TokenStore.clear()
                // Note: We don't clear notes or user info on logout - they should persist for the same user
                Log.d("ProfileViewModel", "User logged out - notes and user info preserved for same user")
            } catch (e: Exception) {
                // Log error but continue with logout
            }
        }
    }
}