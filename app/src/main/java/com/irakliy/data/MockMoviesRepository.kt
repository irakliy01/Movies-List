package com.irakliy.data

import com.irakliy.data.datasource.MoviesMockDataSource
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class MockMoviesRepository @Inject constructor(
    private val mockDataSource: MoviesMockDataSource,
) : MoviesRepository {

    private fun format(data: MoviesDto) = data.data

    override fun fetchMovies(): Maybe<List<MovieDto>> {
        return mockDataSource.fetchMovies().map(::format)
    }

}