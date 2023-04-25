package com.gtfconnect.ui.adapters.commonGroupChannelAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.FragmentMediaListItemBinding;
import com.gtfconnect.databinding.FragmentUserDocumentItemBinding;
import com.gtfconnect.databinding.FragmentUserLinkItemBinding;
import com.gtfconnect.models.groupChannelModels.GroupChannelMediaResponseModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GalleryTypeStatus;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.utilities.LocalGalleryUtil;
import com.gtfconnect.utilities.Utils;

public class GroupChannelMemberMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int viewType;

    private Context context;

    private GroupChannelMediaResponseModel mediaResponseModel;

    private String base_url;

    private Rest rest;

    //private boolean isResponseLoaded ;

    public  GroupChannelMemberMediaAdapter(Context context,int viewType,GroupChannelMediaResponseModel mediaResponseModel){

        this.context = context;
        this.viewType = viewType;
        this.mediaResponseModel = mediaResponseModel;
        //this.isResponseLoaded = isResponseLoaded;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        /*if (viewType == 2)
        {
            return new LinkViewHolder(FragmentUserLinkItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }*/
        if (viewType ==3) {
            return new MediaViewHolder(FragmentMediaListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }
        else {
            return new DocumentViewHolder(FragmentUserDocumentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        base_url = "https://gtfconnect-api.strivemindz.com/";

        if (mediaResponseModel != null && mediaResponseModel.getData() != null) {

            if (viewType == 3) {

                MediaViewHolder viewHolder = (MediaViewHolder) holder;

                if (mediaResponseModel.getData().getMedia() != null && !mediaResponseModel.getData().getMedia().isEmpty()) {

                    if (Utils.checkFileType(mediaResponseModel.getData().getMedia().get(position).getMimeType()).equalsIgnoreCase("video")){
                        viewHolder.binding.playButton.setVisibility(View.VISIBLE);
                    }
                    else{
                        viewHolder.binding.playButton.setVisibility(View.GONE);
                    }


                    if (mediaResponseModel.getData().getMedia().get(position).getStoragePath() != null){
                        if(mediaResponseModel.getData().getMedia().get(position).getFileName() != null){
                            base_url += mediaResponseModel.getData().getMedia().get(position).getStoragePath() + mediaResponseModel.getData().getMedia().get(position).getFileName();
                            loadImage(base_url,viewHolder.binding.mediaImage);
                        }
                    }
                    else{
                        if(mediaResponseModel.getData().getMedia().get(position).getFileName() != null){
                            base_url += mediaResponseModel.getData().getMedia().get(position).getFileName();
                            loadImage(base_url,viewHolder.binding.mediaImage);
                        }
                    }



                    viewHolder.binding.playButton.setOnClickListener(v -> {

                        context.startActivity(new Intent(context, VideoActivity.class)
                                .putExtra("videourl", base_url)
                                .putExtra("start_time", "0")
                                .putExtra("end_time", "0"));
                    });
                }
            } else {
                DocumentViewHolder viewHolder = (DocumentViewHolder) holder;
                if (mediaResponseModel.getData().getDocument() != null && !mediaResponseModel.getData().getDocument().isEmpty()) {

                    if (mediaResponseModel.getData().getDocument().get(position).getFileName() != null) {
                        viewHolder.binding.documentName.setText(mediaResponseModel.getData().getDocument().get(position).getFileName());
                    }
                }
            }

        }
    }





    private void loadImage(String imagePath, ImageView imageView){


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

        Glide.with(context).load(imagePath).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }




    @Override
    public int getItemCount() {
        /*if (viewType == 2){
            return mediaResponseModel.getData().getMedia().size();
        }*/
        if (viewType == 3) {
            if (mediaResponseModel != null && mediaResponseModel.getData() != null && mediaResponseModel.getData().getMedia() != null && !mediaResponseModel.getData().getMedia().isEmpty()) {
                return mediaResponseModel.getData().getMedia().size();
            }
            else{
                return 0;
            }
        }
        else {
            if (mediaResponseModel != null && mediaResponseModel.getData() != null && mediaResponseModel.getData().getDocument() != null && !mediaResponseModel.getData().getDocument().isEmpty()) {
                return mediaResponseModel.getData().getDocument().size();
            }
            else {
                return 0;
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {

        FragmentUserDocumentItemBinding binding;

        DocumentViewHolder(@NonNull FragmentUserDocumentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    /*static class LinkViewHolder extends RecyclerView.ViewHolder {

        FragmentUserLinkItemBinding binding;

        LinkViewHolder(@NonNull FragmentUserLinkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }*/
    static class MediaViewHolder extends RecyclerView.ViewHolder {

        FragmentMediaListItemBinding binding;

        MediaViewHolder(@NonNull FragmentMediaListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}

