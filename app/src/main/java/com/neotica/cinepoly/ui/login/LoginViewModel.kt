package com.neotica.cinepoly.ui.login

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.neotica.core.common.PreferenceDefaults
import com.neotica.core.data.repository.LoginRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.neotica.core.domain.repo.PreferenceRepository

class LoginViewModel(
    private val loginRepo: LoginRepositoryImpl,
    private val authRepository: PreferenceRepository,
): ViewModel() {
    val token = authRepository.getToken()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PreferenceDefaults.TOKEN,
        )

    fun setToken() {
        viewModelScope.launch {
            authRepository.setToken(token.value)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            authRepository.setToken("")
        }
    }

    fun navigate(fragment: Fragment) {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        NavHostFragment.findNavController(fragment).navigate(action)
    }

    fun login(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onLoginFailed: () -> Unit,
    ) {
        viewModelScope.launch {
            loginRepo.login(
                email = email,
                password = password,
                onLoginSuccess = onLoginSuccess,
                onLoginFailed = onLoginFailed,
            )
        }
    }
}