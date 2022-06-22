package com.irakliy.data

import com.irakliy.data.datasource.MoviesFileDataSource
import com.irakliy.data.datasource.MoviesMemoryDataSource
import com.irakliy.data.datasource.MoviesRemoteDataSource
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
        private val moviesMemoryDataSource: MoviesMemoryDataSource,
        private val moviesFileDataSource: MoviesFileDataSource,
        private val moviesRemoteDataSource: MoviesRemoteDataSource,
) : MoviesRepository {

    private fun format(data: MoviesDto) = data.data

    override fun fetchMovies(): Maybe<List<MovieDto>> {
        return Maybe.concat(
            moviesMemoryDataSource.fetchMovies(),
            moviesFileDataSource.fetchMovies().doOnSuccess {
                moviesMemoryDataSource.saveInMemory(it)
            },
            moviesRemoteDataSource.fetchMovies().doOnSuccess {
                moviesMemoryDataSource.saveInMemory(it)
                moviesFileDataSource.saveInFile(it)
            }
        )
            .firstElement()
            .map(::format)
    }

}