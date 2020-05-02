package com.tmdbclient.mvi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelConfigFactory
import com.tmdbclient.mvi.view.PopularMoviesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MvRx.viewModelConfigFactory = MvRxViewModelConfigFactory(applicationContext)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PopularMoviesFragment(), "PopularMovies")
                .commit()
        }
    }
}
