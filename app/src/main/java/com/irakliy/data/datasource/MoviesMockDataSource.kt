package com.irakliy.data.datasource

import com.irakliy.data.MovieDto
import com.irakliy.data.MoviesDto
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * Data store that fetches mock data
 */
class MoviesMockDataSource @Inject constructor() : MoviesDataSource {

    /**
     * Fetch mock data
     */
    override fun fetchMovies(): Maybe<MoviesDto> {
        return Maybe.just(
            MoviesDto(
                data = listOf(
                    MovieDto(
                        id = 912312,
                        title = "Dunkirk",
                        year = "2017",
                        genre = "History",
                        poster = "https://goo.gl/1zUyyq"
                    ),
                    MovieDto(
                        id = 11241,
                        title = "Jumanji: welcome to the jungle",
                        year = "2017",
                        genre = "Action",
                        poster = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"
                    ), MovieDto(
                        id = 46232,
                        title = "Star Wars: The Last Jedi",
                        year = "2017",
                        genre = "Fantasy",
                        poster = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/kOVEVeg59E0wsnXmF9nrh6OmWII.jpg"
                    ), MovieDto(
                        id = 5556,
                        title = "Blade Runner 2049",
                        year = "2017",
                        genre = "Mystery",
                        poster = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/gajva2L0rPYkEWjzgFlBXCAVBE5.jpg"
                    )
                )
            )
        )
    }

}