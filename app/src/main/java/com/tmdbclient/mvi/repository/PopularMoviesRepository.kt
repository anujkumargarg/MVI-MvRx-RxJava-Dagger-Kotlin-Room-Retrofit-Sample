package com.tmdbclient.mvi.repository

import com.tmdbclient.mvi.api.MoviesApi
import com.tmdbclient.mvi.model.Movie
import com.tmdbclient.mvi.room.PopularMoviesDao
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PopularMoviesRepository @Inject constructor(
    private val moviesApi: MoviesApi,
    private val popularMoviesDao: PopularMoviesDao
) {

    private val disposables = CompositeDisposable()
    //private val popularMoviesApi = RetrofitBuilder.getPopularMoviesService()
    //private val popularMoviesDao: PopularMoviesDao = App.instance.database.getPopularMoviesDao()

    fun getPopularMovies(): Observable<List<Movie>> {
        return popularMoviesDao.getPopularMovies()
    }

    fun clearPopularMovies() {
        Observable.fromCallable {
             popularMoviesDao.deleteAll()
        }.subscribeOn(Schedulers.io()).subscribe{

        }.disposeOnClear()
    }

    fun requestPopularMovies(page: Int) {
        moviesApi.getPopularMovies(API_KEY, page).map {
            it.results
        }.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
            it?.let { movies ->
                popularMoviesDao.insertPopularMovies(movies)
            }
        }.disposeOnClear()
    }

    companion object {
        const val API_KEY = "API_KEY"
    }

    fun clear() {
        disposables.clear()
    }

    private fun Disposable.disposeOnClear() {
        disposables.add(this)
    }
}

