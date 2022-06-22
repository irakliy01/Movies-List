package com.irakliy.domain

import com.irakliy.data.MovieDto
import com.irakliy.data.MoviesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Maybe
import org.junit.Test


internal class GetMoviesUseCaseTest {

    private val moviesDtoList = listOf(
        MovieDto(
            1,
            "TEST",
            "TEST",
            "TEST",
            "TEST",
        )
    )

    private val movieModelList = listOf(
        MovieModel(
            1,
            "TEST",
            "TEST",
            "TEST",
            "TEST",
        )
    )

    @Test
    fun invokeTest() {
        val moviesRepository: MoviesRepository = mockk(relaxed = true)
        every { moviesRepository.fetchMovies() } returns Maybe.just(moviesDtoList)

        val getMoviesUseCase = GetMoviesUseCase(moviesRepository)

        getMoviesUseCase.invoke()
            .test()
            .assertNoErrors()
            .assertValue(movieModelList)

        verify(exactly = 1) {
            moviesRepository.fetchMovies()
        }
        confirmVerified(moviesRepository)
    }
}