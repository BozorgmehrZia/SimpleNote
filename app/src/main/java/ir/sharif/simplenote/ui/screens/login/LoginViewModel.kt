package ir.sharif.simplenote.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.model.TokenStore
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.data.model.UiState
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun login(username: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = authRepository.login(username, password)

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data == null) {
                        handleUnknownError(parseErrorMessage(response))
                    } else {
                        TokenStore.setFromLoginResponse(data)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                } else {
                    handleUnknownError(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                handleUnknownError()
            }
        }
    }

    private fun handleUnknownError(errorMessage: String = "Unknown error") {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = errorMessage
        )
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

}
