package com.gtfconnect.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.exa.ashutosh_video.VideoActivity;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChatMediaItemBinding;
import com.gtfconnect.databinding.RecyclerSingleChatMediaItemBinding;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupChannel_MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<GroupChatResponseModel.Medium> mediaList;
    String postBaseUrl;
    String userID;

    RecyclerView recyclerRootView;

    public  GroupChannel_MediaAdapter(Context context,RecyclerView recyclerRootView,List<GroupChatResponseModel.Medium> mediaList,String postBaseUrl,String userID){
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
            return new SingleMediaItemViewHolder(RecyclerSingleChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
        else{
            return new MultiMediaItemViewHolder(RecyclerChatMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String fileType = Utils.checkFileType(mediaList.get(position).getMimeType());
        String post_path = postBaseUrl + mediaList.get(position).getStoragePath() + mediaList.get(position).getFileName();


        if (mediaList.size() == 1){
            SingleMediaItemViewHolder holder1 = (SingleMediaItemViewHolder) holder;


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
                holder1.binding.docName.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                holder1.binding.docName.setVisibility(View.VISIBLE);
                holder1.binding.playVideo.setVisibility(View.GONE);

                loadDocumentFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("video")) {

                holder1.binding.docName.setVisibility(View.GONE);
                holder1.binding.playVideo.setVisibility(View.VISIBLE);
                //loadVideoFile(post_path,holder.binding.postImage);
            }
            else{
                Log.d("File_Type_Error",fileType);
            }


            holder1.binding.playVideo.setOnClickListener(view -> {
                loadVideoFile(post_path,holder1.binding.postImage);
            });
        }
        else{

            MultiMediaItemViewHolder holder1 = (MultiMediaItemViewHolder) holder;

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
                holder1.binding.docName.setVisibility(View.GONE);

                loadImageFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                holder1.binding.docName.setVisibility(View.VISIBLE);
                holder1.binding.playVideo.setVisibility(View.GONE);

                loadDocumentFile(post_path,holder1.binding.postImage);
            }
            else if (fileType.equalsIgnoreCase("video")) {

                holder1.binding.docName.setVisibility(View.GONE);
                holder1.binding.playVideo.setVisibility(View.VISIBLE);
                //loadVideoFile(post_path,holder.binding.postImage);
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

