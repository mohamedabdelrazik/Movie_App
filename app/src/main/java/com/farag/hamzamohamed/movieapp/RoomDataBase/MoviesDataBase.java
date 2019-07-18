package com.farag.hamzamohamed.movieapp.RoomDataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Movies.class} , version = 1)
public abstract class MoviesDataBase extends RoomDatabase {

    public abstract DaoMovies daoMovies();
}
