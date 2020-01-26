package com.tmdbclient.mvi.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.tmdbclient.mvi.model.Movie
import com.tmdbclient.mvi.repository.PopularMoviesRepository
import com.tmdbclient.mvi.view.PopularMoviesFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

data class PopularMoviesViewState(val searchQuery:String, val movieList:List<Movie>): MvRxState

class PopularMoviesViewModel @AssistedInject constructor(
    private val moviesRepository:PopularMoviesRepository,
    @Assisted val initialState:PopularMoviesViewState = PopularMoviesViewState(
        searchQuery = "",
        movieList = listOf()
    )
): BaseMvRxViewModel<PopularMoviesViewState>(initialState) {
    private val disposable: CompositeDisposable = CompositeDisposable()

    //private val moviesRepository = PopularMoviesRepository()

    lateinit var searchQuery: Observable<String>

    init {
        moviesRepository.clearPopularMovies()
        Observable.combineLatest<List<Movie>, String, List<Movie>>(
            moviesRepository.getPopularMovies(),
            searchQuery.startWith(""),
            BiFunction { a: List<Movie>, b: String ->
                a.filter {
                    it.title?.startsWith(b, true) ?: false
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { movieList ->
                setState { copy(movieList = movieList) }
            }
            .disposeOnDestroy()
        requestPopularMovies()
    }

    fun requestPopularMovies(){
        Observable.intervalRange(1, 10, 0, 10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                moviesRepository.requestPopularMovies(it.toInt())
            }.disposeOnDestroy()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        moviesRepository.clear()
    }
    private fun Disposable.disposeOnDestroy() {
        disposable.add(this)
    }

    @AssistedInject.Factory
    interface AssistedFactory{
        fun create(initialState:PopularMoviesViewState): PopularMoviesViewModel
    }
    companion object : MvRxViewModelFactory<PopularMoviesViewModel, PopularMoviesViewState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: PopularMoviesViewState
        ): PopularMoviesViewModel? {
            val fragment =  (viewModelContext as FragmentViewModelContext).fragment as PopularMoviesFragment
            return fragment.assistedFactory.create(state)
        }
    }
}