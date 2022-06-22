package com.irakliy.domain

import com.irakliy.data.MovieDto
import com.irakliy.data.MoviesRepository
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * UseCase for work with movies
 *
 * @property moviesRepository repository from which movies will be fetched
 */
class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    private fun formatData(data: List<MovieDto>) = data.asSequence()
        .map {
            MovieModel(
                id = it.id,
                title = it.title,
                subtitle = it.genre,
                supportingText = it.year,
                imgUrl = it.poster,
            )
        }.toList()

    /**
     * Fetch movies
     */
    operator fun invoke(): Maybe<List<MovieModel>> {
        return moviesRepository.fetchMovies().map(::formatData)
    }

}