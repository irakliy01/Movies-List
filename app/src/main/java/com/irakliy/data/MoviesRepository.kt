package com.irakliy.data

import io.reactivex.rxjava3.core.Maybe

/**
 * Repository for work with movies
 */
interface MoviesRepository {

    /**
     * Fetch movies list
     */
    fun fetchMovies(): Maybe<List<MovieDto>>

}