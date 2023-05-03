package com.gtfconnect.ui.adapters.channelModuleAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.medialibrary.VideoActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChatMediaItemBinding;
import com.gtfconnect.databinding.RecyclerSingleChatMediaItemBinding;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelMediaResponseModel;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class ChannelMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<ChannelMediaResponseModel> mediaList;
    String postBaseUrl;
    String userID;

    RecyclerView recyclerRootView;

    ExoPlayer simpleExoPlayer;

    ImageView btFullScreen, rewind, forward;

    public  ChannelMediaAdapter(Context context,RecyclerView recyclerRootView,List<ChannelMediaResponseModel> mediaList,String postBaseUrl,String userID){
        this.context= context;
        this.mediaList = mediaList;
        this.postBaseUrl = postBaseUrl;
        this.userID = userID;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            /*final View view =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.your_view, parent, false);

            // recyclerView is your passed view.
            int width = recyclerRootView.getWidth();
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = (int)(width * 0.8);
            view.setLayoutParams(params);
            return new YourViewHolder(view);*/

        if (viewType == 1){
            return new ChannelMediaAdapter.SingleMediaItemViewHolder(RecyclerSingleChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
        else{
            return new ChannelMediaAdapter.MultiMediaItemViewHolder(RecyclerChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String fileType = Utils.checkFileType(mediaList.get(position).getMimeType());
        String post_path = postBaseUrl + mediaList.get(position).getStoragePath() + mediaList.get(position).getFileName();



        if (mediaList.size() == 1){
            ChannelMediaAdapter.SingleMediaItemViewHolder holder1 = (ChannelMediaAdapter.SingleMediaItemViewHolder) holder;
            holder1.binding.progressBar.setVisibility(View.GONE);
            holder1.binding.playerView.setVisibility(View.GONE);

            holder1.binding.postMediaContainer.setOnClickListener(view -> {

                Dialog previewDialog = new Dialog(context);
                previewDialog.setContentView(R.layout.dialog_media_preview);

                previewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView preview =(ImageView) previewDialog.findViewById(R.id.preview);
                loadImageFile(post_path,preview);



                ImageView close = (ImageView) previewDialog.findViewById(R.id.close);
                ImageView download_media = (ImageView) previewDialog.findViewById(R.id.download_media);

                close.setOnClickListener(view1 -> previewDialog.dismiss());
                download_media.setOnClickListener(view1 -> {

                });

                previewDialog.show();
            });


            Log.d("Entered_POst",postBaseUrl);

            if (fileType.equalsIgnoreCase("image"))
            {

                Log.d("Post Main Url", post_path);

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                holder1.binding.docContainer.setVisibility(View.VISIBLE);
                holder1.binding.playVideo.setVisibility(View.GONE);

                loadDocumentFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("video")) {

                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.playVideo.setVisibility(View.VISIBLE);

                //loadVideoFile(post_path,holder.binding.postImage);
            } else if (fileType.equalsIgnoreCase("gif")) {

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            } else{
                Log.d("File_Type_Error",fileType);
            }


            holder1.binding.playVideo.setOnClickListener(view -> {

                holder1.binding.progressBar.setVisibility(View.VISIBLE);
                holder1.binding.playerView.setVisibility(View.VISIBLE);


                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.headerContainer.setVisibility(View.GONE);



                //holder1.binding.headerContainer.setVisibility(View.GONE);
                //holder1.binding.playerContainer.setVisibility(View.VISIBLE);

            simpleExoPlayer = new ExoPlayer.Builder(context).build();
            holder1.binding.playerView.setPlayer(simpleExoPlayer);
            holder1.binding.playerView.setKeepScreenOn(true);

            MediaItem mediaItem = MediaItem.fromUri(post_path);
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
                        holder1.binding.progressBar.setVisibility(View.VISIBLE);
                    } else if (state == Player.STATE_READY) {
                        holder1.binding.progressBar.setVisibility(View.GONE);
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

            });
        }
        else{

            ChannelMediaAdapter.MultiMediaItemViewHolder holder1 = (ChannelMediaAdapter.MultiMediaItemViewHolder) holder;

            holder1.binding.postMediaContainer.setOnClickListener(view -> {

                Dialog previewDialog = new Dialog(context);
                previewDialog.setContentView(R.layout.dialog_media_preview);

                previewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                previewDialog.show();
            });

            Log.d("Entered_POst",postBaseUrl);

            if (fileType.equalsIgnoreCase("image"))
            {

                Log.d("Post Main Url", post_path);

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                holder1.binding.docContainer.setVisibility(View.VISIBLE);
                holder1.binding.playVideo.setVisibility(View.GONE);

                loadDocumentFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("video")) {

                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.playVideo.setVisibility(View.VISIBLE);

                //loadVideoFile(post_path,holder.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("gif")) {

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            }
            else{
                Log.d("File_Type_Error",fileType);
            }


            holder1.binding.playVideo.setOnClickListener(view -> {
                loadVideoFile(post_path,holder1.binding.postImage);
            });

        }






    }

    /*public void setupPlayer(int position) {

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
                MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
            }
        });

        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    btFullScreen.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), com.exa.ashutosh_video.R.drawable.ic_fullscreen, null));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    flag = false;
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.bottomLayout.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.simpleExoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = 800;
                    binding.simpleExoPlayerView.setLayoutParams(params);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                } else {
                    btFullScreen.setImageDrawable(ResourcesCompat.getDrawable(getResources(), com.exa.ashutosh_video.R.drawable.ic_fullscreen_exit, null));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;
                    binding.toolBar.setVisibility(View.GONE);
                    binding.bottomLayout.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.simpleExoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    binding.simpleExoPlayerView.setLayoutParams(params);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }
            }
        });
    }*/








    private void loadImageFile(String imageFilePath, ImageView imageView)
    {
        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();

        Glide.with(context).load(imageFilePath).
                fitCenter().apply(requestOptions).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    private void loadVideoFile(String videoFilePath, ImageView imageView)
    {
        context.startActivity(new Intent(context, VideoActivity.class)
                .putExtra("videourl",videoFilePath)
                .putExtra("start_time","0")
                .putExtra("end_time","0"));
    }


    private void loadDocumentFile(String docFilePath, ImageView imageView)
    {

    }





    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    static class SingleMediaItemViewHolder extends RecyclerView.ViewHolder {
        RecyclerSingleChatMediaItemBinding binding;

        SingleMediaItemViewHolder(@NonNull RecyclerSingleChatMediaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    static class MultiMediaItemViewHolder extends RecyclerView.ViewHolder {
        RecyclerChatMediaItemBinding binding;

        MultiMediaItemViewHolder(@NonNull RecyclerChatMediaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        return mediaList.size();
    }
}

