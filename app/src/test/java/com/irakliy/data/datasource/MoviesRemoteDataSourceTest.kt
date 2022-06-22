package com.irakliy.data.datasource

import android.util.Log
import com.irakliy.data.MovieDto
import com.irakliy.data.MoviesApi
import com.irakliy.data.MoviesDto
import io.mockk.*
import io.reactivex.rxjava3.core.Maybe
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class MoviesRemoteDataSourceTest {

    private val remoteMoviesDto = MoviesDto(
        data = listOf(
            MovieDto(
                1,
                "REMOTE",
                "REMOTE",
                "REMOTE",
                "REMOTE",
            )
        )
    )

    @Before
    fun before() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun fetchMoviesTest() {
        val moviesApi: MoviesApi = mockk()
        every { moviesApi.fetchMovies() } returns Maybe.just(remoteMoviesDto)

        val moviesRemoteDataSource = MoviesRemoteDataSource(moviesApi)

        moviesRemoteDataSource.fetchMovies()
            .test()
            .assertNoErrors()
            .assertValue(remoteMoviesDto)

        verify(exactly = 1) {
            moviesApi.fetchMovies()
        }
        confirmVerified(moviesApi)
    }
}