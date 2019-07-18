package com.farag.hamzamohamed.movieapp.RoomDataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Movies implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int dbId;

    @ColumnInfo(name = "poster")
    private String poster;

    @ColumnInfo(name = "releaseDate")
    private String releaseDate;

    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "voteAverage")
    private String voteAverage;

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}
