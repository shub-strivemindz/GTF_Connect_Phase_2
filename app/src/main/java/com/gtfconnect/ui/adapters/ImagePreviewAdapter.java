package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
import com.example.medialibrary.VideoActivity;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerMediaPreviewBinding;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.interfaces.MultiPreviewListener;
import com.gtfconnect.models.groupChannelModels.MediaListModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.Utils;

import java.util.ArrayList;
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

            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.VISIBLE);
            holder.binding.docViewer.setVisibility(View.GONE);
            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
        }
        else if (fileType.equalsIgnoreCase("video")) {


            holder.binding.playVideo.setVisibility(View.VISIBLE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.GONE);
            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
        }
        else {

            holder.binding.previewNotAvailableContainer.setVisibility(View.VISIBLE);
            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.GONE);

            loadDocument(holder,post_path);
        }


        holder.binding.playVideo.setOnClickListener(view -> loadVideoFile(post_path));

        holder.binding.openDoc.setOnClickListener(view -> {
            holder.binding.previewNotAvailableContainer.setVisibility(View.GONE);
            holder.binding.playVideo.setVisibility(View.GONE);
            holder.binding.mediaPreview.setVisibility(View.GONE);
            holder.binding.docViewer.setVisibility(View.VISIBLE);
        });

    }

    public void updateList(List<MediaListModel> mediaList)
    {
        this.mediaList = mediaList;
        notifyDataSetChanged();
    }

    private void loadVideoFile(String videoFilePath)
    {
        context.startActivity(new Intent(context, VideoActivity.class)
                .putExtra("videourl",videoFilePath)
                .putExtra("start_time","0")
                .putExtra("end_time","0"));
    }

    private void loadDocument(ViewHolder holder,String docPath)
    {
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
}