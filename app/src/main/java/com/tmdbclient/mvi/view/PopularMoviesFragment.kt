package com.tmdbclient.mvi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.widget.queryTextChangeEvents
import com.tmdbclient.mvi.R
import com.tmdbclient.mvi.viewmodel.PopularMoviesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PopularMoviesFragment : BaseMvRxFragment() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    //@Inject
    lateinit var viewModel: PopularMoviesViewModel
    @Inject
    lateinit var adapter: PopularMoviesAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var assistedFactory: PopularMoviesViewModel.AssistedFactory

    //val adapter: PopularMoviesAdapter = PopularMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesRecycler.adapter = adapter
        viewModel = ViewModelProviders.of(this, factory)
            .get(PopularMoviesViewModel::class.java)
        viewModel.searchQuery = search_view.queryTextChangeEvents()
            .skipInitialValue()
            .debounce(500, TimeUnit.MILLISECONDS)
            .map {
                it.queryText.toString()
            }.subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun invalidate() = withState(viewModel){state ->
        adapter.updateMovieList(state.movieList.sortedBy { it.title })
    }

    private fun Disposable.disposeOnDestroy() {
        disposable.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

