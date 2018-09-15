package com.project.android.moviedb.movieDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.android.moviedb.R;
import com.project.android.moviedb.data.Video;

import java.util.List;

/**
 * Created by fg7cpt on 12/26/2017.
 */

public class MovieTrailerAdaptor extends RecyclerView.Adapter<MovieTrailerAdaptor.MovieTrailerViewHolder> {

    private static final String TAG = MovieTrailerAdaptor.class.getName();
    private List<Video> trailers;
    private final String youtubeUrl = "https://youtu.be/";
    private Context context;
    public MovieTrailerAdaptor (@NonNull List<Video> videos){
        trailers = videos;
    }

    @Override
    public MovieTrailerAdaptor.MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewGroup layout = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new MovieTrailerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MovieTrailerAdaptor.MovieTrailerViewHolder holder, int position) {
        final Video trailer = trailers.get(holder.getAdapterPosition());
        holder.trailerName.setText(trailer.getName());
        holder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo(trailer.getSite(),trailer.getKey());
            }
        });
    }

    private void openVideo(String site, String key){
        if(site.equals( "YouTube")) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(youtubeUrl + key)));
        }else{
            Log.i(TAG, "not youtube");
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    protected static class MovieTrailerViewHolder extends RecyclerView.ViewHolder{
        private final TextView trailerName;
        private final ImageView videoImage;
        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView)itemView.findViewById(R.id.trailerName_TextView);
            videoImage =(ImageView) itemView.findViewById(R.id.video_imageView);
        }
    }
}
