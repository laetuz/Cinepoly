package com.neotica.cinepoly.ui.register

import androidx.lifecycle.ViewModel
import com.neotica.core.data.repository.RegisterRepositoryImpl

class RegisterViewModel(
    private val repo: RegisterRepositoryImpl
): ViewModel() {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    suspend fun registerUser(username: String, email: String, pass: String, passConf: String){
        repo.register(username, email, pass, passConf)
    }
}