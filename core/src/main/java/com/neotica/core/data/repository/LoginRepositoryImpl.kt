package com.neotica.core.data.repository

import android.util.Log
import com.neotica.core.data.local.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.neotica.core.domain.repo.LoginRepository
import com.neotica.core.domain.repo.PreferenceRepository

class LoginRepositoryImpl(
    private val dao: UserDao,
    private val authRepository: PreferenceRepository,
): LoginRepository {
    private val TAG = "LoginRepository"

    override suspend fun login(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onLoginFailed: () -> Unit,
        ) {
        withContext(Dispatchers.IO) {
            val dao = dao.getUser(email, password)
            if (dao != null) {
                Log.d(TAG, "login succeeded")
                Log.d(TAG, dao.toString())
                authRepository.setToken(dao.token)
                onLoginSuccess()
            } else {
                Log.d(TAG, "login failed")
                onLoginFailed()
            }
        }
    }
}