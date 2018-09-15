package com.project.android.moviedb.mainSorting;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.android.moviedb.R;
import com.project.android.moviedb.data.MovieContract;


public class MovieDBFavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static  RecyclerView.LayoutManager mLayoutManager;
    private static  RecyclerView mRecyclerView;
    private static final int LOADER_ID = 0;
    private static final String TAG = MovieDBFavoriteFragment.class.getSimpleName();
    private FavoriteMoviesDBAdaptor favoriteMoviesDBAdaptor;

    public MovieDBFavoriteFragment() {
        // Required empty public constructor
    }

    public static MovieDBFavoriteFragment newInstance() {
        MovieDBFavoriteFragment fragment = new MovieDBFavoriteFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fav_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(),2);
        favoriteMoviesDBAdaptor = new FavoriteMoviesDBAdaptor(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(favoriteMoviesDBAdaptor);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootView;
    }

    public AsyncTaskLoader<Cursor> onCreateLoader(int id, final Bundle loaderArgs){
        return new AsyncTaskLoader<Cursor>(getContext()) {

            Cursor mFavoriteMoviesData;

            @Override
            protected void onStartLoading() {
                if (mFavoriteMoviesData != null) {

                    deliverResult(mFavoriteMoviesData);
                } else {

                    forceLoad();
                }
            }
            @Override
            public Cursor loadInBackground() {

                try {
                    return getContext().getContentResolver().query(MovieContract.FavMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mFavoriteMoviesData = data;
                super.deliverResult(data);
            }
        };

    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favoriteMoviesDBAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
