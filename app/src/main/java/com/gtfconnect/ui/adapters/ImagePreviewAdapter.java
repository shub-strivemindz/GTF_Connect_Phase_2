package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.exa.ashutosh_video.VideoActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerMediaPreviewBinding;
import com.gtfconnect.interfaces.MultiPreviewListener;
import com.gtfconnect.models.commonGroupChannelResponseModels.MediaListModel;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {

    private Context context;

    private List<MediaListModel> mediaList;

    private int oldPosition = 0;

    String post_base_url="";

    private Drawable imageDrawable;

    private MultiPreviewListener listener;

    private String fileType = "";

    private int viewPosition ;

    ExoPlayer simpleExoPlayer;

    CircularProgressDrawable circularProgressDrawable;

    public  ImagePreviewAdapter(Context context, List<MediaListModel> mediaList,String post_base_url,MultiPreviewListener listener){
        this.mediaList = mediaList;
        this.post_base_url = post_base_url;
        this.context = context;

        this.listener = listener;
    }



    @NonNull
    @Override
    public ImagePreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ImagePreviewAdapter.ViewHolder(RecyclerMediaPreviewBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ImagePreviewAdapter.ViewHolder holder, int index) {

        final int position = index;


        imageDrawable = null;

        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();


        String fileType = Utils.checkFileType(mediaList.get(position).getMimeType());
        String post_path = post_base_url + mediaList.get(position).getStoragePath() + mediaList.get(position).getFileName();

        if (fileType.equalsIgnoreCase("image"))
        {

            Glide.with(context).load(post_path).
                    fitCenter().apply(requestOptions).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            listener.imageLoading(false);
                            return false;

                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            listener.imageLoading(true);
                            imageDrawable = resource;
                            return false;
                        }
                    }).
                    transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.mediaPreview);

            holder.binding.videoPlayerContainer.setVisibility(View.GONE);
            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.VISIBLE);
            holder.binding.docViewer.setVisibility(View.GONE);
            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
        }
        else if (fileType.equalsIgnoreCase("video")) {

            holder.binding.videoPlayerContainer.setVisibility(View.GONE);
            holder.binding.playVideo.setVisibility(View.VISIBLE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.GONE);
            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
        }
        else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

            holder.binding.videoPlayerContainer.setVisibility(View.GONE);
            holder.binding.previewNotAvailableContainer.setVisibility(View.VISIBLE);
            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.GONE);
        }


        holder.binding.playVideo.setOnClickListener(view -> {
            //loadVideoFile(post_path);
            holder.binding.videoPlayerContainer.setVisibility(View.VISIBLE);
            loadAutoPlayVideoFile(post_path,holder.binding.playerView,holder.binding.progressBar);
        });

        holder.binding.openDoc.setOnClickListener(view -> {

            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.VISIBLE);

            loadDocument(holder,post_path);
        });

    }

    public void updateList(List<MediaListModel> mediaList)
    {
        this.mediaList = mediaList;
        notifyDataSetChanged();
    }

/*
    private void loadVideoFile(String videoFilePath)
    {
        context.startActivity(new Intent(context, VideoActivity.class)
                .putExtra("videourl",videoFilePath)
                .putExtra("start_time","0")
                .putExtra("end_time","0"));
    }
*/

    private void loadDocument(ViewHolder holder,String docPath)
    {
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + docPath), "text/html");
        context.startActivity(intent);*/

        WebSettings settings = holder.binding.docViewer.getSettings();
        holder.binding.docViewer.setWebViewClient(new AppWebViewClients());
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        holder.binding.docViewer.setWebChromeClient(new WebChromeClient());
        holder.binding.docViewer.loadUrl("http://docs.google.com/gview?embedded=true&url="+docPath);
    }

    public class AppWebViewClients extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }




    public void downloadAttachment(int viewPosition){

        String fileType = Utils.checkFileType(mediaList.get(viewPosition).getMimeType());

        Log.d("fileType",fileType);

        if (fileType.equalsIgnoreCase("image")){
            if (Utils.isFileTypeGif(mediaList.get(viewPosition).getMimeType())){
                fileType = "gif";
            }
        }

        switch (fileType){
            case "gif":
                break;
            case "image":
                LocalGalleryUtil.saveImageToGallery(context,imageDrawable);
                break;
            case "video":

                String post_path = post_base_url + mediaList.get(viewPosition).getStoragePath() + mediaList.get(viewPosition).getFileName();
                listener.downloadVideo(mediaList.get(viewPosition).getGroupChannelID().toString(),mediaList.get(viewPosition).getGroupChatID().toString(),post_path);
                break;
            default:

        }
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

        //listener.OnMediaPlayPause(simpleExoPlayer);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerMediaPreviewBinding binding;

        ViewHolder(@NonNull RecyclerMediaPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



    public void destroyExoPlayer(){

        if (simpleExoPlayer != null){
            if (simpleExoPlayer.isPlaying()) {
                simpleExoPlayer.stop();
            }
            simpleExoPlayer.release();

        }
    }


    public void pauseExoPlayer(){
        if (simpleExoPlayer != null){
            if (simpleExoPlayer.isPlaying()) {
                simpleExoPlayer.pause();
            }
        }
    }
}