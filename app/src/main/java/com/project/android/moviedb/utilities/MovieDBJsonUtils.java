package com.project.android.moviedb.utilities;

import android.content.Context;
import android.util.Log;

import com.project.android.moviedb.data.Movie;
import com.project.android.moviedb.R;
import com.project.android.moviedb.data.Review;
import com.project.android.moviedb.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class to Parse Json objects to Movie objects
 */

public class MovieDBJsonUtils {


    private static final String TAG = MovieDBJsonUtils.class.toString();

    private static Movie updateMovieFromJson(Context context, JSONObject movieObj, Movie movie) {
        try {
            movie.setOriginalTitle(movieObj.getString(context.getString(R.string.original_title)));
            movie.setOverview(movieObj.getString(context.getString(R.string.overview)));
            movie.setRuntime(movieObj.getInt(context.getString(R.string.runtime)));
            movie.setReleaseDate(movieObj.getString(context.getString(R.string.release_date)));
            movie.setTrailers(createVideoList(movieObj,context));
            movie.setReviews(createReviewList(movieObj, context));
        } catch (JSONException e) {
        Log.e(TAG, e.getMessage());
    }
        return movie;
    }

    private static ArrayList<Review> createReviewList(JSONObject movieObj, Context context) throws JSONException{
        JSONObject reviewObj = (JSONObject) movieObj.get(context.getString(R.string.review_json));
        ArrayList<Review> reviews = new ArrayList<>();

        JSONArray videosArray = reviewObj.getJSONArray(context.getString(R.string.json_movie_results_obj));

        for (int i = 0; i < videosArray.length(); i++) {

            JSONObject review = videosArray.getJSONObject(i);
            reviews.add(i, createReviewFromJson(review,context));
        }
        return reviews;
    }

    private static ArrayList<Video> createVideoList(JSONObject movieObj, Context context) throws JSONException{
        JSONObject videosObjRes = (JSONObject) movieObj.get(context.getString(R.string.video_json));
        ArrayList<Video> videos = new ArrayList<> ();

        JSONArray videosArray = videosObjRes.getJSONArray(context.getString(R.string.json_movie_results_obj));

        for (int i = 0; i < videosArray.length(); i++) {

            JSONObject video = videosArray.getJSONObject(i);
            videos.add(i, createVideoFromJson(video,context));
        }
        return videos;
    }


    private static Review createReviewFromJson(JSONObject reviewObj, Context context){
        Review review = null;
        try {
            if(reviewObj.length()!=0)
                review = new Review(reviewObj.getString(context.getString(R.string.id_json)),reviewObj.getString(context.getString(R.string.author_json)),reviewObj.getString(context.getString(R.string.content_json)),reviewObj.getString(context.getString(R.string.url_json)));

        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
        return review;
    }

    private static Video createVideoFromJson(JSONObject videoObj, Context context){
        Video video = null;
        try {

            if(videoObj.length()!=0)
                video = new Video(videoObj.getString(context.getString(R.string.id_json)),videoObj.getString(context.getString(R.string.key_json)),videoObj.getString(context.getString(R.string.name_json)),videoObj.getString(context.getString(R.string.site_json)), videoObj.getInt(context.getString(R.string.size_json)),videoObj.getString(context.getString(R.string.type_json)));

        }catch (JSONException e){
            Log.e(TAG,e.getMessage());

        }
        return video;
    }

    private static Movie createNewMovieFromJson(Context context, JSONObject movieObj) {
        Movie newMovie = null;
        try {
            newMovie = new Movie(movieObj.getInt(context.getString(R.string.json_movie_id)),
                    movieObj.getDouble(context.getString(R.string.json_movie_vote_avrg)),
                    movieObj.getString(context.getString(R.string.json_movie_title)));
            newMovie.setPosterPath(movieObj.getString(context.getString(R.string.json_movie_poster_path)));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return newMovie;
    }


    public static ArrayList<Movie> getSimpleMovieQueryStringFromJson(Context context, String searchResult)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(searchResult);
        ArrayList<Movie> arrayList = new ArrayList<>();
        // Handle possible Query error
        if (moviesJson.has(context.getString(R.string.json_movie_success))) {
            Boolean isSuccessful = moviesJson.getBoolean(context.getString(R.string.json_movie_success));
            String error_message = moviesJson.getString(context.getString(R.string.json_error_message));

            if (!isSuccessful) {
                Log.e(TAG, error_message);
                return null;
            }
        }

        JSONArray movieArray = moviesJson.getJSONArray(context.getString(R.string.json_movie_results_obj));

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movie = movieArray.getJSONObject(i);
            arrayList.add(i, createNewMovieFromJson(context, movie));

        }

        return arrayList;
    }


    public static Movie getSimpleMovieDetailFromJson(Context context, String searchResult, Movie movie)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(searchResult);

        // Handle possible Query error
        if (moviesJson.has(context.getString(R.string.json_movie_success))) {
            Boolean isSuccessful = moviesJson.getBoolean(context.getString(R.string.json_movie_success));
            String error_message = moviesJson.getString(context.getString(R.string.json_error_message));

            if (!isSuccessful) {
                Log.e(TAG, error_message);
                return null;
            }

        }
        return updateMovieFromJson(context, moviesJson, movie);
    }
}



