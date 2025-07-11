package ir.sharif.simplenote.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.data.model.UiState
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun register(username: String,
                 email: String,
                 firstName: String,
                 lastName: String,
                 password: String,
                 passwordRetype: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            if (password != passwordRetype) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Passwords mismatch"
                )
            }
            val response = authRepository.register(username, password, email, firstName, lastName)

            if (response.isSuccessful) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = parseErrorMessage(response)
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

}