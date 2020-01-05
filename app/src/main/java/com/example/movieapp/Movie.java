package com.example.movieapp;

import android.widget.Switch;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie  {

    @SerializedName("popularity")
    private float popularity;
    @SerializedName("vote_count")
    private long vote_count;
    @SerializedName("video")
    private boolean video;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("id")
    private int id;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private Double vote_average;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private Date release_date;


    public float getPopularity() {
        return popularity;
    }

    public long getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public Date getRelease_date() {
        return release_date;
    }


}
