package com.neotica.core.common

import com.neotica.core.data.remote.model.MovieModel
import com.neotica.core.data.remote.response.MovieItem

object DataMapper {
    fun mapNowPlayingResponseToDomain(data: List<MovieItem>): List<MovieModel> =
        data.map { response ->
            MovieModel(
                overview = response.overview,
                title = response.title,
                posterPath = response.posterPath,
                backdropPath = response.backdropPath,
                releaseDate = response.releaseDate,
                popularity = response.popularity,
                voteAverage = response.voteAverage,
                id = response.id
            )
        }
}