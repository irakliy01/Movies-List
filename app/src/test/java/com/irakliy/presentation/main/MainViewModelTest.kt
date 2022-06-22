package com.irakliy.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.irakliy.domain.GetMoviesUseCase
import com.irakliy.domain.MovieModel
import io.mockk.*
import io.reactivex.rxjava3.core.Maybe
import org.junit.Rule
import org.junit.Test


internal class MainViewModelTest {

    private val movieModelList = listOf(
        MovieModel(
            1,
            "TEST",
            "TEST",
            "TEST",
            "TEST",
        )
    )

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadMoviesTest() {
        val getMoviesUseCase: GetMoviesUseCase = mockk(relaxed = true)
        every { getMoviesUseCase.invoke() } returns Maybe.just(movieModelList)

        val mockedObserver: Observer<List<MovieModel>> = spyk()
        val mainViewModel = MainViewModel(getMoviesUseCase)
        val slot = slot<List<MovieModel>>()

        assert(mainViewModel.srcList.isNullOrEmpty())

        mainViewModel.getMovies.observeForever(mockedObserver)

        mainViewModel.loadMovies()

        verifySequence {
            getMoviesUseCase.invoke()
            mockedObserver.onChanged(capture(slot))
        }
        assert(slot.captured == movieModelList)
        assert(mainViewModel.srcList == movieModelList)
    }
}