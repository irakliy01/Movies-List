package com.irakliy.data

import java.io.Serializable
import java.util.*

/**
 * DTO for storing in cache
 *
 * @property data cache data
 * @property creationTime time when cache object was created
 */
data class MoviesCacheDto(
    val data: MoviesDto,
    val creationTime: Date,
) : Serializable

data class MoviesDto(
    val data: List<MovieDto>
) : Serializable

data class MovieDto(
    val id: Int,
    val title: String,
    val year: String,
    val genre: String,
    val poster: String,
) : Serializable
