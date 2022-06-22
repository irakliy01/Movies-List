package com.irakliy.data.datasource

import android.util.Log
import com.irakliy.data.MoviesDto
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * Data source that fetches data from memory cache
 */
class MoviesMemoryDataSource @Inject constructor() : MoviesDataSource {

    private var data: MoviesDto? = null

    /**
     * Fetches data from memory cache
     */
    override fun fetchMovies(): Maybe<MoviesDto> {
        return Maybe.create { emitter ->
            data?.let {
                emitter.onSuccess(it)
                Log.d(TAG, "Found data in memory cache.")
            } ?: run {
                Log.d(TAG, "Cache miss...")
            }
            emitter.onComplete()
        }
    }

    /**
     * Saves [moviesDto] into cache
     *
     * @param moviesDto object to store
     */
    fun saveInMemory(moviesDto: MoviesDto) {
        this.data = moviesDto.copy()
    }

    private companion object {
        const val TAG = "MoviesMemoryDataStore"
    }

}