package com.gtfconnect.ui.adapters.channelModuleAdapter;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChannelChatItemBinding;
import com.gtfconnect.interfaces.ChannelChatListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.TextViewUtil;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.List;

public class ChannelChatAdapter extends RecyclerView.Adapter<ChannelChatAdapter.ViewHolder> {

    private List<ChannelRowListDataModel> list;
    private Context context;

    private ChannelRowListDataModel item;

    private JSONObject jsonRawObject;

    private String userID;

    private boolean isDayTagShown = false;

    private ChannelChatListener channelChatListener;

    private boolean isMessageClicked = false;

    private int messageUserID = 0;

    private String messageTime = "";

    private int selectedPostCount ;

    private ViewHolder holder;


    private boolean isMultipleMessageSelectionEnabled = false;



    String userName = "";
    String message = "";
    String time = "";

    String post_base_url= "";

    String profileBaseUrl = "";

    private InfoDbEntity infoDbEntity;


    private int previewVideoPlayBackPosition = -1;
    private ExoPlayer videoPlayer;


    private boolean isAudioPlaying = false;





    private boolean isReactionEnabled = false;

    private boolean isSharingEnabled = false;

    private boolean isPinMessage = true;

    private boolean isQuoteMessage = true;


    private boolean isVideoAutoPlay;

    private boolean isGifAutoPlay;

    private boolean isEnableDisableDiscussion;
    private boolean isDiscussionAllowed = false;



//    private int commentCount;

    // ArrayList<Boolean> isMessageLiked = new ArrayList<>();

    public ChannelChatAdapter(Context context, List<ChannelRowListDataModel> list, String userID, String post_base_url, String profileBaseUrl, InfoDbEntity infoDbEntity, ChannelChatListener channelChatListener,boolean isVideoAutoPlay, boolean isGifAutoPlay, boolean isEnableDisableDiscussion) {
        this.list = list;
        this.context = context;
        this.userID = userID;
        this.channelChatListener = channelChatListener;
        this.post_base_url = post_base_url;
        this.profileBaseUrl = profileBaseUrl;

        this.infoDbEntity = infoDbEntity;

        this.isGifAutoPlay =  isGifAutoPlay;
        this.isVideoAutoPlay = isVideoAutoPlay;

        this.isEnableDisableDiscussion = isEnableDisableDiscussion;


        Log.d("autoPlayTrigger","this = "+this.isVideoAutoPlay);
        Log.d("autoPlayTrigger","adap = "+isVideoAutoPlay);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(RecyclerChannelChatItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {

        //Log.d("post_container_visibility",""+showPostSelection);

        final int position = index;

        setUserPermissions();

        if (isSharingEnabled){
            holder.binding.forward.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.forward.setVisibility(View.GONE);
        }


        boolean isLikeWithReaction = false;




        // ----------------------------------------------------------------- Getting System Current Date and time-----------------------------------------------------

        if (list.get(position) != null){
            if (list.get(position).getUpdatedAt()!=null && !list.get(position).getUpdatedAt().isEmpty()){
                if (Utils.getHeaderDate(list.get(position).getUpdatedAt()).equalsIgnoreCase("Today")){

                    /*holder.binding.currentHeaderDate.setVisibility(View.VISIBLE);
                    holder.binding.currentDate.setText(Utils.getHeaderDate(list.get(position).getUpdatedAt()));*/
                }

            }
        }



        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));

        if (list.get(position).getUser() != null) {
            if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.userName.setText("Bot");
            } else {

                //Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                int userId= Integer.parseInt(list.get(position).getUser().getUserID());

                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                    userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname()+" (You)";
                    holder.binding.userName.setText(userName);
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





        // ---------------------------------------------------------- To Find If message send by same user or not. --------------------------------------------
        if (list.get(position) != null) {
            if (list.get(position).getUser() != null) {
                if (list.get(position).getUser().getUserID() != null) {

                    messageUserID = Integer.parseInt(list.get(position).getUser().getUserID());


                    if (position + 1 < list.size()) {
                        if (list.get(position + 1) != null) {
                            if (list.get(position + 1).getUser() != null) {
                                if (list.get(position + 1).getUser().getUserID() != null) {
                                    int nextUserID = Integer.parseInt(list.get(position + 1).getUser().getUserID());

                                    if (messageUserID == nextUserID) {
                                        holder.binding.memberProfileContainer.setVisibility(View.GONE);
                                        /*holder.binding.userIcon.setVisibility(View.GONE);
                                        holder.binding.userName.setVisibility(View.GONE);*/
                                    } else {
                                        holder.binding.memberProfileContainer.setVisibility(View.VISIBLE);
                                        /*holder.binding.userIcon.setVisibility(View.VISIBLE);
                                        holder.binding.userName.setVisibility(View.VISIBLE);*/
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




        // ---------------------------------------------------------- To Find If message date of chat. --------------------------------------------
        if (list.get(position) != null) {
            if (list.get(position).getUser() != null) {
                if (list.get(position).getUser().getUserID() != null) {

                    messageTime = Utils.getChipDate(list.get(position).getUpdatedAt());


                    if (position + 1 < list.size()) {
                        if (list.get(position + 1) != null) {
                            if (list.get(position + 1).getUser() != null) {
                                if (list.get(position + 1).getUser().getUserID() != null) {
                                    String nextMessageTime = Utils.getChipDate(list.get(position+1).getUpdatedAt());

                                    if (messageTime.equalsIgnoreCase(nextMessageTime)) {
                                        holder.binding.currentHeaderDate.setVisibility(View.GONE);
                                    } else {
                                        holder.binding.currentHeaderDate.setVisibility(View.VISIBLE);
                                        holder.binding.currentDate.setText(Utils.getChipDate(list.get(position).getUpdatedAt()));
                                        messageTime = "";
                                    }
                                }
                            }
                        }
                    } else if (position+1 == list.size()) {
                        holder.binding.currentHeaderDate.setVisibility(View.VISIBLE);
                        holder.binding.currentDate.setText(Utils.getChipDate(list.get(position).getUpdatedAt()));
                        messageTime = "";
                    }

                }
            }
        }





        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageContainer.setVisibility(View.GONE);

            boolean isMessageSelfQuoted = false;

            if (list.get(position).getQuote() != null) {
                if (list.get(position).getQuote().getMessage() != null) {

                    holder.binding.oldMessage.setVisibility(View.VISIBLE);
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setCardBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));

                        holder.binding.voiceMessageQuoteContainer.setCardBackgroundColor(context.getColor(R.color.channelQuoteMediaAttachedSentBackgroundColor));

                        isMessageSelfQuoted = true;
                    }
                }
                else{
                    holder.binding.oldMessage.setVisibility(View.GONE);
                    isMessageSelfQuoted = false;
                }
                holder.binding.oldMessage.setTypeface(holder.binding.oldMessage.getTypeface(), Typeface.ITALIC);

                TextViewUtil.channelExpandableMessage(context,holder.binding.oldMessage,list.get(position).getQuote().getMessage(), Constants.CHANNEL_QUOTE_MESSAGE_LIMIT_COUNT,isMessageSelfQuoted);

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser.setText(username);

                holder.binding.oldMsgTime.setText(Utils.getHeaderDate(list.get(position).getQuote().getUpdatedAt()));

                TextViewUtil.channelExpandableMessage(context,holder.binding.newMessage,list.get(position).getMessage(), Constants.CHANNEL_QUOTE_MESSAGE_LIMIT_COUNT,isMessageSelfQuoted);


                if (list.get(position).getQuote().getMedia() != null && !list.get(position).getQuote().getMedia().isEmpty()){

                    int mediaListSize = list.get(position).getQuote().getMedia().size();

                    if (list.get(position).getQuote().getMedia().get(0).getMimeType() != null){

                        String fileType = Utils.checkFileType(list.get(position).getQuote().getMedia().get(0).getMimeType());

                        if (fileType.equalsIgnoreCase("audio")){

                            holder.binding.voiceMessageQuoteContainer.setVisibility(View.VISIBLE);
                            holder.binding.quoteMediaContainer.setVisibility(View.GONE);

                        }
                        else{

                            holder.binding.voiceMessageQuoteContainer.setVisibility(View.GONE);
                            holder.binding.quoteMediaContainer.setVisibility(View.VISIBLE);

                            if (list.get(position).getQuote().getMedia().get(0).getStoragePath() != null && list.get(position).getQuote().getMedia().get(0).getFileName() != null){
                                String media_path = post_base_url + list.get(position).getQuote().getMedia().get(0).getStoragePath() + list.get(position).getQuote().getMedia().get(0).getFileName();
                                GlideUtils.loadImage(context,holder.binding.quotedMediaPreview,media_path);
                            }
                            else{
                                holder.binding.quotedMediaPreview.setImageDrawable(context.getDrawable(R.drawable.no_image_logo_background));
                            }


                            if (mediaListSize > 1){

                                holder.binding.multipleMediaCountContainer.setVisibility(View.VISIBLE);
                                holder.binding.multipleMediaCountContainer.setText("+ "+(mediaListSize-1));
                            }
                            else{
                                holder.binding.multipleMediaCountContainer.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                else{
                    holder.binding.quoteMediaContainer.setVisibility(View.GONE);
                    holder.binding.voiceMessageQuoteContainer.setVisibility(View.GONE);
                }

            }
            else{
                holder.binding.headerDivider.setVisibility(View.VISIBLE);
                holder.binding.messageContainer.setVisibility(View.VISIBLE);
                holder.binding.quoteContainer.setVisibility(View.GONE);
            }
        } else {
            holder.binding.headerDivider.setVisibility(View.VISIBLE);
            holder.binding.messageContainer.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer.setVisibility(View.GONE);
        }






        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            String fileType = Utils.checkFileType(list.get(position).getMedia().get(0).getMimeType());

            if (fileType.equalsIgnoreCase("audio")){

                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                holder.binding.mediaRecycler.setVisibility(View.GONE);
                holder.binding.quoteContainer.setVisibility(View.GONE);
                holder.binding.messageContainer.setVisibility(View.GONE);
                holder.binding.headerDivider.setVisibility(View.GONE);

                holder.binding.audioContainer.setVisibility(View.VISIBLE);


                holder.binding.waveForm.setProgressInPercentage(100);
                holder.binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                holder.binding.waveForm.setProgressInPercentage(0);

                if (list.get(position).isAudioDownloaded()){
                    holder.binding.downloadAudio.setVisibility(View.GONE);
                    holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                    String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID());

                    long duration = AudioPlayUtil.getAudioDuration(filePath);
                    String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                    holder.binding.totalAudioTime.setText("/" + totalTime);
                }
                else {
                    if (AudioPlayUtil.checkFileExistence(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID())) {

                        holder.binding.downloadAudio.setVisibility(View.GONE);
                        holder.binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                        String filePath = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(), list.get(position).getGroupChatID());

                        if (!filePath.trim().isEmpty()) {
                            long duration = AudioPlayUtil.getAudioDuration(filePath);
                            String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                            holder.binding.totalAudioTime.setText("/" + totalTime);

                        }
                        else{
                            holder.binding.audioTimeContainer.setVisibility(View.GONE);

                            holder.binding.downloadAudio.setVisibility(View.VISIBLE);
                            holder.binding.playPauseRecordedAudio.setVisibility(View.GONE);
                        }
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

//                    if (!fileType.equalsIgnoreCase("video")) {


                if (list.get(position).getMedia().size() == 1 && fileType.equalsIgnoreCase("video")){

                    if (isVideoAutoPlay){

                        String video_image_thumbnail = post_base_url + list.get(position).getMedia().get(0).getStoragePath() + list.get(position).getMedia().get(0).getThumbnail();

                        holder.binding.singleVideoPlayerContainer.setVisibility(View.VISIBLE);
                        loadImageFile(video_image_thumbnail,holder.binding.videoThumbnail);

                        if (position == 0) {
                            String post_path = post_base_url + list.get(position).getMedia().get(0).getStoragePath() + list.get(position).getMedia().get(0).getFileName();
                            channelChatListener.autoPlayVideo(position,post_path, holder.binding.playerView, holder.binding.progressBar);
                        }
                    }
                    else{

                        holder.binding.singleVideoPlayerContainer.setVisibility(View.GONE);

                        ChannelMediaAdapter mediaAdapter = new ChannelMediaAdapter(context, list.get(position).getMedia(), post_base_url, String.valueOf(userID), userName, videoPlayer, isVideoAutoPlay, isGifAutoPlay);
                        holder.binding.mediaRecycler.setHasFixedSize(true);
                        holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        holder.binding.mediaRecycler.setAdapter(mediaAdapter);

                        mediaAdapter.setOnMediaPlayPauseListener((exoPlayer) -> {
                            videoPlayer = exoPlayer;
                        });
                    }


                }
                else {

                    holder.binding.singleVideoPlayerContainer.setVisibility(View.GONE);

                    ChannelMediaAdapter mediaAdapter = new ChannelMediaAdapter(context, list.get(position).getMedia(), post_base_url, String.valueOf(userID), userName, videoPlayer, isVideoAutoPlay, isGifAutoPlay);
                    holder.binding.mediaRecycler.setHasFixedSize(true);
                    holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    holder.binding.mediaRecycler.setAdapter(mediaAdapter);

                    mediaAdapter.setOnMediaPlayPauseListener((exoPlayer) -> {
                        videoPlayer = exoPlayer;
                    });
                }
             //   }

            }
            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.audioContainer.setVisibility(View.GONE);
            holder.binding.mediaRecycler.setVisibility(View.GONE);
            holder.binding.singleVideoPlayerContainer.setVisibility(View.GONE);
        }





        holder.binding.memberProfileContainer.setOnClickListener(view -> {

            int userId= Integer.parseInt(list.get(position).getUser().getUserID());
            if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) != userId) {
                channelChatListener.viewMemberProfile(Integer.parseInt(userID),list.get(position).getGCMemberID(),Integer.parseInt(list.get(position).getGroupChatID()),list.get(position).getGroupChannelID());
            }
            else{
                channelChatListener.viewSelfProfile();
            }
        });




        holder.binding.playPauseRecordedAudio.setOnClickListener(view -> {
            if(AudioPlayUtil.checkFileExistence(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID())){
                String path = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID());
                long duration = AudioPlayUtil.getAudioDuration(path);
                channelChatListener.playAudio(path,holder.binding.waveForm,duration);
            }
        });

        holder.binding.downloadAudio.setOnClickListener(view -> {
            String filePath = post_base_url+list.get(position).getMedia().get(0).getStoragePath()+list.get(position).getMedia().get(0).getFileName();
            channelChatListener.downloadAudio(filePath,list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID(),holder.binding.waveForm,holder.binding.downloaderLoader,holder.binding.playPauseRecordedAudio);
        });


        if (list.get(position).getMessage() != null && !list.get(position).getMessage().trim().isEmpty()) {

            holder.binding.message.setVisibility(View.VISIBLE);
            message = list.get(position).getMessage();

            TextViewUtil.channelExpandableMessage(context,holder.binding.message,list.get(position).getMessage(),Constants.CHANNEL_MESSAGE_LIMIT_COUNT,false);

        } else {
            holder.binding.message.setVisibility(View.GONE);
        }




        if (list.get(position).getReactionsCount() != null && list.get(position).getReactionsCount() > 0){
            holder.binding.reactionCount.setText(String.valueOf(list.get(position).getReactionsCount()));
        }
        else{
            holder.binding.reactionCount.setVisibility(View.GONE);
        }





        if (list.get(position).getUpdatedAt() != null) {
            time = Utils.getHeaderDate(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }


        /**
         * Below code for allow discussion checks
         */


        if (list.get(position).getAllowDiscussion() != null && list.get(position).getAllowDiscussion() == 1){
            isDiscussionAllowed = true;
            holder.binding.comment.setVisibility(View.VISIBLE);
        }
        else{
            isDiscussionAllowed = true;
            holder.binding.comment.setVisibility(View.VISIBLE);
        }





        /**
         *  Below checks for footer reaction and comment counts
         */


        holder.binding.reactionCommentFooter.setVisibility(View.VISIBLE);
        boolean isAnyFooterDataAvailable = false;


        if(list.get(position).getLastThreeData() != null && !list.get(position).getLastThreeData().isEmpty()){

            isAnyFooterDataAvailable = true;

            switch (list.get(position).getLastThreeData().size()){
                case 1:

                    holder.binding.emoji1.setVisibility(View.VISIBLE);
                    holder.binding.emoji2.setVisibility(View.GONE);
                    holder.binding.emoji3.setVisibility(View.GONE);

                    if (list.get(position).getLastThreeData().get(0).getReaction() != null && list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode() != null){
                        holder.binding.emoji1.setText(list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji1.setText("👍");
                    }
                    break;
                case 2:

                    holder.binding.emoji1.setVisibility(View.VISIBLE);
                    holder.binding.emoji2.setVisibility(View.VISIBLE);
                    holder.binding.emoji3.setVisibility(View.GONE);

                    if (list.get(position).getLastThreeData().get(0).getReaction() != null && list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode() != null){
                        holder.binding.emoji1.setText(list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji1.setText("👍");
                    }

                    if (list.get(position).getLastThreeData().get(1).getReaction() != null && list.get(position).getLastThreeData().get(1).getReaction().getEmojiCode() != null){
                        holder.binding.emoji2.setText(list.get(position).getLastThreeData().get(1).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji2.setText("👍");
                    }


                    break;
                case 3:
                default:

                    holder.binding.emoji1.setVisibility(View.VISIBLE);
                    holder.binding.emoji2.setVisibility(View.VISIBLE);
                    holder.binding.emoji3.setVisibility(View.VISIBLE);

                    if (list.get(position).getLastThreeData().get(0).getReaction() != null && list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode() != null){
                        holder.binding.emoji1.setText(list.get(position).getLastThreeData().get(0).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji1.setText("👍");
                    }
                    if (list.get(position).getLastThreeData().get(1).getReaction() != null && list.get(position).getLastThreeData().get(1).getReaction().getEmojiCode() != null){
                        holder.binding.emoji2.setText(list.get(position).getLastThreeData().get(1).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji2.setText("👍");
                    }
                    if (list.get(position).getLastThreeData().get(2).getReaction() != null && list.get(position).getLastThreeData().get(2).getReaction().getEmojiCode() != null){
                        holder.binding.emoji3.setText(list.get(position).getLastThreeData().get(2).getReaction().getEmojiCode());
                    }
                    else{
                        holder.binding.emoji3.setText("👍");
                    }
                    break;
            }
        }
        else{
            holder.binding.emoji1.setVisibility(View.GONE);
            holder.binding.emoji2.setVisibility(View.GONE);
            holder.binding.emoji3.setVisibility(View.GONE);
        }


        /**
         *  - Comment Section
         */


        if (list.get(position).getCommentData() == null) {

            holder.binding.commentContainer.setVisibility(View.GONE);
            holder.binding.singleCommentContainer.setVisibility(View.GONE);
            holder.binding.singleCommentContainerDivider.setVisibility(View.GONE);

        } else if (list.get(position).getCommentData().size() == 0) {
            holder.binding.commentContainer.setVisibility(View.GONE);
            holder.binding.singleCommentContainer.setVisibility(View.GONE);
            holder.binding.singleCommentContainerDivider.setVisibility(View.GONE);
        }
        else {

            isAnyFooterDataAvailable = true;

            holder.binding.singleCommentContainer.setVisibility(View.VISIBLE);
            holder.binding.singleCommentContainerDivider.setVisibility(View.VISIBLE);

            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText(String.valueOf(list.get(position).getCommentData().size()));


            if (list.get(position).getCommentData().get(0).getComment() != null) {
                holder.binding.commentText.setText(list.get(position).getCommentData().get(0).getComment());
            }

            if (list.get(position).getCommentData().get(0).getUser() != null) {

                int commentUserID = list.get(position).getCommentData().get(0).getUserID();

                    if (PreferenceConnector.readInteger(context, PreferenceConnector.CONNECT_USER_ID, 0) == commentUserID) {

                        if (list.get(position).getCommentData().get(0).getUser().getFirstname() != null && list.get(position).getCommentData().get(0).getUser().getLastname() != null) {
                            String commentUserName = list.get(position).getCommentData().get(0).getUser().getFirstname() + " " + list.get(position).getCommentData().get(0).getUser().getLastname() + " (You)";
                            holder.binding.commentUser.setText(commentUserName);
                        }

                        holder.binding.editContainer.setVisibility(View.VISIBLE);

                    } else {
                        if (list.get(position).getCommentData().get(0).getUser().getFirstname() != null && list.get(position).getCommentData().get(0).getUser().getLastname() != null) {
                            String commentUserName = list.get(position).getCommentData().get(0).getUser().getFirstname() + " " +  list.get(position).getCommentData().get(0).getUser().getLastname();
                            holder.binding.commentUser.setText(commentUserName);
                        }

                        holder.binding.editContainer.setVisibility(View.GONE);
                    }


                if (list.get(position).getCommentData().get(0).getUser().getProfileImage() != null){
                    String profileImage = profileBaseUrl + list.get(position).getCommentData().get(0).getUser().getProfileImage();
                    GlideUtils.loadImage(context,holder.binding.commentProfilePic,profileImage);
                }
                else{
                    holder.binding.commentProfilePic.setImageDrawable(context.getDrawable(R.drawable.no_image_logo_background));
                }
            }

            if (list.get(position).getCommentData().get(0).getUpdatedAt() != null){
                String commentTimeStamp = Utils.getCommentMessageTimeStamp(list.get(position).getCommentData().get(0).getUpdatedAt());
                holder.binding.commentDate.setText(commentTimeStamp);
            }
        }


        /**
         * Edit / Delete Comment
         */


        holder.binding.edit.setOnClickListener(view -> {
            if (list.get(position).getCommentData().get(0).getComment() != null && list.get(position).getCommentData().get(0).getCommentID() != null) {
                channelChatListener.editComment(position, list.get(position).getCommentData().get(0).getComment(),list.get(position).getCommentData().get(0).getCommentID(),Integer.parseInt(list.get(position).getGroupChatID()));
            }
        });

        holder.binding.delete.setOnClickListener(view -> {
            if (list.get(position).getCommentData().get(0).getCommentID() != null) {


                Dialog delete_comment_dialog = new Dialog(context);

                delete_comment_dialog.setContentView(R.layout.dialog_delete_comment);
                delete_comment_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                delete_comment_dialog.setCancelable(false);
                delete_comment_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView delete = (TextView) delete_comment_dialog.findViewById(R.id.delete);
                TextView cancel = (TextView) delete_comment_dialog.findViewById(R.id.cancel);

                delete.setOnClickListener(view1 -> {
                    channelChatListener.deleteComment(position,list.get(position).getCommentData().get(0).getCommentID(),Integer.parseInt(list.get(position).getGroupChatID()));
                    delete_comment_dialog.dismiss();
                });

                cancel.setOnClickListener(view1 -> {
                    delete_comment_dialog.dismiss();
                });

                delete_comment_dialog.show();
            }
        });




        if (isAnyFooterDataAvailable){
            holder.binding.reactionCommentFooter.setVisibility(View.VISIBLE);
        }
        else {
            holder.binding.reactionCommentFooter.setVisibility(View.GONE);
        }





        if (list.get(position).getLike() != null && !list.get(position).getLike().isEmpty()){

                if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1){

                    if (list.get(position).getLike().get(0).getReaction() != null && list.get(position).getLike().get(0).getReaction().getEmojiCode() != null){

                        holder.binding.likeIcon.setVisibility(View.GONE);
                        holder.binding.reactionIcon.setVisibility(View.VISIBLE);

                        holder.binding.reactionIcon.setText(list.get(position).getLike().get(0).getReaction().getEmojiCode());

                        isLikeWithReaction = true;
                    }
                    else{
                        holder.binding.likeIcon.setVisibility(View.VISIBLE);
                        holder.binding.reactionIcon.setVisibility(View.GONE);

                        isLikeWithReaction = false;

                        holder.binding.likeIcon.setColorFilter(context.getColor(R.color.theme_green));
                    }
                }
                else {

                    holder.binding.likeIcon.setVisibility(View.VISIBLE);
                    holder.binding.reactionIcon.setVisibility(View.GONE);

                    isLikeWithReaction = false;
                    holder.binding.likeIcon.setColorFilter(context.getColor(R.color.chatIconColor));


                }
        }



        holder.binding.quoteMsgContainer.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                channelChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });

        holder.binding.commentContainer.setOnClickListener(view -> {
            channelChatListener.initiateCommentScreen(data,profileBaseUrl,post_base_url,userID,isDiscussionAllowed);
        });


        // Reply into the Chat
        holder.binding.comment.setOnClickListener(view -> {
            channelChatListener.initiateCommentScreen(data,profileBaseUrl,post_base_url,userID,isDiscussionAllowed);
        });

        // Bottom-sheet for chat options --
        holder.binding.bottomsheetChatOption.setOnClickListener(view -> loadBottomSheet(holder,position));


        holder.binding.like.setOnLongClickListener(view -> {

            if (list.get(position).getLike() != null)
            {
                if (list.get(position).getLike().size() != 0)
                {
                    if (list.get(position).getLike().get(0).getIsLike() == 0)
                    {
                        channelChatListener.likeAsEmote(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1,
                                position,
                                holder.binding.like,
                                holder.binding.likeIcon,
                                holder.binding.reactionIcon);
                    }
                    else{
                        channelChatListener.likeAsEmote(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                0,
                                position,
                                holder.binding.like,
                                holder.binding.likeIcon,
                                holder.binding.reactionIcon);
                    }
                }
                else {
                    channelChatListener.likeAsEmote(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1,
                            position,
                            holder.binding.like,
                            holder.binding.likeIcon,
                            holder.binding.reactionIcon);
                }
            }
            else {
                channelChatListener.likeAsEmote(Integer.parseInt(userID),
                        list.get(position).getGroupChannelID(),
                        list.get(position).getGCMemberID(),
                        Integer.parseInt(list.get(position).getGroupChatID()),
                        1,
                        position,
                        holder.binding.like,
                        holder.binding.likeIcon,
                        holder.binding.reactionIcon);
            }

            /*if (isReactionEnabled) {
                channelChatListener.likeAsEmote(position, holder.binding.likeIcon);
            }*/




            return false;
        });

        holder.binding.like.setOnClickListener(view -> {

            if (list.get(position).getLike() != null)
            {
                if (list.get(position).getLike().size() != 0)
                {
                    if (list.get(position).getLike().get(0).getIsLike() == 0)
                    {
                        channelChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1);
                    }
                    else{
                        channelChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                0);
                    }
                }
                else {
                    channelChatListener.likePost(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1);
                }
            }
            else {
                channelChatListener.likePost(Integer.parseInt(userID),
                        list.get(position).getGroupChannelID(),
                        list.get(position).getGCMemberID(),
                        Integer.parseInt(list.get(position).getGroupChatID()),
                        1);
            }


            final ValueAnimator anim = ValueAnimator.ofFloat(1f, 1.5f);
            anim.setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    holder.binding.likeIcon.setScaleX((Float) animation.getAnimatedValue());
                    holder.binding.likeIcon.setScaleY((Float) animation.getAnimatedValue());
                }
            });
            anim.setRepeatCount(1);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.start();
        });


        /**
         * - Forward a message
         * - Save a message
         */


        holder.binding.forward.setOnClickListener(view -> {
            channelChatListener.forwardPost(Integer.parseInt(list.get(position).getGroupChatID()));
        });











        if (list.get(position).isShowPostSelection()){
            holder.binding.selectPost.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.selectPost.setVisibility(View.GONE);
        }
        if (list.get(position).isPostSelected()){
            holder.binding.selectPost.setChecked(list.get(position).isPostSelected());
        }
        else {
            holder.binding.selectPost.setChecked(list.get(position).isPostSelected());
        }

        holder.binding.selectPost.setOnClickListener(view -> {
            if(((CompoundButton) view).isChecked()){

                list.get(position).setPostSelected(true);
                notifyItemChanged(position);

                selectedPostCount++;
                channelChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),true);


            } else {

                selectedPostCount--;
                list.get(position).setPostSelected(false);
                notifyItemChanged(position);

                if (selectedPostCount <= 0){
                    channelChatListener.forwardMultiplePost(-1,Integer.parseInt(list.get(position).getGroupChatID()),false);
                    channelChatListener.toggleMultipleMessageSelection(false);

                }
                else{
                    channelChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),false);

                }

            }
        });
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

        RecyclerChannelChatItemBinding binding;

        ViewHolder(@NonNull RecyclerChannelChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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
        requestOptions.error(R.drawable.no_image_logo_background);
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();

        Glide.with(context).load(imageFilePath).
                fitCenter().apply(requestOptions).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }




    public String getChipDate(int position)
    {
        String date = "";
        if (list!=null) {
            if (list.size()-1 >= position) {
                if (list.get(position) != null) {
                    if (list.get(position).getUpdatedAt() != null) {
                        date = Utils.getChipDate(list.get(position).getUpdatedAt());
                    }
                }
            }
        }
        else{
            date = "";
        }
        return date;
    }


    private void loadBottomSheet(ViewHolder holder,int position)
    {
        BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
        chat_options_dialog.setContentView(R.layout.bottomsheet_group_chat_actions);

        TextView select = chat_options_dialog.findViewById(R.id.select);

        TextView pin = chat_options_dialog.findViewById(R.id.pin);
        TextView quote = chat_options_dialog.findViewById(R.id.quote);
        TextView copy = chat_options_dialog.findViewById(R.id.copy);
        TextView remove = chat_options_dialog.findViewById(R.id.remove);
        TextView cancel = chat_options_dialog.findViewById(R.id.cancel);

        TextView enable_disable_discussion = chat_options_dialog.findViewById(R.id.enable_disable_discussion);


        assert remove != null;
        if(list.get(position).getUserID() == PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0))
        {
            remove.setVisibility(View.VISIBLE);
        }
        else{
            remove.setVisibility(View.GONE);
        }


        assert copy != null;
        if (list.get(position).getMessage() != null && !list.get(position).getMessage().trim().isEmpty()){
            copy.setVisibility(View.VISIBLE);
        }
        else{
            copy.setVisibility(View.GONE);
        }


        assert pin != null;
        if (isPinMessage){
            pin.setVisibility(View.VISIBLE);
        }
        else{
            pin.setVisibility(View.GONE);
        }
        pin.setOnClickListener(view -> {
            chat_options_dialog.dismiss();


            Integer groupChatId = Integer.parseInt(list.get(position).getGroupChatID());
            channelChatListener.pinMessage(list.get(position).getGCMemberID(),list.get(position).getGroupChannelID(),list.get(position).getUserID(),groupChatId);
        });





        assert enable_disable_discussion != null;
        if (isEnableDisableDiscussion){
            enable_disable_discussion.setVisibility(View.VISIBLE);
        }
        else{
            enable_disable_discussion.setVisibility(View.GONE);
        }




        assert quote != null;
        if (isQuoteMessage){
            quote.setVisibility(View.VISIBLE);
        }
        else{
            quote.setVisibility(View.GONE);
        }
        quote.setOnClickListener(view -> {
            String name = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();

            int mediaCount = 0;
            String previewUrl = "";
            if (list.get(position).getMedia() != null && !list.get(position).getMedia().isEmpty()){
                mediaCount = list.get(position).getMedia().size();

                if (list.get(position).getMedia().get(0).getStoragePath() != null && list.get(position).getMedia().get(0).getFileName() != null){
                    previewUrl = post_base_url + list.get(position).getMedia().get(0).getStoragePath() + list.get(position).getMedia().get(0).getFileName();
                }
            }

            channelChatListener.sendQuotedMessage(holder.binding.getRoot(), list.get(position).getGroupChatID(), list.get(position).getMessage(), name, time,mediaCount,previewUrl);
            chat_options_dialog.dismiss();
        });


        assert select != null;
        select.setOnClickListener(v -> {

            list.get(position).setPostSelected(true);
            channelChatListener.toggleMultipleMessageSelection(true);

            selectedPostCount = 1;
            channelChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),true);

            chat_options_dialog.dismiss();
        });


        copy.setOnClickListener(view -> {
            chat_options_dialog.dismiss();

            Dialog copy_dialog = new Dialog(context);

            copy_dialog.setContentView(R.layout.dialog_message_copy);
            copy_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            copy_dialog.setCancelable(false);
            copy_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            copy_dialog.show();

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Post Message",list.get(position).getMessage());
            clipboard.setPrimaryClip(clip);

            new Handler().postDelayed(copy_dialog::dismiss,1000);
        });


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
                channelChatListener.deletePost(Integer.parseInt(userID),list.get(position).getGCMemberID(),Integer.parseInt(list.get(position).getGroupChatID()),list.get(position).getGroupChannelID());
                delete_post_dialog.dismiss();
            });

            cancel_post.setOnClickListener(view1 -> delete_post_dialog.dismiss());

        });


        assert cancel != null;
        cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

        chat_options_dialog.show();
    }


    public void downloadComplete(String groupChatID){
        for (int i=0;i<list.size();i++)
        {
            if (list.get(i).getGroupChatID().equalsIgnoreCase(groupChatID)){
                list.get(i).setAudioDownloaded(true);
                notifyItemChanged(i);
                break;
            }
        }
    }


    public void setUserPermissions(){

        if (infoDbEntity != null){
            if (infoDbEntity.getGcInfo() != null){

                if (infoDbEntity.getGcSetting() != null){

                    if (infoDbEntity.getGcSetting().getEnableReactions() != null){
                        if (infoDbEntity.getGcSetting().getEnableReactions() == 1){

                            isReactionEnabled = true;
                        }
                        else{
                            isReactionEnabled = false;
                        }
                    }


                    if (infoDbEntity.getGcSetting().getRestrictSharingContent() != null){
                        if (infoDbEntity.getGcSetting().getRestrictSharingContent() == 1){

                            isSharingEnabled = true;
                        }
                        else{
                            isSharingEnabled = false;
                        }
                    }


                }

                if (infoDbEntity.getGcPermission() != null){
                    if (infoDbEntity.getGcPermission().getPinMessage() != null && infoDbEntity.getGcPermission().getPinMessage() == 1){
                        isPinMessage = true;
                    }
                    else{
                        isPinMessage = false;
                    }



                    if (infoDbEntity.getGcPermission().getSendMessage() != null && infoDbEntity.getGcPermission().getSendMessage() == 1){
                        isQuoteMessage = true;
                    }
                    else{
                        isQuoteMessage = false;
                    }
                }
            }
        }


        if (PreferenceConnector.readString(context,PreferenceConnector.USER_TYPE,"").equalsIgnoreCase("super-admin")){
            isQuoteMessage = true;
            isPinMessage = true;
            isReactionEnabled = true;
            isEnableDisableDiscussion = true;
            isSharingEnabled = true;
        }
    }


    public void updateMultipleMessageSelection(List<ChannelRowListDataModel> list){
        this.list = list;
        notifyDataSetChanged();
    }


    public void updateList(List<ChannelRowListDataModel> list,String postBaseUrl,String profileBaseUrl,boolean isVideoAutoPlay,boolean isGifAutoPlay)
    {
        this.list = list;
        this.post_base_url = postBaseUrl;
        this.profileBaseUrl = profileBaseUrl;

        this.isVideoAutoPlay = isVideoAutoPlay;
        this.isGifAutoPlay = isGifAutoPlay;


        notifyDataSetChanged();
    }





    public void updateGcPermission(InfoDbEntity infoDbEntity){
        this.infoDbEntity = infoDbEntity;
        setUserPermissions();
        notifyDataSetChanged();
    }


    public void updateLikeResponse(int position, List<ChannelRowListDataModel> list){
        this.list = list;
        notifyItemChanged(position);
    }


    public void updateChat(List<ChannelRowListDataModel> list){
        this.list = list;
        notifyItemInserted(0);
    }

    public void updateChat(int deletePostPosition,List<ChannelRowListDataModel> list){
        this.list = list;
        notifyItemRemoved(deletePostPosition);
    }

    public void updateChat(List<ChannelRowListDataModel> list, int positionUpdated){
        this.list = list;
        notifyItemChanged(positionUpdated);
    }


    public void updatePostBaseUrl(String post_base_url){
        this.post_base_url = post_base_url;
    }





    public void checkMediaForPlay(int position,RecyclerView recyclerView,View view){

        PlayerView playerView = view.findViewById(R.id.player_view);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        if (list != null && !list.isEmpty() && list.get(position).getMedia() != null && !list.get(position).getMedia().isEmpty()){

            if (list.get(position).getMedia().size() == 1 && Utils.checkFileType(list.get(position).getMedia().get(0).getMimeType()).equalsIgnoreCase("video")){


                String post_path = post_base_url + list.get(position).getMedia().get(0).getStoragePath() + list.get(position).getMedia().get(0).getFileName();

                channelChatListener.autoPlayVideo(position,post_path,playerView,progressBar);
            }
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