package com.neotica.core.domain.repo

import androidx.lifecycle.LiveData
import com.neotica.core.data.local.UserEntity
import com.neotica.core.data.remote.ApiResult
import com.neotica.core.data.remote.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getNowPlaying(): Flow<ApiResult<List<MovieModel>>>
    fun getUsername(userId: String): LiveData<UserEntity>
}