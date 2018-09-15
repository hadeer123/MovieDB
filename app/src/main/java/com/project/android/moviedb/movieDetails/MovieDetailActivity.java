package com.project.android.moviedb.movieDetails;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.moviedb.BuildConfig;
import com.project.android.moviedb.R;
import com.project.android.moviedb.data.Movie;
import com.project.android.moviedb.data.MovieContract;
import com.project.android.moviedb.utilities.MovieDBJsonUtils;
import com.project.android.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getName();

    private Movie movie;
    private ImageView posterImageView;
    private Toolbar toolbar;
    private ImageView favoriteImageView;
    private RecyclerView.LayoutManager trailerLayoutManager;
    private RecyclerView.LayoutManager reviewLayoutManager;
    private boolean toSave=true;
    private RecyclerView trailerRecycler;
    private RecyclerView reviewRecycler;
    private TextView releaseDate;
    private TextView duration;
    private TextView overview;
    private TextView ratings;
    private MovieTrailerAdaptor movieTrailerAdaptor;
    private MovieReviewAdaptor movieReviewAdaptor;

    private void getMovieDBSearchQuery(String Query) throws ExecutionException, InterruptedException {
        URL getSearchURL = NetworkUtils.buildUrl(Query);
        new MovieQueryTask().execute(getSearchURL).get();
    }

    private  void connectedToInternet(){

        movie = getIntent().getParcelableExtra(this.getString(R.string.movie_details));
        setContentView(R.layout.activity_movie_detail);
        setTitle(getString(R.string.movie_details_activity_title));
        toolbar = (Toolbar) findViewById(R.id.movie_toolbar);
        posterImageView = (ImageView) findViewById(R.id.posterImageView);
        releaseDate = (TextView) findViewById(R.id.release_date_textView);
        duration = (TextView) findViewById(R.id.duration_textView);
        overview = (TextView) findViewById(R.id.overview_textView);
        ratings = (TextView) findViewById(R.id.rating_textView);
        trailerRecycler = (RecyclerView) findViewById(R.id.videos_recyclerView);
        reviewRecycler=(RecyclerView)findViewById(R.id.review_recyclerView) ;
        trailerLayoutManager = new LinearLayoutManager(getApplicationContext());
        trailerLayoutManager.canScrollHorizontally();
        reviewLayoutManager = new LinearLayoutManager(getApplicationContext());
        trailerRecycler.setLayoutManager(trailerLayoutManager);
        reviewRecycler.setLayoutManager(reviewLayoutManager);
        favoriteImageView = (ImageView) findViewById(R.id.favorite_imageView);
        toSave = !isSaved();
        favoriteImageView.setImageResource((toSave)?R.drawable.ic_favorite_border_black_24dp:R.drawable.ic_favorite_black_fill_24dp);
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toSave){
                    // save
                    saveToFavorites();
                }else{
                    //delete
                    unSave();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NetworkUtils.isNetworkAvailable(this)) {
            connectedToInternet();
            try {
                getMovieDBSearchQuery(movie.getId() + "?" +"api_key="+ BuildConfig.API_KEY + "&" + "append_to_response=videos,reviews");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            setContentView(R.layout.no_network);
        }
    }

    public boolean isSaved(){
        int id = movie.getId();
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.FavMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = getContentResolver().query(uri,null,null,null,null, null);
        return cursor.getCount()!=0?true:false;
    }

    public void unSave(){
        toSave = true;
        favoriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        int id = movie.getId();
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.FavMovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        getContentResolver().delete(uri, null, null);
    }

    public void saveToFavorites(){
        toSave = false ;
        favoriteImageView.setImageResource(R.drawable.ic_favorite_black_fill_24dp);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_VOTE_AVG, movie.getVoteAverage());
        contentValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        Uri uri = getContentResolver().insert(MovieContract.FavMovieEntry.CONTENT_URI, contentValues);
        if(uri != null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private class MovieQueryTask extends AsyncTask<URL, Void, Movie> {

        private void updateView(Movie movie){

            releaseDate.setText(movie.getReleaseDate());
            overview.setText(movie.getOverview());
            duration.setText(movie.getRuntime()+"");
            toolbar.setTitle(movie.getOriginalTitle());
            ratings.setText(movie.getVoteAverage()+" ");

            movie.setPosterSize("w500");
            Log.i(TAG, movie.getConstructedPosterPath());
            Picasso.with(getApplicationContext()).load(movie.getConstructedPosterPath()).into(posterImageView);
            movieTrailerAdaptor = new MovieTrailerAdaptor(movie.getTrailers());
            movieReviewAdaptor = new MovieReviewAdaptor(movie.getReviews());
            reviewRecycler.setAdapter(movieReviewAdaptor);
            trailerRecycler.setAdapter(movieTrailerAdaptor);

        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            updateView(movie);
        }

        @Override
        protected Movie doInBackground(URL... params) {
            URL searchUrl = params[0];
            String SearchResults;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                return  MovieDBJsonUtils
                        .getSimpleMovieDetailFromJson(getApplicationContext(), SearchResults, movie);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }

        }


    }

}
