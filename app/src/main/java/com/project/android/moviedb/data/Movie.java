package com.project.android.moviedb.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Class for a typical movie data retrieved from Movie DB
 */

public class Movie implements Parcelable {

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private static final String TAG = Movie.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private int runtime;
    private int id;
    private double voteAverage;
    private String title;
    private String posterSize ="w500";
    private String posterPath;
    private List<Video> trailers ;
    private List<Review> reviews;


    public Movie() {

    }

    private Movie(Parcel in) {
        voteAverage = in.readDouble();
        id = in.readInt();
        runtime = in.readInt();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        posterPath = in.readString();
        posterSize = in.readString();
    }

    public Movie(int id, double voteAverage, String title) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
    }

    public List<Video> getTrailers(){return trailers;}

    public void setTrailers(List<Video> trailers){
        this.trailers =  trailers;
    }

    public List<Review> getReviews (){return reviews;}

    public void setReviews(List<Review> reviews){this.reviews = reviews;}

    public String getPosterSize() {
        return posterSize;
    }

    public void setPosterSize(String posterSize) {
        this.posterSize = posterSize;
    }

    public String getConstructedPosterPath() {
        return BASE_IMAGE_URL + posterSize + posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath(){return  posterPath;}

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate.substring(0, 4);
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(voteAverage);
        dest.writeInt(id);
        dest.writeInt(runtime);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(posterSize);
    }


}