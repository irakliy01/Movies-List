package com.irakliy.data

import com.irakliy.data.datasource.MoviesFileDataSource
import com.irakliy.data.datasource.MoviesMemoryDataSource
import com.irakliy.data.datasource.MoviesRemoteDataSource
import io.mockk.*
import io.reactivex.rxjava3.core.Maybe
import org.junit.Test

internal class MoviesRepositoryImplTest {

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

    private val fileMoviesDto = MoviesDto(
        data = listOf(
            MovieDto(
                1,
                "FILE",
                "FILE",
                "FILE",
                "FILE",
            )
        )
    )

    private val memoryMoviesDto = MoviesDto(
        data = listOf(
            MovieDto(
                1,
                "MEMORY",
                "MEMORY",
                "MEMORY",
                "MEMORY",
            )
        )
    )


    @Test
    fun `fetch movies no cache`() {
        val moviesMemoryDataSource: MoviesMemoryDataSource = mockk(relaxed = true)
        val moviesFileDataSource: MoviesFileDataSource = mockk(relaxed = true)
        val moviesRemoteDataSource: MoviesRemoteDataSource = mockk(relaxed = true)

        every { moviesMemoryDataSource.fetchMovies() } returns Maybe.empty()
        every { moviesFileDataSource.fetchMovies() } returns Maybe.empty()
        every { moviesRemoteDataSource.fetchMovies() } returns Maybe.just(remoteMoviesDto)

        val moviesRepositoryImpl = MoviesRepositoryImpl(
            moviesMemoryDataSource,
            moviesFileDataSource,
            moviesRemoteDataSource
        )

        moviesRepositoryImpl.fetchMovies()
            .test()
            .assertNoErrors()
            .assertValue(remoteMoviesDto.data)

        verifySequence {
            moviesMemoryDataSource.fetchMovies()
            moviesFileDataSource.fetchMovies()
            moviesRemoteDataSource.fetchMovies()
            moviesMemoryDataSource.saveInMemory(remoteMoviesDto)
            moviesFileDataSource.saveInFile(remoteMoviesDto)
        }
        confirmVerified(moviesMemoryDataSource, moviesFileDataSource, moviesRemoteDataSource)
    }

    @Test
    fun `fetch movies cache in file`() {
        val moviesMemoryDataSource: MoviesMemoryDataSource = mockk(relaxed = true)
        val moviesFileDataSource: MoviesFileDataSource = mockk(relaxed = true)
        val moviesRemoteDataSource: MoviesRemoteDataSource = mockk(relaxed = true)

        every { moviesMemoryDataSource.fetchMovies() } returns Maybe.empty()
        every { moviesFileDataSource.fetchMovies() } returns Maybe.just(fileMoviesDto)
        every { moviesRemoteDataSource.fetchMovies() } returns Maybe.just(remoteMoviesDto)

        val moviesRepositoryImpl = MoviesRepositoryImpl(
            moviesMemoryDataSource,
            moviesFileDataSource,
            moviesRemoteDataSource
        )

        moviesRepositoryImpl.fetchMovies()
            .test()
            .assertNoErrors()
            .assertValue(fileMoviesDto.data)

        verifySequence {
            moviesMemoryDataSource.fetchMovies()
            moviesFileDataSource.fetchMovies()
            moviesRemoteDataSource.fetchMovies()
            moviesMemoryDataSource.saveInMemory(fileMoviesDto)
        }
        verify(exactly = 0) {
            moviesFileDataSource.saveInFile(any())
        }
        confirmVerified(moviesMemoryDataSource, moviesFileDataSource, moviesRemoteDataSource)
    }

    @Test
    fun `fetch movies cache in memory`() {
        val moviesMemoryDataSource: MoviesMemoryDataSource = mockk(relaxed = true)
        val moviesFileDataSource: MoviesFileDataSource = mockk(relaxed = true)
        val moviesRemoteDataSource: MoviesRemoteDataSource = mockk(relaxed = true)

        every { moviesMemoryDataSource.fetchMovies() } returns Maybe.just(memoryMoviesDto)
        every { moviesFileDataSource.fetchMovies() } returns Maybe.just(fileMoviesDto)
        every { moviesRemoteDataSource.fetchMovies() } returns Maybe.just(remoteMoviesDto)

        val moviesRepositoryImpl = MoviesRepositoryImpl(
            moviesMemoryDataSource,
            moviesFileDataSource,
            moviesRemoteDataSource
        )


        moviesRepositoryImpl.fetchMovies()
            .test()
            .assertNoErrors()
            .assertValue(memoryMoviesDto.data)

        verifySequence {
            moviesMemoryDataSource.fetchMovies()
            moviesFileDataSource.fetchMovies()
            moviesRemoteDataSource.fetchMovies()
        }
        verify(exactly = 0) {
            moviesMemoryDataSource.saveInMemory(any())
            moviesFileDataSource.saveInFile(any())
        }
        confirmVerified(moviesMemoryDataSource, moviesFileDataSource, moviesRemoteDataSource)
    }
}
