package com.irakliy.data.datasource

import com.irakliy.data.MoviesDto
import io.reactivex.rxjava3.core.Maybe

/**
 * Common movies data source interface
 */
interface MoviesDataSource {

    fun fetchMovies(): Maybe<MoviesDto>

}