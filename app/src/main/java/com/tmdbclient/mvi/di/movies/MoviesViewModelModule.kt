package com.tmdbclient.mvi.di.movies

import androidx.lifecycle.ViewModel
import com.tmdbclient.mvi.ViewModelKey
import com.tmdbclient.mvi.repository.PopularMoviesRepository
import com.tmdbclient.mvi.viewmodel.PopularMoviesViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MoviesViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    fun bindPopularMoviesViewModelIntoMap(moviesRepository: PopularMoviesRepository):ViewModel
            = PopularMoviesViewModel(moviesRepository)
}