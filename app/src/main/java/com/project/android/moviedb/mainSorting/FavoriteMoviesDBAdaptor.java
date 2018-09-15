package com.project.android.moviedb.mainSorting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.android.moviedb.data.MovieContract;
import com.project.android.moviedb.movieDetails.MovieDetailActivity;
import com.project.android.moviedb.data.Movie;
import com.project.android.moviedb.R;
import com.squareup.picasso.Picasso;

public class FavoriteMoviesDBAdaptor extends RecyclerView.Adapter<FavoriteMoviesDBAdaptor.FavoriteMovieViewHolder>{
    private static final String TAG = FavoriteMoviesDBAdaptor.class.getName();
    private Cursor mCursor;
    private Context context;


    public FavoriteMoviesDBAdaptor(Context context) {
        this.context = context;
    }

    private void launchActivity(com.project.android.moviedb.data.Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(context.getString(R.string.movie_details), movie);
        context.startActivity(intent);
    }

    @Override
    public FavoriteMoviesDBAdaptor.FavoriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);
        return new FavoriteMovieViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final FavoriteMoviesDBAdaptor.FavoriteMovieViewHolder holder, int position) {

        int posterPath = mCursor.getColumnIndex(MovieContract.FavMovieEntry.COLUMN_MOVIE_POSTER_PATH);
        int id = mCursor.getColumnIndex(MovieContract.FavMovieEntry.COLUMN_MOVIE_ID);
        int average = mCursor.getColumnIndex(MovieContract.FavMovieEntry.COLUMN_MOVIE_VOTE_AVG);
        int title = mCursor.getColumnIndex(MovieContract.FavMovieEntry.COLUMN_MOVIE_TITLE);

        mCursor.moveToPosition(position);

        final String cPosterPath = mCursor.getString(posterPath);
        final String cTitle = mCursor.getString(title);
        final int cId = mCursor.getInt(id);
        final double cAvg = mCursor.getDouble(average);

        final Movie movie = new Movie(cId, cAvg,cTitle);
        movie.setPosterPath(cPosterPath);

        Picasso.with(context).load(movie.getConstructedPosterPath()).into(holder.posterImage);
        Log.i(TAG, ""+cPosterPath);
        holder.posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchActivity(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }



    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    protected static class FavoriteMovieViewHolder extends RecyclerView.ViewHolder{
        private final ImageView posterImage;
        public FavoriteMovieViewHolder(View itemView) {
            super(itemView);
            posterImage= (ImageView)itemView.findViewById(R.id.poster_image);
        }
    }

}
