package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.medialibrary.VideoActivity;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerMediaPreviewBinding;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {

    private Context context;

    private List<GroupChatResponseModel.Medium> mediaList;

    private int oldPosition = 0;

    private ImagePreviewListener listener;

    String post_base_url="";

    public  ImagePreviewAdapter(Context context, ArrayList<GroupChatResponseModel.Medium> mediaList,String post_base_url){
        this.mediaList = mediaList;
        this.post_base_url = post_base_url;
        this.context = context;

    }



    @NonNull
    @Override
    public ImagePreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ImagePreviewAdapter.ViewHolder(RecyclerMediaPreviewBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ImagePreviewAdapter.ViewHolder holder, int index) {

        final int position = index;

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


        String fileType = Utils.checkFileType(mediaList.get(position).getMimeType());
        String post_path = post_base_url + mediaList.get(position).getStoragePath() + mediaList.get(position).getFileName();

        if (fileType.equalsIgnoreCase("image"))
        {
            Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.mediaPreview);
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

    public void updateList(ArrayList<GroupChatResponseModel.Medium> mediaList)
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