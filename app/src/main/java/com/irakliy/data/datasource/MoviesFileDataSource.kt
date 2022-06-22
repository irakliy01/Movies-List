package com.irakliy.data.datasource

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.irakliy.data.MoviesCacheDto
import com.irakliy.data.MoviesDto
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Maybe
import java.io.File
import java.time.Instant
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue

/**
 * Data source that fetches data from local file (cache)
 *
 * @property context [ApplicationContext]
 * @property gson [Gson]
 */
class MoviesFileDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson,
) : MoviesDataSource {


    /**
     * Fetches data from cache. If cache is expired (cache lifetime [CACHE_LIFETIME_MIN]) then it
     * will delete cache
     */
    override fun fetchMovies(): Maybe<MoviesDto> {
        return Maybe.create { emitter ->
            try {
                val cacheFile = File(context.cacheDir, CACHE_FILENAME)
                cacheFile.reader().use {
                    val cacheDto = gson.fromJson(it, MoviesCacheDto::class.java)
                    if (cacheDto != null) {
                        val diff: Long = cacheDto.creationTime.time - Date.from(Instant.now()).time
                        val seconds = diff / 1000
                        val minutes = seconds / 60
                        if (minutes.absoluteValue < CACHE_LIFETIME_MIN) {
                            emitter.onSuccess(cacheDto.data)
                            Log.d(TAG, "Found data in file cache.")
                        } else {
                            Log.d(TAG, "File cache is expired. Deleting file...")
                            cacheFile.delete()
                        }
                    } else {
                        Log.d(TAG, "Cache miss...")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            } finally {
                emitter.onComplete()
            }
        }
    }

    /**
     * Saves [moviesDto] into cache for [CACHE_LIFETIME_MIN] minutes
     *
     * @param moviesDto object to store
     */
    fun saveInFile(moviesDto: MoviesDto) {
        val data = MoviesCacheDto(
            data = moviesDto,
            creationTime = Date.from(Instant.now())
        )
        try {
            val file = File(context.cacheDir, CACHE_FILENAME)
            file.writer().use {
                gson.toJson(data, it)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    private companion object {
        const val CACHE_FILENAME = "movies_cache"
        const val CACHE_LIFETIME_MIN = 10L
        const val TAG = "MoviesFileDataStore"
    }

}