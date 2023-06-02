package com.gtfconnect.ui.adapters.commonGroupChannelAdapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentPinnedMessageBinding;
import com.gtfconnect.interfaces.PinnedMessageListener;
import com.gtfconnect.models.PinnedMessagesModel;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.TextViewUtil;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupChannelPinnedMessageAdapter extends RecyclerView.Adapter<GroupChannelPinnedMessageAdapter.ViewHolder> {

    private Context context;
    private List<PinnedMessagesModel.AllPinDatum> list;

    private PinnedMessageListener pinnedMessageListener;

    private boolean isMessageClicked = false;

    String post_base_url= "";

    String userName = "";
    String message = "";
    String time = "";

    private int messageUserID = 0;

    private String profileBaseUrl;

    private ExoPlayer videoPlayer;

    private String messageTime = "";

    private boolean isSelfUser = false;



    public GroupChannelPinnedMessageAdapter(Context context, List<PinnedMessagesModel.AllPinDatum> list, PinnedMessageListener pinnedMessageListener, String post_base_url, String profileBaseUrl){
        this.context = context;
        this.list = list;
        this.pinnedMessageListener = pinnedMessageListener;
        this.post_base_url = post_base_url;
        this.profileBaseUrl = profileBaseUrl;
    }

    @NonNull
    @Override
    public GroupChannelPinnedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new GroupChannelPinnedMessageAdapter.ViewHolder(FragmentPinnedMessageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        final int position = index;

        // ----------------------------------------------------------------- Getting System Current Date and time-----------------------------------------------------

        if (list.get(position).getChat() != null){
            if (list.get(position).getChat().getUpdatedAt()!=null && !list.get(position).getChat().getUpdatedAt().isEmpty()){
                if (Utils.getHeaderDate(list.get(position).getChat().getUpdatedAt()).equalsIgnoreCase("Today")){

                    /*holder.binding.currentHeaderDate.setVisibility(View.VISIBLE);
                    holder.binding.currentDate.setText(Utils.getHeaderDate(list.get(position).getChat().getUpdatedAt()));*/
                }

            }
        }


        if (list.get(position).getChat().getUser() != null) {
            if (list.get(position).getChat().getUser().getFirstname() == null && list.get(position).getChat().getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.userName.setText("Bot");
            } else {

                //Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                int userId= Integer.parseInt(list.get(position).getChat().getUser().getUserID());

                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                    userName = list.get(position).getChat().getUser().getFirstname() + " " + list.get(position).getChat().getUser().getLastname()+" (You)";
                    holder.binding.userName.setText(userName);
                }
                else {
                    userName = list.get(position).getChat().getUser().getFirstname() + " " + list.get(position).getChat().getUser().getLastname();
                    holder.binding.userName.setText(userName);
                }
            }

            if (list.get(position).getChat().getUser().getProfileImage() != null){
                String baseUrl = profileBaseUrl + list.get(position).getChat().getUser().getProfileImage();
                GlideUtils.loadImage(context,holder.binding.userIcon,baseUrl);
            }
            else {
                holder.binding.userIcon.setImageResource(R.drawable.no_image_logo_background);
            }
        }


        if (list.get(position).getChat().getMedia() !=null && !list.get(position).getChat().getMedia().isEmpty()) {

            Log.d("chatID","========= "+list.get(position).getChat().getGroupChatID()+" channelID ="+list.get(position).getChat().getGroupChannelID().toString());

            //List<ChannelMediaResponseModel> mediaResponseModel = list.get(position).getChat().getMedia();

            String fileType = Utils.checkFileType(list.get(position).getChat().getMedia().get(0).getMimeType());

            if (fileType.equalsIgnoreCase("audio")){

                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                holder.binding.mediaRecycler.setVisibility(View.GONE);
                holder.binding.messageContainer.setVisibility(View.GONE);
                holder.binding.headerDivider.setVisibility(View.GONE);

                holder.binding.audioContainer.setVisibility(View.VISIBLE);


                holder.binding.waveForm.setProgressInPercentage(100);
                holder.binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                holder.binding.waveForm.setProgressInPercentage(0);

                if (list.get(position).getChat().isAudioDownloaded()){
                    holder.binding.downloadAudio.setVisibility(View.GONE);
                    holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                    String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getChat().getGroupChannelID().toString(), list.get(position).getChat().getGroupChatID());

                    long duration = AudioPlayUtil.getAudioDuration(filePath);
                    String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                    holder.binding.totalAudioTime.setText("/" + totalTime);
                }
                else {
                    if (AudioPlayUtil.checkFileExistence(list.get(position).getChat().getGroupChannelID().toString(), list.get(position).getChat().getGroupChatID())) {

                        holder.binding.downloadAudio.setVisibility(View.GONE);
                        holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                        String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getChat().getGroupChannelID().toString(), list.get(position).getChat().getGroupChatID());

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

                holder.binding.audioContainer.setVisibility(View.GONE);
                holder.binding.mediaRecycler.setVisibility(View.VISIBLE);

                GroupChannelPinnedMessageMediaAdapter mediaAdapter = new GroupChannelPinnedMessageMediaAdapter(context, list.get(position).getChat().getMedia(), post_base_url, String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0)),userName,videoPlayer,isSelfUser);
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




        holder.binding.memberProfileContainer.setOnClickListener(view -> {
            pinnedMessageListener.viewMemberProfile(PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0),list.get(position).getChat().getGCMemberID(),Integer.parseInt(list.get(position).getChat().getGroupChatID()),list.get(position).getChat().getGroupChannelID());
        });





        holder.binding.playPauseRecordedAudio.setOnClickListener(view -> {
            if(AudioPlayUtil.checkFileExistence(list.get(position).getChat().getGroupChannelID().toString(),list.get(position).getChat().getGroupChatID())){
                String path = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getChat().getGroupChannelID().toString(),list.get(position).getChat().getGroupChatID());

                long duration = AudioPlayUtil.getAudioDuration(path);
                pinnedMessageListener.playAudio(path,holder.binding.waveForm,duration);
            }
        });

        holder.binding.downloadAudio.setOnClickListener(view -> {
            String filePath = post_base_url+list.get(position).getChat().getMedia().get(0).getStoragePath()+list.get(position).getChat().getMedia().get(0).getFileName();
            pinnedMessageListener.downloadAudio(filePath,list.get(position).getChat().getGroupChannelID().toString(),list.get(position).getChat().getGroupChatID(),holder.binding.waveForm,holder.binding.downloaderLoader,holder.binding.playPauseRecordedAudio);
        });


        if (list.get(position).getChat().getMessage() != null && !list.get(position).getChat().getMessage().trim().isEmpty()) {

            holder.binding.message.setVisibility(View.VISIBLE);
            message = list.get(position).getChat().getMessage();

            TextViewUtil.channelExpandableMessage(context,holder.binding.message,list.get(position).getChat().getMessage(), Constants.CHANNEL_MESSAGE_LIMIT_COUNT,false);

        } else {
            holder.binding.message.setVisibility(View.GONE);
        }



        if (list.get(position).getChat().getUpdatedAt() != null) {
            time = Utils.getHeaderDate(list.get(position).getChat().getUpdatedAt());
            holder.binding.time.setText(Utils.getHeaderDate(list.get(position).getChat().getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }




        holder.binding.removeMessage.setOnClickListener(view -> {
            pinnedMessageListener.deleteSinglePinMessage(list.get(position).getPinmessagesID());
        });


        holder.binding.goToMessage.setOnClickListener(view -> pinnedMessageListener.gotoMessage(list.get(position).getGroupChatID().toString()));

    }



    public void downloadComplete(String groupChatID){
        for (int i=0;i<list.size();i++)
        {
            String chatID = list.get(i).getGroupChatID().toString();
            if (chatID.equalsIgnoreCase(groupChatID)){
                list.get(i).setAudioDownloaded(true);
                notifyItemChanged(i);
                break;
            }
        }
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

