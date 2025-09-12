package ir.sharif.simplenote.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ir.sharif.simplenote.data.model.Resource
import ir.sharif.simplenote.data.model.UserInfo
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository) :
    ViewModel() {
    private val _userState = MutableStateFlow<Resource<UserInfo>>(Resource.Loading)
    val userState: StateFlow<Resource<UserInfo>> = _userState.asStateFlow()

    fun loadUser() {
        viewModelScope.launch {
            try {
                _userState.value = Resource.Loading
                val response = authRepository.userInfo()

                if (response.isSuccessful) {
                    val body = response.body()
                    _userState.value = if (body == null) {
                        Resource.Error(parseErrorMessage(response))
                    } else {
                        Resource.Success(body.toUserInfo())
                    }
                } else {
                    _userState.value = Resource.Error(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                _userState.value = Resource.Error("Unknown error")
            }
        }
    }

    fun dismissError() {

    }

}