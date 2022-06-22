package com.irakliy.data.datasource

import android.util.Log
import com.irakliy.data.MoviesApi
import com.irakliy.data.MoviesDto
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * Data source that fetches data from network using [moviesApi]
 *
 * @property moviesApi API for fetching movies
 */
class MoviesRemoteDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) : MoviesDataSource {

    override fun fetchMovies(): Maybe<MoviesDto> {
        Log.d(TAG, "Fetching data from the remote...")
        return moviesApi.fetchMovies()
    }

    private companion object {
        const val TAG = "MoviesRemoteDataStore"
    }

}