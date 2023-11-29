package com.neotica.core.domain.repo

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getToken(): Flow<String>

    suspend fun setToken(value: String)

    suspend fun clearToken()
}