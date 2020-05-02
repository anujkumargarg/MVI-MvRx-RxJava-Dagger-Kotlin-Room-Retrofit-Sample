package com.tmdbclient.mvi

import com.tmdbclient.mvi.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {

    private val appComponent by lazy {
        DaggerAppComponent.builder().injectApplication(this).build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return appComponent
    }

}