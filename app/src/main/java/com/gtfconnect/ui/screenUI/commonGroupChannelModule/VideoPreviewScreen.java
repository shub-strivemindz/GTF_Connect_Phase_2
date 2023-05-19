package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.gtfconnect.databinding.ActivityVideoPreviewBinding;
import com.gtfconnect.utilities.Constants;

import java.io.File;

public class VideoPreviewScreen extends AppCompatActivity {

    ActivityVideoPreviewBinding binding;

    private ExoPlayer simpleExoPlayer;

    private File videoFile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        videoFile = (File) getIntent().getExtras().get("video");
      //  Uri videoUri = Uri.parse(getIntent().getStringExtra("video"));



        simpleExoPlayer = new ExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(simpleExoPlayer);
        binding.playerView.setKeepScreenOn(true);

        MediaItem mediaItem = MediaItem.fromUri(videoFile.getAbsolutePath());
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.setPlayWhenReady(true);

        //Log.v("playerSetup",isFirstTime+" "+watchTime);

          /*  if (!pageType.equalsIgnoreCase("my_web_series") && isFirstTime) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentMediaItemIndex(), watchTime * 1000L);
                isFirstTime = false;
            }*/

        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        simpleExoPlayer.addListener(new Player.Listener() {


            @Override
            public void onPlaybackStateChanged(@Player.State int state) {
                if (state == Player.STATE_BUFFERING) {
                    //binding.progressBar.setVisibility(View.VISIBLE);
                } else if (state == Player.STATE_READY) {
                    //binding.progressBar.setVisibility(View.GONE);
                } else if (state == Player.STATE_ENDED) {

                    // Todo == Show preview again
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                Log.v("TYPE_SOURCE", "TYPE_SOURCE: " + error.getMessage());
                MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
            }
        });


        binding.sendMessage.setOnClickListener(view -> {

            Intent resultIntent = new Intent();

            String message = binding.type.getText().toString().trim();
            if (message != null){
                resultIntent.putExtra("message",message);
            }
            else{
                resultIntent.putExtra("message","");
            }
            if (videoFile != null){
                resultIntent.putExtra("video",videoFile);

                setResult(Constants.SELECT_VIDEO_REQUEST_CODE,resultIntent);
                finish();
            }
            else{
                setResult(Constants.NO_VIDEO_FOUND,resultIntent);
                finish();
            }
        });
    }
}
