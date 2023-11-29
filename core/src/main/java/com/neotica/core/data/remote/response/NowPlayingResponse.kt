package com.neotica.core.data.remote.response

data class NowPlayingResponse (
    val page: Int,
    val totalPages: Int,
    val results: List<MovieItem>,
    val totalResults: Int
)