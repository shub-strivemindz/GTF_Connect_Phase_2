package com.gtfconnect.ui.adapters.channelModuleAdapter;

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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChatMediaItemBinding;
import com.gtfconnect.databinding.RecyclerSingleChatMediaItemBinding;
import com.gtfconnect.models.commonGroupChannelResponseModels.MediaListModel;
import com.gtfconnect.ui.adapters.authModuleAdapter.CountryListAdapter;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.MultiPreviewScreen;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class ChannelMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<MediaListModel> mediaList;
    String postBaseUrl;
    String userID;

    private boolean isFirstLoading = false;

    RecyclerView recyclerRootView;

    ExoPlayer simpleExoPlayer;

    ImageView btFullScreen, rewind, forward;

    private String user_name;

    OnMediaPlayPauseListener listener;

    private ExoPlayer previousPlaybackInstance;

    public  ChannelMediaAdapter(Context context,RecyclerView recyclerRootView,List<MediaListModel> mediaList,String postBaseUrl,String userID, String user_name, ExoPlayer previousPlaybackInstance){
        this.context= context;
        this.mediaList = mediaList;
        this.postBaseUrl = postBaseUrl;
        this.userID = userID;

        this.user_name = user_name;

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

                Gson gson1  = new Gson();
                String mediaData =  gson1.toJson(mediaList);

                Intent intent = new Intent(context, MultiPreviewScreen.class);
                intent.putExtra("mediaList",mediaData);
                intent.putExtra("base_url",postBaseUrl);


                intent.putExtra("title",user_name);

                context.startActivity(intent);

            });


            //Log.d("Entered_POst",postBaseUrl);

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




            // Todo ========== Add auto play video condition check

            /*boolean autoPlayCheck = true;
            if (autoPlayCheck){

                holder1.binding.progressBar.setVisibility(View.VISIBLE);
                holder1.binding.playerView.setVisibility(View.VISIBLE);


                holder1.binding.docContainer.setVisibility(View.GONE);
                holder1.binding.headerContainer.setVisibility(View.GONE);
                holder1.binding.playGif.setVisibility(View.GONE);

                holder1.binding.playVideo.setVisibility(View.GONE);

                holder1.binding.playerView.hideController();

                loadAutoPlayVideoFile(post_path,holder1.binding.playerView,holder1.binding.progressBar);
            }
            else{
                holder1.binding.playVideo.setVisibility(View.VISIBLE);
            }*/




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

            ChannelMediaAdapter.MultiMediaItemViewHolder holder1 = (ChannelMediaAdapter.MultiMediaItemViewHolder) holder;

            holder1.binding.postMediaContainer.setOnClickListener(view -> {

                /*Dialog previewDialog = new Dialog(context);
                previewDialog.setContentView(R.layout.dialog_media_preview);

                previewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                previewDialog.show();*/



                Gson gson1  = new Gson();
                String mediaData =  gson1.toJson(mediaList);

                Intent intent = new Intent(context, MultiPreviewScreen.class);
                intent.putExtra("mediaList",mediaData);
                intent.putExtra("base_url",postBaseUrl);


                intent.putExtra("title",user_name);

                context.startActivity(intent);
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





    public void setOnMediaPlayPauseListener(OnMediaPlayPauseListener listener) {
        this.listener = listener;
    }

    public interface OnMediaPlayPauseListener {
        void OnMediaPlayPause(ExoPlayer exoPlayer);

    }



}

