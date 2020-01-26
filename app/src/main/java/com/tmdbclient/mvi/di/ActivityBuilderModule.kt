package com.tmdbclient.mvi.di

import com.tmdbclient.mvi.view.PopularMoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributePopularMoviesFragment(): PopularMoviesFragment
}