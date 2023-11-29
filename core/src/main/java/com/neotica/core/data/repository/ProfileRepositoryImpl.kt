package com.neotica.core.data.repository

import androidx.lifecycle.LiveData
import com.neotica.core.common.PreferenceDefaults
import com.neotica.core.data.local.UserDao
import com.neotica.core.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.neotica.core.domain.repo.PreferenceRepository
import com.neotica.core.domain.repo.ProfileRepository

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val prefRepo: PreferenceRepository
): ProfileRepository {
    override suspend fun updateUser(
        userId: Int,
        username: String,
        email: String,
        password: String,
        fullname: String?,
        dob: String?,
        address: String?,
        token: String
    ) {
        withContext(Dispatchers.IO) {
            val userEntity = UserEntity(
                userId = userId,
                username = username,
                email = email,
                password = password,
                fullname = fullname,
                dob = dob,
                address = address,
                token = token
            )
            userDao.updateUser(userEntity)
        }
    }

    override suspend fun logout(
    ) {
        withContext(Dispatchers.IO) {
            prefRepo.setToken(PreferenceDefaults.TOKEN)
        }
    }

    fun getUsername(userId: String): LiveData<UserEntity> = userDao.getUsername(userId)
}