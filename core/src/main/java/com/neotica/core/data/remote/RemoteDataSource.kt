package com.neotica.core.data.remote

import com.neotica.core.common.DataMapper
import com.neotica.core.common.Util.API_KEY
import com.neotica.core.data.remote.model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(
    private val apiService: ApiService
) {

    suspend fun getNowPlaying(): Flow<ApiResult<List<MovieModel>>> = flow {
        try {
            val response = apiService.getNowPlaying(
                apiKey = API_KEY,
            )
            val resultD = response.results
            val rawData = DataMapper.mapNowPlayingResponseToDomain(resultD)

            if (resultD.isNotEmpty()) {
                emit(ApiResult.Success(rawData))
            } else {
                emit(ApiResult.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}