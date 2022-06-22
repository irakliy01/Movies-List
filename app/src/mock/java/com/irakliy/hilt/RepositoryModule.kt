package com.irakliy.hilt

import com.irakliy.data.MockMoviesRepository
import com.irakliy.data.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsMoviesRepository(
        impl: MockMoviesRepository
    ): MoviesRepository

}