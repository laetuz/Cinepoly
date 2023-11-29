package com.neotica.core.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    val id: Int,
    val overview: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val popularity: Double,
    val voteAverage: Double,
    val title: String,
    val posterPath: String? = null,
): Parcelable
