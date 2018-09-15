package com.project.android.moviedb.mainSorting;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.android.moviedb.R;
import com.project.android.moviedb.movieDetails.MovieDetailActivity;
import com.project.android.moviedb.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieDBAdaptor extends RecyclerView.Adapter<MovieDBAdaptor.MovieDBViewHolder> {
    private static final String TAG = MovieDBAdaptor.class.toString();
    private List <Movie> movies;
    private Context context;

    public MovieDBAdaptor(@NonNull List<Movie> objects) {
        movies = objects;
    }

    private void launchActivity(Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(context.getString(R.string.movie_details), movie);
        context.startActivity(intent);
    }

    @Override
    public MovieDBAdaptor.MovieDBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewGroup layout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);
        return new MovieDBViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(final MovieDBAdaptor.MovieDBViewHolder holder, final int position) {
        Picasso.with(context).load(movies.get(holder.getAdapterPosition()).getConstructedPosterPath()).into(holder.posterImage);
        holder.posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(movies.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    protected static class MovieDBViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImage;

        private MovieDBViewHolder(ViewGroup view) {
            super(view);
            posterImage= (ImageView)view.findViewById(R.id.poster_image);
        }
    }
}
