package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerSavedMessageListBinding;
import com.gtfconnect.interfaces.SavedMessageListener;
import com.gtfconnect.models.savedMessageModels.SavedMessageResponseModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.TextViewUtil;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedMessageAdapter extends RecyclerView.Adapter<SavedMessageAdapter.ViewHolder> {

        private List<SavedMessageResponseModel.ListData> list;
        private Context context;

        private JSONObject jsonRawObject;

        private String userID;

        private boolean isDayTagShown = false;

        private SavedMessageListener savedMessageListener;
        private boolean isMessageClicked = false;

        private int selectedPostCount;

        String profileBaseUrl = "";

        String userName = "";
        String time = "";

        String post_base_url= "";

        private boolean isAudioPlay = false;

        private InfoDbEntity infoDbEntity;
        private String messageTime = "";

        private int messageUserID;

        private boolean isDummyUser = true;



        private int previewVideoPlayBackPosition = -1;
        private ExoPlayer videoPlayer;


        private boolean isAudioPlaying = false;





        private boolean isReactionEnabled = false;

        private boolean isAllowDiscussion = false;

        private boolean isSharingEnabled = false;

        private boolean isPinMessage = false;

        private boolean isQuoteMessage = false;

        private boolean isVideoAutoPlay = false;

        private boolean isGifAutoPlay = false;


        public SavedMessageAdapter(Context context, List<SavedMessageResponseModel.ListData> list, String userID, String post_base_url, SavedMessageListener savedMessageListener) {
                this.list = list;
                this.context = context;
                this.userID = userID;
                this.savedMessageListener = savedMessageListener;
                this.post_base_url = post_base_url;
                this.profileBaseUrl = profileBaseUrl;



                this.isGifAutoPlay =  isGifAutoPlay;
                this.isVideoAutoPlay = isVideoAutoPlay;

        }

        @NonNull
        @Override
        public SavedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new SavedMessageAdapter.ViewHolder(RecyclerSavedMessageListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SavedMessageAdapter.ViewHolder holder, int index) {


                if (list != null && list.get(index) != null && !list.isEmpty()) {

                        Log.d("groupChatIDList","list ids = "+list.get(index).getUserID());
                        Log.d("groupChatIDList","list ids = "+list.get(index).getUserID());


                        if (list.get(index).getUser() != null && list.get(index).getUser().getUserID() != null) {
                                if (userID.equalsIgnoreCase(String.valueOf(list.get(index).getUser().getUserID()))) {
                                        sentMessageView(holder, index);
                                } else {
                                        receivedMessageView(holder, index);
                                }
                        }
                }
        }


        private void sentMessageView(SavedMessageAdapter.ViewHolder holder, int position){

                Gson gson = new Gson();
                String data = gson.toJson(list.get(position));

                holder.binding.sentMessageContainer.setVisibility(View.VISIBLE);
                holder.binding.receivedMessageContainer.setVisibility(View.GONE);

                holder.binding.sentMessageContainer.setOnLongClickListener(view -> {
                        Log.d("Not going ","why2");
                        loadBottomSheet(holder,position,true);

                        return false;
                });


                holder.binding.playPauseRecordedAudio1.setOnClickListener(view -> {

                        if (isAudioPlay){

                                if (list.get(position).getSavedMessageMedia().get(0).getStoragePath() != null && list.get(position).getSavedMessageMedia().get(0).getFileName() != null ){
                                        String path = post_base_url + list.get(position).getSavedMessageMedia().get(0).getStoragePath() + list.get(position).getSavedMessageMedia().get(0).getFileName();
                                        savedMessageListener.pauseAudio(path,holder.binding.waveForm);
                                }
                                else{
                                        Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
                                }

                                holder.binding.playPauseRecordedAudio1.setImageDrawable(context.getDrawable(R.drawable.play));
                                isAudioPlay = false;
                        }
                        else{

                                if (list.get(position).getSavedMessageMedia().get(0).getStoragePath() != null && list.get(position).getSavedMessageMedia().get(0).getFileName() != null ){
                                        String path = post_base_url + list.get(position).getSavedMessageMedia().get(0).getStoragePath() + list.get(position).getSavedMessageMedia().get(0).getFileName();
                                        savedMessageListener.playAudio(path,holder.binding.waveForm);
                                }
                                else{
                                        Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
                                }


                                holder.binding.playPauseRecordedAudio1.setImageDrawable(context.getDrawable(R.drawable.pause));
                                isAudioPlay = true;
                        }


                });



                if (list.get(position).getUpdatedAt() != null) {
                        time = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                        holder.binding.time1.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                } else {
                        holder.binding.time1.setText("XX/XX/XXXX");
                }






                if (list.get(position).getSavedMessageMedia() !=null && !list.get(position).getSavedMessageMedia().isEmpty()) {

                        String fileType = Utils.checkFileType(list.get(position).getSavedMessageMedia().get(0).getMimeType());

                        if (list.get(position).getContent() == null || list.get(position).getContent().trim().isEmpty()){
                                holder.binding.messageDivider1.setVisibility(View.GONE);
                                holder.binding.message1.setVisibility(View.GONE);
                        }
                        else{
                                holder.binding.messageDivider1.setVisibility(View.VISIBLE);
                                holder.binding.message1.setVisibility(View.VISIBLE);
                        }

                        if (fileType.equalsIgnoreCase("audio")){

                                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                                holder.binding.mediaRecycler1.setVisibility(View.GONE);
                                holder.binding.messageContainer.setVisibility(View.GONE);
                                holder.binding.headerDivider.setVisibility(View.GONE);

                                holder.binding.audioContainer1.setVisibility(View.VISIBLE);


                                holder.binding.waveForm.setProgressInPercentage(100);
                                holder.binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                                holder.binding.waveForm.setProgressInPercentage(0);


                                holder.binding.playPauseRecordedAudio1.setVisibility(View.VISIBLE);
                        }
                        else {

                                holder.binding.audioContainer.setVisibility(View.GONE);
                                holder.binding.mediaRecycler1.setVisibility(View.VISIBLE);

                                SavedMessageMediaAdapter mediaAdapter = new SavedMessageMediaAdapter(context, list.get(position).getSavedMessageMedia(), post_base_url, String.valueOf(userID), userName, videoPlayer, isVideoAutoPlay, isGifAutoPlay);
                                holder.binding.mediaRecycler1.setHasFixedSize(true);
                                holder.binding.mediaRecycler1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                holder.binding.mediaRecycler1.setAdapter(mediaAdapter);

                                mediaAdapter.setOnMediaPlayPauseListener((exoPlayer) -> {
                                        videoPlayer = exoPlayer;
                                });
                        }
                        //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
                }else{
                        holder.binding.audioContainer.setVisibility(View.GONE);
                        holder.binding.mediaRecycler1.setVisibility(View.GONE);
                }


                if (list.get(position).getContent() != null && !list.get(position).getContent().trim().isEmpty()) {
                        holder.binding.message1.setVisibility(View.VISIBLE);

                        TextViewUtil.groupExpandableMessage(context,holder.binding.message1,list.get(position).getContent(),Constants.GROUP_MESSAGE_LIMIT_COUNT,true);

                } else {
                        holder.binding.message1.setVisibility(View.GONE);
                }

                if (list.get(position).getUpdatedAt() != null) {
                        time = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                        holder.binding.time.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                } else {
                        holder.binding.time.setText("XX/XX/XXXX");
                }
        }


        private void receivedMessageView(SavedMessageAdapter.ViewHolder holder, int position){


                Gson gson = new Gson();
                String data = gson.toJson(list.get(position));


                holder.binding.sentMessageContainer.setVisibility(View.GONE);
                holder.binding.receivedMessageContainer.setVisibility(View.VISIBLE);


                holder.binding.receivedMessageContainer.setOnLongClickListener(view -> {
                        Log.d("Not going ","why");
                        loadBottomSheet(holder,position,false);
                        return false;
                });








                if (list.get(position).getUser() != null) {
                        if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                                userName = "Bot";
                                holder.binding.userName.setText("Bot");
                        } else {

                                //Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                                int userId= list.get(position).getUser().getUserID();

                                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                                        userName = "You";
                                        holder.binding.userName.setText("You");
                                }
                                else {
                                        userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                                        holder.binding.userName.setText(userName);
                                }
                        }

                        if (list.get(position).getUser().getProfileImage() != null){
                                String baseUrl = profileBaseUrl + list.get(position).getUser().getProfileImage();
                                GlideUtils.loadImage(context,holder.binding.userIcon,baseUrl);
                        }
                        else {
                                holder.binding.userIcon.setImageResource(R.drawable.no_image_logo_background);
                        }
                }



                /*holder.binding.userContainer.setOnClickListener(view -> {

                        int userId= list.get(position).getUser().getUserID();
                        if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) != userId) {
                                groupChatListener.viewMemberProfile(Integer.parseInt(userID),list.get(position).getGCMemberID(),list.get(position).getGroupChatID(),list.get(position).getGroupChannelID());
                        }
                        else{
                                groupChatListener.viewSelfProfile();
                        }
                });*/


                holder.binding.playPauseRecordedAudio.setOnClickListener(view -> {


                        if (list.get(position).getSavedMessageMedia().get(0).getStoragePath() != null && list.get(position).getSavedMessageMedia().get(0).getFileName() != null ){
                                String path = post_base_url + list.get(position).getSavedMessageMedia().get(0).getStoragePath() + list.get(position).getSavedMessageMedia().get(0).getFileName();
                                savedMessageListener.playAudio(path,holder.binding.waveForm);
                        }
                        else{
                                Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
                        }

                });


                if (list.get(position).getUpdatedAt() != null) {
                        time = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                        holder.binding.time.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                } else {
                        holder.binding.time.setText("XX/XX/XXXX");
                }



                if (list.get(position).getSavedMessageMedia() !=null && !list.get(position).getSavedMessageMedia().isEmpty()) {

                        String fileType = Utils.checkFileType(list.get(position).getSavedMessageMedia().get(0).getMimeType());


                        if (fileType.equalsIgnoreCase("audio")){

                                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                                holder.binding.mediaRecycler.setVisibility(View.GONE);
                                holder.binding.messageContainer.setVisibility(View.GONE);
                                holder.binding.headerDivider.setVisibility(View.GONE);

                                holder.binding.audioContainer.setVisibility(View.VISIBLE);


                                holder.binding.waveForm.setProgressInPercentage(100);
                                holder.binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                                holder.binding.waveForm.setProgressInPercentage(0);

                                holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);
                        }
                        else {

                                holder.binding.audioContainer.setVisibility(View.GONE);
                                holder.binding.mediaRecycler.setVisibility(View.VISIBLE);

                                SavedMessageMediaAdapter mediaAdapter = new SavedMessageMediaAdapter(context, list.get(position).getSavedMessageMedia(), post_base_url, String.valueOf(userID), userName, videoPlayer, isVideoAutoPlay, isGifAutoPlay);
                                holder.binding.mediaRecycler.setHasFixedSize(true);
                                holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                holder.binding.mediaRecycler.setAdapter(mediaAdapter);

                                mediaAdapter.setOnMediaPlayPauseListener((exoPlayer) -> {
                                        videoPlayer = exoPlayer;
                                });


                        }
                        //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
                }else{
                        holder.binding.audioContainer.setVisibility(View.GONE);
                        holder.binding.mediaRecycler.setVisibility(View.GONE);
                }





                // ---------------------------------------------------------- To Find If message send by same user or not. --------------------------------------------
                if (list.get(position) != null) {
                        if (list.get(position).getUser() != null) {
                                if (list.get(position).getUser().getUserID() != null) {

                                        messageUserID = list.get(position).getUser().getUserID();


                                        if (position + 1 < list.size()) {
                                                if (list.get(position + 1) != null) {
                                                        if (list.get(position + 1).getUser() != null) {
                                                                if (list.get(position + 1).getUser().getUserID() != null) {
                                                                        int nextUserID = list.get(position + 1).getUser().getUserID();

                                                                        if (messageUserID == nextUserID) {
                                                                                holder.binding.userContainer.setVisibility(View.GONE);
                                                                        } else {
                                                                                holder.binding.userContainer.setVisibility(View.VISIBLE);
                                                                                messageUserID = 0;
                                                                        }
                                                                }
                                                        }
                                                }
                                        } else if (position+1 == list.size()) {
                                                holder.binding.userIcon.setVisibility(View.VISIBLE);
                                                holder.binding.userName.setVisibility(View.VISIBLE);
                                                messageUserID = 0;
                                        }

                                }
                        }
                }


                if (list.get(position).getContent() != null && !list.get(position).getContent().trim().isEmpty()) {

                        holder.binding.message.setVisibility(View.VISIBLE);
                        TextViewUtil.groupExpandableMessage(context,holder.binding.message,list.get(position).getContent(),Constants.GROUP_MESSAGE_LIMIT_COUNT,false);
                } else {
                        holder.binding.message.setVisibility(View.GONE);
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






        @Override
        public int getItemViewType(int position) {
                return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
                return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

                RecyclerSavedMessageListBinding binding;

                ViewHolder(@NonNull RecyclerSavedMessageListBinding binding) {
                        super(binding.getRoot());
                        this.binding = binding;

                }
        }



        private void loadBottomSheet(SavedMessageAdapter.ViewHolder holder, int position, boolean isSentMessage)
        {
                BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
                chat_options_dialog.setContentView(R.layout.bottomsheet_saved_message_options);

                TextView copy = chat_options_dialog.findViewById(R.id.copy);
                TextView remove = chat_options_dialog.findViewById(R.id.remove);
                TextView forward = chat_options_dialog.findViewById(R.id.forward);
                TextView cancel = chat_options_dialog.findViewById(R.id.cancel);


                assert copy != null;
                if (list.get(position).getContent() != null && !list.get(position).getContent().trim().isEmpty()){
                        copy.setVisibility(View.VISIBLE);
                }
                else{
                        copy.setVisibility(View.GONE);
                }
                copy.setOnClickListener(view -> {
                        chat_options_dialog.dismiss();

                        Dialog copy_dialog = new Dialog(context);

                        copy_dialog.setContentView(R.layout.dialog_message_copy);
                        copy_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        copy_dialog.setCancelable(false);
                        copy_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        copy_dialog.show();

                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clip = ClipData.newPlainText("Saved Message",list.get(position).getContent());
                        clipboard.setPrimaryClip(clip);

                        new Handler().postDelayed(copy_dialog::dismiss,1000);
                });


                assert remove != null;
                remove.setOnClickListener(view ->
                {
                        chat_options_dialog.dismiss();

                        Dialog delete_post_dialog = new Dialog(context);
                        delete_post_dialog.setContentView(R.layout.dialog_delete_post);
                        delete_post_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        delete_post_dialog.setCancelable(false);
                        delete_post_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        delete_post_dialog.show();

                        TextView delete_post = delete_post_dialog.findViewById(R.id.delete);
                        TextView cancel_post = delete_post_dialog.findViewById(R.id.cancel);

                        delete_post.setOnClickListener(view1 -> {
                                savedMessageListener.deleteSavedPost(list.get(position).getSavedMessageID());
                                delete_post_dialog.dismiss();
                        });


                        cancel_post.setOnClickListener(view1 -> delete_post_dialog.dismiss());

                });


                assert  forward != null;
                forward.setOnClickListener(view -> {
                        //Todo
                });



                assert cancel != null;
                cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

                chat_options_dialog.show();
        }



        public String getSavedMessageChipDate(int position)
        {
                String date = "";
                if (list!=null) {
                        if (list.size()-1 >= position) {
                                if (list.get(position) != null) {
                                        if (list.get(position).getUpdatedAt() != null) {
                                                date = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                                        }
                                }
                        }
                }
                else{
                        date = "";
                }
                return date;
        }






        public void updateChat(List<SavedMessageResponseModel.ListData> list){
                this.list = list;
                notifyItemInserted(0);
        }


        public void destroyExoPlayer(){

                if (videoPlayer != null){
                        if (videoPlayer.isPlaying()) {
                                videoPlayer.stop();
                        }
                        videoPlayer.release();
                }
        }


        public void pauseExoPlayer(){
                if (videoPlayer != null){
                        if (videoPlayer.isPlaying()) {
                                videoPlayer.pause();
                        }
                }
        }




}