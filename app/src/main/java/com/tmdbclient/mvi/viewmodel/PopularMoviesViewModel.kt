package com.tmdbclient.mvi.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.tmdbclient.mvi.model.Movie
import com.tmdbclient.mvi.repository.PopularMoviesRepository
import com.tmdbclient.mvi.view.PopularMoviesFragment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

data class PopularMoviesViewState(
    val searchQuery: String = "",
    val movieList: List<Movie> = listOf(),
    val filteredMovieList: List<Movie> = listOf()
) : MvRxState

class PopularMoviesViewModel @AssistedInject constructor(
    private val moviesRepository: PopularMoviesRepository,
    @Assisted val initialState: PopularMoviesViewState = PopularMoviesViewState()
) : BaseMvRxViewModel<PopularMoviesViewState>(initialState) {

    init {
        moviesRepository.clearPopularMovies()
        moviesRepository.getPopularMovies().subscribe {
            setState {
                copy(
                    movieList = it
                )
            }
        }.disposeOnClear()
        selectSubscribe(
            PopularMoviesViewState::searchQuery,
            PopularMoviesViewState::movieList
        ) { searchQuery, movieList ->
            setState {
                copy(
                    filteredMovieList = movieList.filter {
                        it.title?.startsWith(searchQuery, true) ?: false
                    }
                )
            }
        }
        requestPopularMovies()
    }

    private fun requestPopularMovies() {
        Observable.intervalRange(1, 10, 0, 10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                moviesRepository.requestPopularMovies(it.toInt())
            }.disposeOnClear()
    }

    override fun onCleared() {
        super.onCleared()
        moviesRepository.clear()
    }


    fun updateSearchString(searchString: String?) {
        setState {
            copy(searchQuery = searchString ?: "")
        }
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(initialState: PopularMoviesViewState): PopularMoviesViewModel
    }

    companion object : MvRxViewModelFactory<PopularMoviesViewModel, PopularMoviesViewState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: PopularMoviesViewState
        ): PopularMoviesViewModel? {
            val fragment =
                (viewModelContext as FragmentViewModelContext).fragment as PopularMoviesFragment
            return fragment.assistedFactory.create(state)
        }
    }
}