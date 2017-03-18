package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by John on 3/13/2017.
 */

public class Movie implements Serializable {

    public String posterPath;
    public String backdropPath;
    public String originalTitle;
    public String overview;
    public float voteAverage;
    public long id;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = Float.parseFloat(jsonObject.getString("vote_average"));
        this.id = jsonObject.getLong("id");
    }

    public enum MoviePopularity {
        POPULAR_MOVIE, NOT_POPULAR_MOVIE
    }

    public static MoviePopularity moviePopularity;


    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w1280/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public long getId() {
        return id;
    }


    public static ArrayList<Movie> fromJASONArray (JSONArray jsonArray) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
