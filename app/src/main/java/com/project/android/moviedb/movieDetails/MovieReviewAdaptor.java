package com.project.android.moviedb.movieDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.android.moviedb.R;
import com.project.android.moviedb.data.Review;

import java.util.List;


public class MovieReviewAdaptor extends RecyclerView.Adapter<MovieReviewAdaptor.MovieReviewViewHolder> {

    private static final String TAG = MovieReviewAdaptor.class.getName();
    private List<Review> reviews;
    private Context context;

    public MovieReviewAdaptor(@NonNull List<Review> reviews){
        this.reviews = reviews;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewGroup layout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new MovieReviewViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MovieReviewViewHolder holder, int position) {
        holder.reviewTextView.setText(reviews.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    protected static class MovieReviewViewHolder extends RecyclerView.ViewHolder{
        private final TextView reviewTextView;
        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            reviewTextView = (TextView)itemView.findViewById(R.id.review_textView);
        }
    }
}
