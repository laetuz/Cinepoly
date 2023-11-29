package com.neotica.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotica.core.common.PreferenceDefaults
import com.neotica.core.data.local.UserEntity
import com.neotica.core.data.remote.ApiResult
import com.neotica.core.data.remote.model.MovieModel
import com.neotica.core.data.repository.MainRepositoryImpl
import com.neotica.core.data.repository.PreferenceRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val mainRepo: MainRepositoryImpl,
    private val authRepo: PreferenceRepositoryImpl
) : ViewModel() {
    val token = authRepo.getToken()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PreferenceDefaults.TOKEN,
        )

    suspend fun getNowPlaying(): Flow<ApiResult<List<MovieModel>>> =
        mainRepo.getNowPlaying().flowOn(Dispatchers.IO)

    fun getUsername(userId: String): LiveData<UserEntity> = mainRepo.getUsername(userId)

    suspend fun clearToken() {
        authRepo.clearToken()
    }
}