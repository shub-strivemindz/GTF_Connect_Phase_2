package com.gtfconnect.ui.adapters.commonGroupChannelAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.exa.ashutosh_video.VideoActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChatMediaItemBinding;
import com.gtfconnect.databinding.RecyclerSingleChatMediaItemBinding;
import com.gtfconnect.models.commonGroupChannelResponseModels.MediaListModel;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelMediaAdapter;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.MultiPreviewScreen;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupChannelPinnedMessageMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<MediaListModel> mediaList;
    String postBaseUrl;
    String userID;

    RecyclerView recyclerRootView;

    boolean isSelf ;


    ExoPlayer simpleExoPlayer;

    ImageView btFullScreen, rewind, forward;

    private String user_name;

    OnMediaPlayPauseListener listener;

    private ExoPlayer previousPlaybackInstance;

    public  GroupChannelPinnedMessageMediaAdapter(Context context,List<MediaListModel> mediaList,String postBaseUrl,String userID, String user_name, ExoPlayer previousPlaybackInstance, boolean isSelf){
        this.context= context;
        this.mediaList = mediaList;
        this.postBaseUrl = postBaseUrl;
        this.userID = userID;

        this.isSelf = isSelf;

        this.previousPlaybackInstance = previousPlaybackInstance;

        if (previousPlaybackInstance != null){
            if (previousPlaybackInstance.isPlaying()) {
                previousPlaybackInstance.pause();
                previousPlaybackInstance.stop();
            }
            previousPlaybackInstance.release();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == 1){
            return new GroupChannelPinnedMessageMediaAdapter.SingleMediaItemViewHolder(RecyclerSingleChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
        else{
            return new GroupChannelPinnedMessageMediaAdapter.MultiMediaItemViewHolder(RecyclerChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String fileType = Utils.checkFileType(mediaList.get(position).getMimeType());
        String post_path = postBaseUrl + mediaList.get(position).getStoragePath() + mediaList.get(position).getFileName();



        if (mediaList.size() == 1){
            GroupChannelPinnedMessageMediaAdapter.SingleMediaItemViewHolder holder1 = (SingleMediaItemViewHolder) holder;
            holder1.binding.progressBar.setVisibility(View.GONE);
            //holder1.binding.playerView.setVisibility(View.GONE);

            holder1.binding.postMediaContainer.setOnClickListener(view -> {



                Gson gson1  = new Gson();
                String mediaData =  gson1.toJson(mediaList);

                Intent intent = new Intent(context, MultiPreviewScreen.class);
                intent.putExtra("mediaList",mediaData);
                intent.putExtra("base_url",postBaseUrl);

                String title = "Pinned Messages";

                intent.putExtra("title",title);
                intent.putExtra("isSelf",isSelf);
                context.startActivity(intent);

            });


            Log.d("Entered_POst",postBaseUrl);

            if (fileType.equalsIgnoreCase("image"))
            {

                Log.d("Post Main Url", post_path);

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                if (Utils.isFileTypeGif((mediaList.get(position).getMimeType()))) {

                    holder1.binding.playGif.setVisibility(View.VISIBLE);
                    GlideUtils.loadImage(context,holder1.binding.postImage,post_path);
                    //loadImageFile(post_path,holder1.binding.postImage);
                }
                else{

                    holder1.binding.playGif.setVisibility(View.GONE);
                    loadImageFile(post_path,holder1.binding.postImage);
                }


            }
            else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                holder1.binding.docContainer.setVisibility(View.VISIBLE);
                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.playGif.setVisibility(View.GONE);

                loadDocumentFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("video")) {

                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.playVideo.setVisibility(View.VISIBLE);
                holder1.binding.playGif.setVisibility(View.GONE);

                //loadVideoFile(post_path,holder.binding.postImage);
            } else if (fileType.equalsIgnoreCase("gif")) {

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.playGif.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            } else{
                Log.d("File_Type_Error",fileType);
            }

            holder1.binding.playGif.setOnClickListener(view -> {
                holder1.binding.playGif.setVisibility(View.GONE);
                loadImageFile(post_path,holder1.binding.postImage);
            });


            holder1.binding.playVideo.setOnClickListener(view -> {

                holder1.binding.progressBar.setVisibility(View.VISIBLE);
                //holder1.binding.playerView.setVisibility(View.VISIBLE);


                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.headerContainer.setVisibility(View.GONE);

            });

            holder1.binding.playVideo.setOnClickListener(view -> {

                holder1.binding.progressBar.setVisibility(View.VISIBLE);
                holder1.binding.playerView.setVisibility(View.VISIBLE);


                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.headerContainer.setVisibility(View.GONE);
                holder1.binding.playGif.setVisibility(View.GONE);


                loadAutoPlayVideoFile(post_path,holder1.binding.playerView,holder1.binding.progressBar);

            });
        }
        else{

            GroupChannelPinnedMessageMediaAdapter.MultiMediaItemViewHolder holder1 = (MultiMediaItemViewHolder) holder;

            holder1.binding.postMediaContainer.setOnClickListener(view -> {

                Gson gson1  = new Gson();
                String mediaData =  gson1.toJson(mediaList);

                Intent intent = new Intent(context, MultiPreviewScreen.class);
                intent.putExtra("mediaList",mediaData);
                intent.putExtra("base_url",postBaseUrl);

                String title = "Pinned Messages";
                intent.putExtra("isSelf",isSelf);
                intent.putExtra("title",title);

                context.startActivity(intent);
            });

            Log.d("Entered_POst",postBaseUrl);

            if (fileType.equalsIgnoreCase("image"))
            {

                Log.d("Post Main Url", post_path);

                holder1.binding.playVideo.setVisibility(View.GONE);
                holder1.binding.docContainer.setVisibility(View.GONE);

                if (Utils.isFileTypeGif(mediaList.get(position).getMimeType())){
                    loadImageFile(post_path,holder1.binding.postImage);
                }
                else{
                    GlideUtils.loadImage(context,holder1.binding.postImage,post_path);
                }
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





    private void loadAutoPlayVideoFile(String videoPath, PlayerView videoPlayer, ProgressBar progressBar){


        simpleExoPlayer = new ExoPlayer.Builder(context).build();
        videoPlayer.setPlayer(simpleExoPlayer);
        videoPlayer.setKeepScreenOn(true);

        MediaItem mediaItem = MediaItem.fromUri(videoPath);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        listener.OnMediaPlayPause(simpleExoPlayer);
        simpleExoPlayer.addListener(new Player.Listener() {

            @Override
            public void onPlaybackStateChanged(@Player.State int state) {
                if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                }
                else if (state == Player.STATE_ENDED) {

                    // Todo
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





    public void setOnMediaPlayPauseListener(OnMediaPlayPauseListener listener) {
        this.listener = listener;
    }

    public interface OnMediaPlayPauseListener {
        void OnMediaPlayPause(ExoPlayer exoPlayer);

    }
}

