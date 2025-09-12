package ir.sharif.simplenote.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.di.TokenManagerInstance
import ir.sharif.simplenote.data.model.UiState
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun login(username: String, password: String) {
        Log.d("LoginViewModel", "Attempting login for username: $username")
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Making API call...")
                val response = authRepository.login(username, password)
                
                Log.d("LoginViewModel", "Response code: ${response.code()}")
                Log.d("LoginViewModel", "Response successful: ${response.isSuccessful}")
                Log.d("LoginViewModel", "Response body: ${response.body()}")
                Log.d("LoginViewModel", "Response error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("LoginViewModel", "Login successful! Access token: ${data?.access?.take(20)}...")
                    
                    // Save tokens
                    data?.let { loginResponse ->
                        TokenManagerInstance.getTokenManager()?.saveTokens(loginResponse.access, loginResponse.refresh)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                } else {
                    val errorMessage = parseErrorMessage(response)
                    Log.e("LoginViewModel", "Login failed: $errorMessage")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message}"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

}
