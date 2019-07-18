package com.farag.hamzamohamed.movieapp.RoomDataBase;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DataBaseClient {

    private Context context;
    private static DataBaseClient dataBaseClient;

    //our app database object
    private MoviesDataBase moviesDataBase;

    private DataBaseClient(Context context){
        this.context = context;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        moviesDataBase = Room.databaseBuilder(context,MoviesDataBase.class,"MyDataBase").build();
    }

    public static synchronized DataBaseClient getInstance(Context context){

        if (dataBaseClient == null){
            dataBaseClient =new DataBaseClient(context);
        }
        return dataBaseClient;
    }

    public MoviesDataBase getMoviesDataBase(){
        return moviesDataBase;
    }
}
