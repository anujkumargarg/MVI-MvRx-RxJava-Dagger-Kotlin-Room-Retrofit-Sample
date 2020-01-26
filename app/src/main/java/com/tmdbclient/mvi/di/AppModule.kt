package com.tmdbclient.mvi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.tmdbclient.mvi.App
import com.tmdbclient.mvi.Constants.BASE_URL
import com.tmdbclient.mvi.room.AppDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(app: App): AppDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java, "movies.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return requireNotNull(providerMap[modelClass]).get() as T
            }
        }
}