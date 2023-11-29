package com.neotica.core.domain.repo

interface LoginRepository {
    suspend fun login(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onLoginFailed: () -> Unit
    )
}