package com.example.easeme;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

import java.util.ArrayList;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
ArrayList<UrlModel> videoList;
Lifecycle lifecycle;
Context context;

    public VideoAdapter(ArrayList<UrlModel> videoList, Context context,Lifecycle lifecycle) {
        this.videoList = videoList;
        this.context = context;
        this.lifecycle=lifecycle;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.videolayout,parent,false);


        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

                String videoId = videoList.get(position).getData_url();

        YouTubePlayerView youTubePlayerView= holder.ytview;
        lifecycle.addObserver(youTubePlayerView);



        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                youTubePlayer.cueVideo(videoId, 0);

            }
        });





        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // using pre-made custom ui
                DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                defaultPlayerUiController.showMenuButton(true);
            }
        };

        // disable iframe ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        youTubePlayerView.initialize(listener, options);







    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder{
        YouTubePlayerView ytview;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            ytview= itemView.findViewById(R.id.youtube_player_view);







        }
    }
}
