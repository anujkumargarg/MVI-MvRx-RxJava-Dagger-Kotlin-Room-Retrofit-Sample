package com.tmdbclient.mvi.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tmdbclient.mvi.model.Movie
import io.reactivex.Observable

@Dao
interface PopularMoviesDao {

    @Query("select * from movies")
    fun getPopularMovies():Observable<List<Movie>> //TODO check if List or not

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPopularMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAll()
}