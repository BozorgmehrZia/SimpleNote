package ir.sharif.simplenote.di

import ir.sharif.simplenote.data.repository.AuthRepository

object AuthRepositoryInstance {
    val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }
}