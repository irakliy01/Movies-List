package com.irakliy.data

import io.reactivex.rxjava3.core.Maybe
import retrofit2.http.GET

interface MoviesApi {

    @GET("movies")
    fun fetchMovies(): Maybe<MoviesDto>

    companion object {
        const val BASE_URL = "https://movies-sample.herokuapp.com/api/"
    }
}