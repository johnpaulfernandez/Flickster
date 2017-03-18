package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.orientation;
import static android.R.attr.resource;
import static android.R.attr.type;
import static com.codepath.flickster.R.id.ivImage;
import static com.codepath.flickster.R.mipmap.ic_launcher;

/**
 * Created by John on 3/13/2017.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }
    public MovieArrayAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {

        float voteAverage = getItem(position).voteAverage;

        if (voteAverage > 5.0)
            return Movie.moviePopularity.POPULAR_MOVIE.ordinal();
        else
            return Movie.moviePopularity.NOT_POPULAR_MOVIE.ordinal();
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return Movie.moviePopularity.values().length;
    }

    // Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);

        // Get the data item type for this position
        int type = getItemViewType(position);


        if (type == Movie.MoviePopularity.POPULAR_MOVIE.ordinal()) {
            ViewHolder viewHolderPopularMovie; // view lookup cache stored in tag

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {

                viewHolderPopularMovie = new ViewHolder();

                // Inflate XML layout based on the type
                convertView = getInflatedLayoutForType(type);

                // Find the image view
                viewHolderPopularMovie.ivImage = (ImageView) convertView.findViewById(ivImage);

                int orientation = getContext().getResources().getConfiguration().orientation;

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // Lookup view for data population
                    viewHolderPopularMovie.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                    viewHolderPopularMovie.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                }


                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolderPopularMovie);

                // Clear out the image from convertView
                viewHolderPopularMovie.ivImage.setImageResource(0);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolderPopularMovie = (ViewHolder) convertView.getTag();
            }

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                // Populate the data from the data object via the viewHolder object
                // into the template view.
                viewHolderPopularMovie.tvTitle.setText(movie.originalTitle);
                viewHolderPopularMovie.tvOverview.setText(movie.overview);
            }


            // Load the Image from URL
            Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.mipmap.movie_image_placeholder)
                    .into(viewHolderPopularMovie.ivImage);


        } else if (type == Movie.MoviePopularity.NOT_POPULAR_MOVIE.ordinal()) {

            ViewHolder viewHolderNotPopularMovie; // view lookup cache stored in tag

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {

                viewHolderNotPopularMovie = new ViewHolder();

                // Inflate XML layout based on the type
                convertView = getInflatedLayoutForType(type);

                // Find the image view
                viewHolderNotPopularMovie.ivImage = (ImageView) convertView.findViewById(ivImage);

                // Lookup view for data population
                viewHolderNotPopularMovie.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolderNotPopularMovie.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolderNotPopularMovie);

                // Clear out the image from convertView
                viewHolderNotPopularMovie.ivImage.setImageResource(0);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolderNotPopularMovie = (ViewHolder) convertView.getTag();
            }

            // Populate the data from the data object via the viewHolder object
            // into the template view.
            viewHolderNotPopularMovie.tvTitle.setText(movie.originalTitle);
            viewHolderNotPopularMovie.tvOverview.setText(movie.overview);

            // Load the Image from URL
            Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.mipmap.movie_image_placeholder)
                    .into(viewHolderNotPopularMovie.ivImage);
        }
        // Return the completed view to render on screen
        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        if (type == Movie.MoviePopularity.POPULAR_MOVIE.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        } else if (type == Movie.MoviePopularity.NOT_POPULAR_MOVIE.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }
}
