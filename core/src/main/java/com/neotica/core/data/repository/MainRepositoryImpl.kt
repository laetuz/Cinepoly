package com.neotica.core.data.repository

import androidx.lifecycle.LiveData
import com.neotica.core.data.local.UserDao
import com.neotica.core.data.local.UserEntity
import com.neotica.core.data.remote.ApiResult
import com.neotica.core.data.remote.RemoteDataSource
import com.neotica.core.data.remote.model.MovieModel
import kotlinx.coroutines.flow.Flow
import com.neotica.core.domain.repo.MainRepository

class MainRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val userDao: UserDao,
): MainRepository {
    override suspend fun getNowPlaying(): Flow<ApiResult<List<MovieModel>>> {
        return remoteDataSource.getNowPlaying()
    }

    override fun getUsername(userId: String): LiveData<UserEntity> = userDao.getUsername(userId)
}