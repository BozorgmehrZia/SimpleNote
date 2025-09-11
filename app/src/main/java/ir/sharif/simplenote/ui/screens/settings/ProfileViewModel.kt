package ir.sharif.simplenote.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.model.Resource
import ir.sharif.simplenote.data.model.UserInfo
import ir.sharif.simplenote.data.repository.AuthRepository
import ir.sharif.simplenote.di.AuthRepositoryInstance
import ir.sharif.simplenote.util.parseErrorMessage
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository = AuthRepositoryInstance.authRepository) :
    ViewModel() {
    var userState by mutableStateOf<Resource<UserInfo>>(Resource.Loading)
        private set

    fun loadUser() {
        viewModelScope.launch {
            try {
                userState = Resource.Loading
                val response = authRepository.userInfo()

                if (response.isSuccessful) {
                    val body = response.body()
                    userState = if (body == null) {
                        Resource.Error(parseErrorMessage(response))
                    } else {
                        Resource.Success(body.toUserInfo())
                    }
                } else {
                    userState = Resource.Error(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                userState = Resource.Error("Unknown error")
            }
        }
    }

    fun dismissError() {

    }

}