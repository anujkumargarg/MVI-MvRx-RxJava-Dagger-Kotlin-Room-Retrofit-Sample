package com.tmdbclient.mvi.di.movies

import com.tmdbclient.mvi.api.MoviesApi
import com.tmdbclient.mvi.room.AppDatabase
import com.tmdbclient.mvi.room.PopularMoviesDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MoviesModule {


    //@MovieScope
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    //@MovieScope
    @Provides
    fun providePopularMoviesDao(appDatabase: AppDatabase): PopularMoviesDao {
        return appDatabase.getPopularMoviesDao()
    }

//    @Provides
//    fun providePopularMoviesViewModel(
//        popularMoviesFragment: PopularMoviesFragment,
//        factory: ViewModelProvider.Factory
//    ): PopularMoviesViewModel {
//        return ViewModelProviders.of(popularMoviesFragment, factory)
//            .get(PopularMoviesViewModel::class.java)
//    }
}