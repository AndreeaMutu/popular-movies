package com.andreea.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andreea.popularmovies.model.Video;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<Video> videos = new ArrayList<>();

    public void refresh(List<Video> reviews) {
        this.videos = reviews;
        this.notifyDataSetChanged();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = videos.get(position);
        holder.videoTitleTextView.setText(video.getName());
        holder.playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                playYoutubeVideo(context, video.getKey());
            }
        });
    }

    private void playYoutubeVideo(Context context, String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYoutubeUri(videoId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitleTextView;
        private ImageButton playVideoButton;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoTitleTextView = (TextView) itemView.findViewById(R.id.video_title_tv);
            playVideoButton = (ImageButton) itemView.findViewById(R.id.play_trailer_btn);
        }
    }
}
