package com.irakliy.hilt

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object GsonModule {

    @Provides
    @Reusable
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

}