package com.tmdbclient.mvi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.widget.queryTextChangeEvents
import com.tmdbclient.mvi.R
import com.tmdbclient.mvi.viewmodel.PopularMoviesViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PopularMoviesFragment : BaseMvRxFragment() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val viewModel: PopularMoviesViewModel by fragmentViewModel()

    @Inject
    lateinit var adapter: PopularMoviesAdapter

    @Inject
    lateinit var assistedFactory: PopularMoviesViewModel.AssistedFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesRecycler.adapter = adapter
        search_view.queryTextChangeEvents()
            .skipInitialValue()
            .debounce(500, TimeUnit.MILLISECONDS)
            .map {
                it.queryText.toString()
            }.subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                viewModel.updateSearchString(it)
            }.disposeOnDestroy()
    }

    override fun invalidate() = withState(viewModel) { state ->
        adapter.updateMovieList(state.filteredMovieList.sortedBy { it.title })
    }

    private fun Disposable.disposeOnDestroy() {
        disposable.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

