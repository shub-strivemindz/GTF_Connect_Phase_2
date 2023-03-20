package com.example.medialibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class VideoActivity extends AppCompatActivity {
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen, rewind, forward;
    ExoPlayer simpleExoPlayer;
    boolean flag = false;
    long videoWatchedTime =0;
    long startTime, endTime;
    ScheduledExecutorService mScheduledExecutorService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

      /*  Intent intent = getIntent();
        String videourl = intent.getStringExtra("videourl");
        Log.v("TYPE_SOURCE", "videourl: " + videourl);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        HashMap<String , String> extraHeaders = new HashMap<>();
        extraHeaders.put("foo","bar");
        andExoPlayerView.setSource(videourl,extraHeaders);*/
        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btFullScreen = findViewById(R.id.bt_fullscreen);
        rewind = findViewById(R.id.exo_rew);
        forward = findViewById(R.id.exo_ffwd);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String videourl = intent.getStringExtra("videourl");
        startTime = Long.parseLong(intent.getStringExtra("start_time"));
        endTime = Long.parseLong(intent.getStringExtra("end_time"));
        Log.v("TYPE_SOURCE", "videourl: " + videourl);


        simpleExoPlayer = new ExoPlayer.Builder(VideoActivity.this).build();
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        MediaItem mediaItem = MediaItem.fromUri(videourl);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.setPlayWhenReady(true);

        if (endTime != 0) {
            simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentMediaItemIndex(),startTime);
            playerView.findViewById(R.id.exo_progress).setVisibility(View.GONE);
            playerView.setShowRewindButton(false);
            playerView.setShowFastForwardButton(false);
        }
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
        simpleExoPlayer.addListener(new Player.Listener() {


            @Override
            public void onPlaybackStateChanged(@Player.State int state) {
                if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
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
                MediaItem mediaItem = MediaItem.fromUri(videourl);
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.setPlayWhenReady(true);
                if (endTime != 0) {
                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentMediaItemIndex(),startTime);
                    playerView.findViewById(R.id.exo_progress).setVisibility(View.GONE);
                    playerView.setShowRewindButton(false);
                    playerView.setShowFastForwardButton(false);
                }
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
            }
        });



        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {

                    btFullScreen.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fullscreen, null));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    flag = false;
                } else {
                    btFullScreen.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fullscreen_exit, null));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;
                }

            }
        });
        Runnable getPositionVideo = () -> {
            videoWatchedTime= simpleExoPlayer.getCurrentPosition();
            Log.v("VideoTimeView",""+videoWatchedTime);
            if (videoWatchedTime >= endTime){
                //MediaItem mediaItem = MediaItem.fromUri(videourl);
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentMediaItemIndex(),startTime);
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
            }


        };
        mScheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        mScheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (endTime != 0) {
                runOnUiThread(getPositionVideo);
            }
            //MediaControllerExitFullScreen.init(video_url, time);
        }, 1000, 1000, TimeUnit.MILLISECONDS);

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do your work
                videoWatchedTime= simpleExoPlayer.getCurrentPosition();

*/
/*
                if (videoWatchedTime >= 10000){
                    MediaItem mediaItem = MediaItem.fromUri(videourl);
                    simpleExoPlayer.addMediaItem(mediaItem);
                    simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentMediaItemIndex(),5000);
                    simpleExoPlayer.prepare();
                    simpleExoPlayer.play();
                }
*//*

            }
        },1000);
*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleExoPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

}