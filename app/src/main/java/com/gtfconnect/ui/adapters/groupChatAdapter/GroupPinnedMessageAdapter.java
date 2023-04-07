package com.gtfconnect.ui.adapters.groupChatAdapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.medialibrary.VideoActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentPinnedMessageBinding;
import com.gtfconnect.interfaces.PinnedMessageListener;
import com.gtfconnect.models.PinnedMessagesModel;
import com.gtfconnect.ui.screenUI.groupModule.GroupCommentScreen;
import com.gtfconnect.ui.screenUI.groupModule.MultiPreviewImage;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class GroupPinnedMessageAdapter extends RecyclerView.Adapter<GroupPinnedMessageAdapter.ViewHolder> {

    private Context context;
    private List<PinnedMessagesModel.Datum> list;

    private PinnedMessageListener pinnedMessageListener;

    private boolean isMessageClicked = false;

    String post_base_url= "";

    String userName = "";
    String message = "";
    String time = "";

    public GroupPinnedMessageAdapter(Context context,List<PinnedMessagesModel.Datum> list,PinnedMessageListener pinnedMessageListener,String post_base_url){
        this.context = context;
        this.list = list;
        this.pinnedMessageListener = pinnedMessageListener;
        this.post_base_url = post_base_url;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentPinnedMessageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        final int position = index;

        if (list.get(position).getChat().getUser() != null) {
            if (list.get(position).getChat().getUser().getFirstname() == null && list.get(position).getChat().getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.firstName.setText("Bot");
            } else {

                userName = list.get(position).getChat().getUser().getFirstname() + " " + list.get(position).getChat().getUser().getLastname();
                holder.binding.firstName.setText(list.get(position).getChat().getUser().getFirstname());
                holder.binding.lastName.setText(list.get(position).getChat().getUser().getLastname());
            }
        }

        if (list.get(position).getChat().getMedia() !=null && !list.get(position).getChat().getMedia().isEmpty())
        {

            String fileType = Utils.checkFileType(list.get(position).getChat().getMedia().get(0).getMimeType());

            if (fileType.equalsIgnoreCase("audio")){

                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                holder.binding.postImageContainer.setVisibility(View.GONE);
                holder.binding.message.setVisibility(View.GONE);
                holder.binding.headerDivider.setVisibility(View.GONE);

                holder.binding.audioContainer.setVisibility(View.VISIBLE);


                holder.binding.waveForm.setProgressInPercentage(100);
                holder.binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                holder.binding.waveForm.setProgressInPercentage(0);

                if (list.get(position).isAudioDownloaded()){
                    holder.binding.downloadAudio.setVisibility(View.GONE);
                    holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                    String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID().toString());

                    long duration = AudioPlayUtil.getAudioDuration(filePath);
                    String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                    holder.binding.totalAudioTime.setText("/" + totalTime);
                }
                else {
                    if (AudioPlayUtil.checkFileExistence(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID().toString())) {

                        holder.binding.downloadAudio.setVisibility(View.GONE);
                        holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                        String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID().toString());

                        long duration = AudioPlayUtil.getAudioDuration(filePath);
                        String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                        holder.binding.totalAudioTime.setText("/" + totalTime);
                    } else {
                        holder.binding.audioTimeContainer.setVisibility(View.GONE);

                        holder.binding.downloadAudio.setVisibility(View.VISIBLE);
                        holder.binding.playPauseRecordedAudio.setVisibility(View.GONE);
                    }
                }
            }
            else {
                holder.binding.postImageContainer.setVisibility(View.VISIBLE);

                // Todo : Uncomment below code once get thumbnail for the video and remove below line -----------------
                loadPostMedia(holder, position, list.get(position).getChat().getMedia().size());
            }
        }
        else{
            holder.binding.audioContainer.setVisibility(View.GONE);
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }

        if (list.get(position).getChat().getMessage() != null) {
            message = list.get(position).getChat().getMessage();
            holder.binding.message.setText(list.get(position).getChat().getMessage());
        } else {
            message = "No message found";
            holder.binding.message.setText("No message found");
        }

        if (list.get(position).getCreatedAt() != null) {
            time = Utils.getHeaderDate(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getHeaderDate(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }


        holder.binding.message.setOnClickListener(view -> {
            if(isMessageClicked){
                //This will shrink textview to 2 lines if it is expanded.
                holder.binding.message.setMaxLines(3);
                isMessageClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                holder.binding.message.setMaxLines(Integer.MAX_VALUE);
                isMessageClicked = true;
            }
        });



        // Bottom-sheet for image options --

        holder.binding.postImageContainer.setOnLongClickListener(view -> {
            BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
            chat_options_dialog.setContentView(R.layout.bottomsheet_post_action_options2);
            chat_options_dialog.show();
            return false;
        });

        holder.binding.postImageContainer.setOnClickListener(view -> {
            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getChat().getMedia());

            Intent intent = new Intent(context, MultiPreviewImage.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getChat().getUser().getFirstname()+" "+list.get(position).getChat().getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });

        holder.binding.removeMessage.setOnClickListener(view -> {
            pinnedMessageListener.deleteSinglePinMessage(list.get(position).getPinmessagesID());
        });


        holder.binding.goToMessage.setOnClickListener(view -> pinnedMessageListener.gotoMessage(list.get(position).getGroupChatID().toString()));
    }


    private void loadPostMedia(GroupPinnedMessageAdapter.ViewHolder holder, int index, int media_count)
    {


        String fileType= "";

        String post_path = "";


        switch (media_count) {
            case 1:
                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(0).getStoragePath() + list.get(index).getChat().getMedia().get(0).getFileName();

                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.VISIBLE);



                if (fileType.equalsIgnoreCase("image"))
                {

                    Log.d("Post Main Url", post_path);

                    holder.binding.playVideo.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.postImage);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playVideo.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.postImage);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playVideo.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.postImage);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }
                break;

            case 2:
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer.setVisibility(View.GONE);
                holder.binding.dualPostImageContainer.setVisibility(View.VISIBLE);



                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(0).getStoragePath() + list.get(index).getChat().getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.dualPost1);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo1.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.dualPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(1).getStoragePath() + list.get(index).getChat().getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo2.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.dualPost2);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo2.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo2.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.dualPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }

                break;
            case 3:
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.additionalImageCount.setVisibility(View.GONE);

                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);




                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(0).getStoragePath() + list.get(index).getChat().getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost1);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(1).getStoragePath() + list.get(index).getChat().getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost2);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo4.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(2).getStoragePath() + list.get(index).getChat().getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost3);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost3);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo5.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost3);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }




                    /*post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                    Log.d("Post1 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost1);

                    post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();
                    Log.d("Post2 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost2);

                    post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();
                    Log.d("Post3 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost3);*/

                break;

            default:
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.additionalImageCount.setVisibility(View.VISIBLE);

                holder.binding.additionalImageCount.setText("+ " + (media_count - 3));

                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(0).getStoragePath() + list.get(index).getChat().getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost1);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(1).getStoragePath() + list.get(index).getChat().getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost2);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo4.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getChat().getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getChat().getMedia().get(2).getStoragePath() + list.get(index).getChat().getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost3);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost3);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo5.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost3);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }
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

        Glide.with(context).load(imageFilePath).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    private void loadVideoFile(String videoFilePath, ImageView imageView)
    {
        /*context.startActivity(new Intent(context, VideoActivity.class)
                .putExtra("videourl",videoFilePath)
                .putExtra("start_time","0")
                .putExtra("end_time","0"));*/
    }


    private void loadDocumentFile(String docFilePath, ImageView imageView)
    {

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPinnedMessageBinding binding;

        ViewHolder(@NonNull FragmentPinnedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

