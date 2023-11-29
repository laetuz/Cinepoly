package com.neotica.core.domain.repo

interface ProfileRepository {
    suspend fun updateUser(
        userId: Int,
        username: String,
        email: String,
        password: String,
        fullname: String? = null,
        dob: String? = null,
        address: String? = null,
        token: String
    )
    suspend fun logout()
}