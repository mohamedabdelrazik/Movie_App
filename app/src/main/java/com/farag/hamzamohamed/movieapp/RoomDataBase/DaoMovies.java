package com.farag.hamzamohamed.movieapp.RoomDataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoMovies {

    @Query("SELECT * FROM movies")
    List<Movies> getAll();

    @Insert
    void insert(Movies movies);

    @Delete
    void delete(Movies movies);

    @Update
    void update(Movies movies);
}
