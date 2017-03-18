package com.codepath.flickster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;
import static com.codepath.flickster.R.id.ivImage;
import static com.codepath.flickster.R.id.tvSynopsis;
import static com.codepath.flickster.R.id.tvTitle;
import static com.codepath.flickster.R.id.youtubeFragment;

/**
 * Created by John on 3/14/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String ID_MOVIE_DETAIL = "movie_detail";
    public static final String ID_MOVIE_ID = "movie_id";
    public static final String YT_API_KEY = "AIzaSyBzlhgNuTvImemM1YUM26EispPYW4BupLo";
    public static final String YT_TRAILER_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    Movie movie;
    private TextView tvTitle;
    private TextView tvOverview;
    RatingBar rbRating;
    public JSONArray movieJsonResults = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvSynopsis);
        rbRating = (RatingBar) findViewById(R.id.rbRating);

        // Get the passed variable from Intent
        movie = (Movie) getIntent().getSerializableExtra(ID_MOVIE_DETAIL);

        loadMovieDetails(movie);
        cueYoutubeTrailer();
    }

    // Populate the data for the movie
    public void loadMovieDetails(Movie movie) {

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
        rbRating.setRating(movie.getVoteAverage() / 2);
    }

    public void cueYoutubeTrailer() {

        Long id;
        String trailer;
        AsyncHttpClient client = new AsyncHttpClient();
        String key;

        id = getIntent().getLongExtra(ID_MOVIE_ID, 0);
        trailer = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + YT_TRAILER_API_KEY;

        client.get(trailer, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    movieJsonResults = response.getJSONArray("results");

                    YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                            getFragmentManager().findFragmentById(R.id.youtubeFragment);

                    youtubeFragment.initialize(YT_API_KEY,
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {

                                    // Cue video
                                    try {
                                        youTubePlayer.cueVideo(movieJsonResults.getJSONObject(0).getString("key"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
}


