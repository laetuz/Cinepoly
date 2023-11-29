package com.neotica.core.data.remote

import com.neotica.core.data.remote.response.NowPlayingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ) : NowPlayingResponse
}