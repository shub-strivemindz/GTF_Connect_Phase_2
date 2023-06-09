package com.gtfconnect.ui.adapters.groupChatAdapter;

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
import com.gtfconnect.databinding.RecyclerGroupChatBinding;
import com.gtfconnect.interfaces.ChannelChatListener;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelChatAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelMediaAdapter;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.MultiPreviewScreen;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.TextViewUtil;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private ArrayList<ChannelRowListDataModel> list;
    private Context context;

    private GroupChatResponseModel.Row item;

    private JSONObject jsonRawObject;

    private String userID;

    private boolean isDayTagShown = false;

    private GroupChatListener groupChatListener;

    private ViewHolder viewHolder;
    private boolean isMessageClicked = false;

    private int selectedPostCount;

    String profileBaseUrl = "";

    String userName = "";
    String time = "";

    String post_base_url= "";

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


    public GroupChatAdapter(Context context, ArrayList<ChannelRowListDataModel> list, String userID, String post_base_url, String profileBaseUrl, InfoDbEntity infoDbEntity, GroupChatListener groupChatListener,boolean isVideoAutoPlay, boolean isGifAutoPlay) {
        this.list = list;
        this.context = context;
        this.userID = userID;
        this.groupChatListener = groupChatListener;
        this.post_base_url = post_base_url;
        this.profileBaseUrl = profileBaseUrl;
        ///this.commentCount = commentCount;

        this.infoDbEntity = infoDbEntity;

        this.isGifAutoPlay =  isGifAutoPlay;
        this.isVideoAutoPlay = isVideoAutoPlay;

        selectedPostCount = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ViewHolder(RecyclerGroupChatBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        setUserPermissions();

        viewHolder = holder;

        if (list != null && list.get(index) != null && !list.isEmpty()) {


            // ---------------------------------------------------------- To Find If message date of chat. --------------------------------------------
            if (list.get(index) != null) {
                if (list.get(index).getUser() != null) {
                    if (list.get(index).getUser().getUserID() != null) {

                        messageTime = Utils.getChipDate(list.get(index).getUpdatedAt());


                        if (index + 1 < list.size()) {
                            if (list.get(index + 1) != null) {
                                if (list.get(index + 1).getUser() != null) {
                                    if (list.get(index + 1).getUser().getUserID() != null) {
                                        String nextMessageTime = Utils.getChipDate(list.get(index +1).getUpdatedAt());

                                        if (messageTime.equalsIgnoreCase(nextMessageTime)) {
                                            holder.binding.dateChipContainer.setVisibility(View.GONE);
                                        } else {
                                            holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                                            holder.binding.currentDate.setText(Utils.getChipDate(list.get(index).getUpdatedAt()));
                                            messageTime = "";
                                        }
                                    }
                                }
                            }
                        } else if (index +1 == list.size()) {
                            holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                            holder.binding.currentDate.setText(Utils.getChipDate(list.get(index).getUpdatedAt()));
                            messageTime = "";
                        }

                    }
                }
            }

            Log.d("groupChatIDList","list ids = "+list.get(index).getUserID());
            Log.d("groupChatIDList","list ids = "+list.get(index).getUserID());


            if (userID.equalsIgnoreCase(String.valueOf(list.get(index).getUserID()))){
                sentMessageView(holder, index);
            }
            else{
                receivedMessageView(holder, index);
            }
        }
    }


    private void sentMessageView(ViewHolder holder,int position){

        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));

        holder.binding.sentMessageContainer.setVisibility(View.VISIBLE);
        holder.binding.receivedMessageContainer.setVisibility(View.GONE);


        if (isSharingEnabled){
            holder.binding.forward1.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.forward1.setVisibility(View.GONE);
        }


        if (!isAllowDiscussion){
            holder.binding.greenHighlightDivider.setVisibility(View.GONE);
        }
        else{
            holder.binding.greenHighlightDivider.setVisibility(View.VISIBLE);
        }


        if (isDummyUser){
            holder.binding.dummyUserName.setVisibility(View.VISIBLE);
            holder.binding.dummyUserTimeDivider.setVisibility(View.VISIBLE);
            isDummyUser =false ;
        }
        else{
            holder.binding.dummyUserName.setVisibility(View.GONE);
            holder.binding.dummyUserTimeDivider.setVisibility(View.GONE);
        }

        holder.binding.sentMessageContainer.setOnLongClickListener(view -> {
            Log.d("Not going ","why2");
            loadBottomSheet(holder,position,true);

            return false;
        });


        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer1.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageContainer1.setVisibility(View.GONE);

            holder.binding.messageDivider1.setVisibility(View.GONE);
            holder.binding.message1.setVisibility(View.GONE);

            boolean isMessageSelfQuoted = false;

            if (list.get(position).getQuote() != null) {
                if (list.get(position).getQuote().getMessage() != null) {



                    holder.binding.oldMessage1.setVisibility(View.VISIBLE);
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer1.setBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon1.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage1.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage1.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser1.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime1.setTextColor(context.getColor(R.color.white));


                        holder.binding.voiceMessageQuoteContainer1.setCardBackgroundColor(context.getColor(R.color.channelQuoteMediaAttachedSentBackgroundColor));

                        isMessageSelfQuoted = true;
                    }
                }
                else{
                    holder.binding.oldMessage1.setVisibility(View.GONE);
                }
                holder.binding.oldMessage1.setTypeface(holder.binding.oldMessage1.getTypeface(), Typeface.ITALIC);

                holder.binding.oldMessage1.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser1.setText(username);

                holder.binding.oldMsgTime1.setText(Utils.getHeaderDate(list.get(position).getQuote().getUpdatedAt()));

                TextViewUtil.groupExpandableMessage(context,holder.binding.newMessage1,list.get(position).getMessage(), Constants.GROUP_QUOTE_MESSAGE_LIMIT_COUNT,isMessageSelfQuoted);


                if (list.get(position).getQuote().getMedia() != null && !list.get(position).getQuote().getMedia().isEmpty()){

                    int mediaListSize = list.get(position).getQuote().getMedia().size();

                    if (list.get(position).getQuote().getMedia().get(0).getMimeType() != null){

                        String fileType = Utils.checkFileType(list.get(position).getQuote().getMedia().get(0).getMimeType());

                        if (fileType.equalsIgnoreCase("audio")){

                            holder.binding.voiceMessageQuoteContainer1.setVisibility(View.VISIBLE);
                            holder.binding.quoteMediaContainer1.setVisibility(View.GONE);

                        }
                        else{

                            holder.binding.voiceMessageQuoteContainer1.setVisibility(View.GONE);
                            holder.binding.quoteMediaContainer1.setVisibility(View.VISIBLE);

                            if (list.get(position).getQuote().getMedia().get(0).getStoragePath() != null && list.get(position).getQuote().getMedia().get(0).getFileName() != null){
                                String media_path = post_base_url + list.get(position).getQuote().getMedia().get(0).getStoragePath() + list.get(position).getQuote().getMedia().get(0).getFileName();
                                GlideUtils.loadImage(context,holder.binding.quotedMediaPreview1,media_path);
                            }
                            else{
                                holder.binding.quotedMediaPreview1.setImageDrawable(context.getDrawable(R.drawable.no_image_logo_background));
                            }


                            if (mediaListSize > 1){

                                holder.binding.multipleMediaCountContainer1.setVisibility(View.VISIBLE);
                                holder.binding.multipleMediaCountContainer1.setText("+ "+(mediaListSize-1));
                            }
                            else{
                                holder.binding.multipleMediaCountContainer1.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                else{
                    holder.binding.quoteMediaContainer1.setVisibility(View.GONE);
                    holder.binding.voiceMessageQuoteContainer1.setVisibility(View.GONE);
                }

            }
            else{
                holder.binding.headerDivider.setVisibility(View.VISIBLE);
                holder.binding.messageContainer1.setVisibility(View.VISIBLE);
                holder.binding.quoteContainer1.setVisibility(View.GONE);
            }
        } else {

            holder.binding.messageDivider1.setVisibility(View.VISIBLE);
            holder.binding.message1.setVisibility(View.VISIBLE);

            holder.binding.headerDivider.setVisibility(View.VISIBLE);
            holder.binding.messageContainer1.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer1.setVisibility(View.GONE);
        }


        holder.binding.playPauseRecordedAudio1.setOnClickListener(view -> {
            if(AudioPlayUtil.checkFileExistence(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID())){
                String path = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID());
                long duration = AudioPlayUtil.getAudioDuration(path);
                groupChatListener.playAudio(path,holder.binding.waveForm1,duration);
            }
        });

        holder.binding.downloadAudio1.setOnClickListener(view -> {
            String filePath = post_base_url+list.get(position).getMedia().get(0).getStoragePath()+list.get(position).getMedia().get(0).getFileName();
            groupChatListener.downloadAudio(filePath,list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID(),holder.binding.waveForm1,holder.binding.downloaderLoader1,holder.binding.playPauseRecordedAudio1);
        });

        if (list.get(position).getUpdatedAt() != null) {
            time = Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt());
            holder.binding.time1.setText(Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time1.setText("XX/XX/XXXX");
        }

        if (list.get(position).getCommentData() == null || list.get(position).getCommentData().size() == 0) {

            if (isAllowDiscussion) {
                holder.binding.comment1.setVisibility(View.VISIBLE);
                holder.binding.commentCount1.setText("Comment");
            }
            else{
                holder.binding.comment1.setVisibility(View.GONE);
            }
        }
        else {
            String commentCounts = String.valueOf(list.get(position).getCommentData().size());
            holder.binding.commentCount1.setText("("+commentCounts+")");
        }

        if (list.get(position).getLike() != null && list.get(position).getLike().size() != 0) {
            if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1) {
                holder.binding.likeIcon1.setColorFilter(context.getColor(R.color.theme_green));
            }
            else {
                holder.binding.likeIcon1.setColorFilter(context.getColor(R.color.chatIconColor));
            }
        }

        holder.binding.quoteMsgContainer1.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                groupChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });

        holder.binding.comment1.setOnClickListener(view -> {
            groupChatListener.initiateCommentScreen(data,profileBaseUrl,post_base_url,userID,isAllowDiscussion);
        });



        holder.binding.like1.setOnLongClickListener(view -> {

            if (isReactionEnabled) {
                if (list.get(position).getLike() != null) {
                    if (list.get(position).getLike().size() != 0) {
                        if (list.get(position).getLike().get(0).getIsLike() == 0) {
                            groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    1,
                                    position,
                                    holder.binding.like1,
                                    holder.binding.likeIcon1,
                                    holder.binding.reactionIcon1);
                        } else {
                            groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    0,
                                    position,
                                    holder.binding.like1,
                                    holder.binding.likeIcon1,
                                    holder.binding.reactionIcon1);
                        }
                    } else {
                        groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1,
                                position,
                                holder.binding.like1,
                                holder.binding.likeIcon1,
                                holder.binding.reactionIcon1);
                    }
                } else {
                    groupChatListener.likeAsEmote(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1,
                            position,
                            holder.binding.like1,
                            holder.binding.likeIcon1,
                            holder.binding.reactionIcon1);
                }
            }

            return false;
        });




        holder.binding.like1.setOnClickListener(view -> {

            if (list.get(position).getLike() != null)
            {
                if (list.get(position).getLike().size() != 0)
                {
                    if (list.get(position).getLike().get(0).getIsLike() == 0)
                    {
                        groupChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1);
                    }
                    else{
                        groupChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                0);
                    }
                }
                else {
                    groupChatListener.likePost(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1);
                }
            }
            else {
                groupChatListener.likePost(Integer.parseInt(userID),
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
                    holder.binding.likeIcon1.setScaleX((Float) animation.getAnimatedValue());
                    holder.binding.likeIcon1.setScaleY((Float) animation.getAnimatedValue());
                }
            });
            anim.setRepeatCount(1);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.start();
        });

        holder.binding.forward1.setOnClickListener(view -> {
            Dialog forward_dialog = new Dialog(context);

            forward_dialog.setContentView(R.layout.dialog_forward_message);
            forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
            close.setOnClickListener(view1 -> forward_dialog.dismiss());


            RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);

            int channelID = list.get(position).getGroupChannelID();
            int chatID = Integer.parseInt(list.get(position).getGroupChatID());

            ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(context,channelID,chatID);
            personList.setHasFixedSize(true);
            personList.setLayoutManager(new LinearLayoutManager(context));
            personList.setAdapter(forwardPersonListAdapter);

            forwardPersonListAdapter.setOnSaveMessageClickListener(chatID1 -> {
                groupChatListener.saveMessage(chatID1);
                forward_dialog.dismiss();
            });

            forward_dialog.show();
        });


        if (list.get(position).isShowPostSelection()){
            holder.binding.selectPost1.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.selectPost1.setVisibility(View.GONE);
        }
        if (list.get(position).isPostSelected()){
            holder.binding.selectPost1.setChecked(list.get(position).isPostSelected());
        }
        else {
            holder.binding.selectPost1.setChecked(list.get(position).isPostSelected());
        }

        holder.binding.selectPost1.setOnClickListener(view -> {
            if(((CompoundButton) view).isChecked()){

                list.get(position).setPostSelected(true);
                notifyItemChanged(position);

                selectedPostCount++;
                groupChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),true);


            } else {
                selectedPostCount--;
                list.get(position).setPostSelected(false);
                notifyItemChanged(position);

                if (selectedPostCount <= 0){
                    groupChatListener.forwardMultiplePost(-1,Integer.parseInt(list.get(position).getGroupChatID()),false);
                    groupChatListener.toggleMultipleMessageSelection(false);

                }
                else{
                    groupChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),false);

                }

            }
        });

        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            String fileType = Utils.checkFileType(list.get(position).getMedia().get(0).getMimeType());

            if (list.get(position).getMessage() == null || list.get(position).getMessage().trim().isEmpty()){
                holder.binding.messageDivider1.setVisibility(View.GONE);
                holder.binding.message1.setVisibility(View.GONE);
            }
            else{
                holder.binding.messageDivider1.setVisibility(View.VISIBLE);
                holder.binding.message1.setVisibility(View.VISIBLE);
            }

            if (fileType.equalsIgnoreCase("audio")){

                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                holder.binding.postImageContainer1.setVisibility(View.GONE);
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
                holder.binding.postImageContainer1.setVisibility(View.VISIBLE);

                loadSentPostMedia(holder, position, list.get(position).getMedia().size());
            }
            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
        }else{
            holder.binding.audioContainer.setVisibility(View.GONE);
            holder.binding.postImageContainer1.setVisibility(View.GONE);
        }


        if (list.get(position).getMessage() != null && !list.get(position).getMessage().trim().isEmpty()) {
            holder.binding.message1.setVisibility(View.VISIBLE);

            TextViewUtil.groupExpandableMessage(context,holder.binding.message1,list.get(position).getMessage(),Constants.GROUP_MESSAGE_LIMIT_COUNT,true);

        } else {
            holder.binding.message1.setVisibility(View.GONE);
        }

        if (list.get(position).getUpdatedAt() != null) {
            time = Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }


        holder.binding.postImageContainer1.setOnClickListener(view -> {

            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getMedia());

            Intent intent = new Intent(context, MultiPreviewScreen.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });


    }


    private void receivedMessageView(ViewHolder holder,int position){


        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));


        holder.binding.sentMessageContainer.setVisibility(View.GONE);
        holder.binding.receivedMessageContainer.setVisibility(View.VISIBLE);



        if (isSharingEnabled){
            holder.binding.forward.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.forward.setVisibility(View.GONE);
        }


        if (!isAllowDiscussion){
            holder.binding.greenHighlightDivider.setVisibility(View.GONE);
        }
        else{
            holder.binding.greenHighlightDivider.setVisibility(View.VISIBLE);
        }


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

                int userId= Integer.parseInt(list.get(position).getUser().getUserID());

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

        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageContainer.setVisibility(View.GONE);


            boolean isMessageSelfQuoted = false;

            if (list.get(position).getQuote() != null) {
                if (list.get(position).getQuote().getMessage() != null) {

                    holder.binding.oldMessage.setVisibility(View.VISIBLE);
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));

                        isMessageSelfQuoted = true;
                        holder.binding.voiceMessageQuoteContainer.setCardBackgroundColor(context.getColor(R.color.channelQuoteMediaAttachedSentBackgroundColor));
                    }
                }
                else{
                    holder.binding.oldMessage.setVisibility(View.GONE);
                }
                holder.binding.oldMessage.setTypeface(holder.binding.oldMessage.getTypeface(), Typeface.ITALIC);

                holder.binding.oldMessage.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser.setText(username);

                holder.binding.oldMsgTime.setText(Utils.getHeaderDate(list.get(position).getQuote().getUpdatedAt()));

                TextViewUtil.groupExpandableMessage(context,holder.binding.newMessage,list.get(position).getMessage(), Constants.GROUP_QUOTE_MESSAGE_LIMIT_COUNT,isMessageSelfQuoted);

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

        holder.binding.userContainer.setOnClickListener(view -> {

            int userId= Integer.parseInt(list.get(position).getUser().getUserID());
            if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) != userId) {
                groupChatListener.viewMemberProfile(Integer.parseInt(userID),list.get(position).getGCMemberID(),Integer.parseInt(list.get(position).getGroupChatID()),list.get(position).getGroupChannelID());
            }
            else{
                groupChatListener.viewSelfProfile();
            }
        });


        holder.binding.playPauseRecordedAudio.setOnClickListener(view -> {
            if(AudioPlayUtil.checkFileExistence(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID())){
                String path = AudioPlayUtil.getSavedAudioFilePath(list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID());
                long duration = AudioPlayUtil.getAudioDuration(path);
                groupChatListener.playAudio(path,holder.binding.waveForm,duration);
            }
        });

        holder.binding.downloadAudio.setOnClickListener(view -> {
            String filePath = post_base_url+list.get(position).getMedia().get(0).getStoragePath()+list.get(position).getMedia().get(0).getFileName();
            groupChatListener.downloadAudio(filePath,list.get(position).getGroupChannelID().toString(),list.get(position).getGroupChatID(),holder.binding.waveForm,holder.binding.downloaderLoader,holder.binding.playPauseRecordedAudio);
        });

        if (list.get(position).getUpdatedAt() != null) {
            time = Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getChatBoxTimeStamp(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }

        if (list.get(position).getCommentData() == null || list.get(position).getCommentData().size() == 0) {

            if (isAllowDiscussion) {
                holder.binding.comment.setVisibility(View.VISIBLE);
                holder.binding.commentCount.setText("Comment");
            }
            else{
                holder.binding.comment.setVisibility(View.GONE);
            }
        }
        else {
            String commentCounts = String.valueOf(list.get(position).getCommentData().size());
            holder.binding.commentCount.setText("("+commentCounts+")");
        }

        if (list.get(position).getLike() != null && list.get(position).getLike().size() != 0) {
            if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1) {
                holder.binding.likeIcon.setColorFilter(context.getColor(R.color.theme_green));
            }
            else {
                holder.binding.likeIcon.setColorFilter(context.getColor(R.color.chatIconColor));
            }
        }

        holder.binding.quoteMsgContainer.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                groupChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });

        holder.binding.comment.setOnClickListener(view -> {
            groupChatListener.initiateCommentScreen(data, profileBaseUrl, post_base_url, userID,isAllowDiscussion);
        });



        holder.binding.like.setOnLongClickListener(view -> {

            if (isReactionEnabled) {
                if (list.get(position).getLike() != null) {
                    if (list.get(position).getLike().size() != 0) {
                        if (list.get(position).getLike().get(0).getIsLike() == 0) {
                            groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    1,
                                    position,
                                    holder.binding.like,
                                    holder.binding.likeIcon,
                                    holder.binding.reactionIcon);
                        } else {
                            groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    0,
                                    position,
                                    holder.binding.like,
                                    holder.binding.likeIcon,
                                    holder.binding.reactionIcon);
                        }
                    } else {
                        groupChatListener.likeAsEmote(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1,
                                position,
                                holder.binding.like,
                                holder.binding.likeIcon,
                                holder.binding.reactionIcon);
                    }
                } else {
                    groupChatListener.likeAsEmote(Integer.parseInt(userID),
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
            return false;
        });





        holder.binding.like.setOnClickListener(view -> {

            if (list.get(position).getLike() != null)
            {
                if (list.get(position).getLike().size() != 0)
                {
                    if (list.get(position).getLike().get(0).getIsLike() == 0)
                    {
                        groupChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1);
                    }
                    else{
                        groupChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                0);
                    }
                }
                else {
                    groupChatListener.likePost(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1);
                }
            }
            else {
                groupChatListener.likePost(Integer.parseInt(userID),
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

        holder.binding.forward.setOnClickListener(view -> {
            Dialog forward_dialog = new Dialog(context);

            forward_dialog.setContentView(R.layout.dialog_forward_message);
            forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
            close.setOnClickListener(view1 -> forward_dialog.dismiss());


            RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);

            int channelID = list.get(position).getGroupChannelID();
            int chatID = Integer.parseInt(list.get(position).getGroupChatID());

            ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(context,channelID,chatID);
            personList.setHasFixedSize(true);
            personList.setLayoutManager(new LinearLayoutManager(context));
            personList.setAdapter(forwardPersonListAdapter);

            forwardPersonListAdapter.setOnSaveMessageClickListener(chatID1 -> {
                groupChatListener.saveMessage(chatID1);
                forward_dialog.dismiss();
            });

            forward_dialog.show();
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
                groupChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),true);


            } else {
                selectedPostCount--;
                list.get(position).setPostSelected(false);
                notifyItemChanged(position);

                if (selectedPostCount <= 0){
                    groupChatListener.forwardMultiplePost(-1,Integer.parseInt(list.get(position).getGroupChatID()),false);
                    groupChatListener.toggleMultipleMessageSelection(false);

                }
                else{
                    groupChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),false);

                }

            }
        });

        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            String fileType = Utils.checkFileType(list.get(position).getMedia().get(0).getMimeType());


            if (fileType.equalsIgnoreCase("audio")){

                holder.binding.audioTimeContainer.setVisibility(View.VISIBLE);

                holder.binding.postImageContainer.setVisibility(View.GONE);
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
                holder.binding.postImageContainer.setVisibility(View.VISIBLE);

                loadReceivePostMedia(holder, position, list.get(position).getMedia().size());


            }
            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
        }else{
            holder.binding.audioContainer.setVisibility(View.GONE);
            holder.binding.postImageContainer.setVisibility(View.GONE);
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


        if (list.get(position).getMessage() != null && !list.get(position).getMessage().trim().isEmpty()) {

            holder.binding.message.setVisibility(View.VISIBLE);

            TextViewUtil.groupExpandableMessage(context,holder.binding.message,list.get(position).getMessage(),Constants.GROUP_MESSAGE_LIMIT_COUNT,false);
        } else {
            holder.binding.message.setVisibility(View.GONE);
        }


        holder.binding.postImageContainer.setOnClickListener(view -> {

            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getMedia());

            Intent intent = new Intent(context, MultiPreviewScreen.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });
    }



    private void loadSentPostMedia(ViewHolder holder,int index,int media_count)
    {


        String fileType= "";

        String post_path = "";


        switch (media_count) {
            case 1:

                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                Log.d("Entered_POst",post_base_url);
                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer1.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer1.setVisibility(View.VISIBLE);



                if (fileType.equalsIgnoreCase("image"))
                {

                    Log.d("Post Main Url", post_path);

                    holder.binding.playVideo01.setVisibility(View.GONE);
                    holder.binding.docContainer1.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif1.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif1.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.postImage1);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.postImage1,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif1.setVisibility(View.GONE);
                    holder.binding.docContainer1.setVisibility(View.VISIBLE);
                    holder.binding.playVideo01.setVisibility(View.GONE);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName1.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                    loadDocumentFile(post_path,holder.binding.postImage);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.docContainer1.setVisibility(View.GONE);
                    holder.binding.playGif1.setVisibility(View.GONE);
                    holder.binding.playVideo01.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.postImage);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }
                break;

            case 2:
                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer1.setVisibility(View.GONE);
                holder.binding.dualPostImageContainer1.setVisibility(View.VISIBLE);



                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo11.setVisibility(View.GONE);
                    holder.binding.docContainer2.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif2.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif2.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.dualPost11);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.dualPost11,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document"))
                {

                    holder.binding.docContainer2.setVisibility(View.VISIBLE);
                    holder.binding.playGif2.setVisibility(View.GONE);
                    holder.binding.playVideo11.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost11);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName2.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                }
                else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif2.setVisibility(View.GONE);
                    holder.binding.playVideo11.setVisibility(View.VISIBLE);
                    holder.binding.docContainer2.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.dualPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo21.setVisibility(View.GONE);
                    holder.binding.docContainer3.setVisibility(View.GONE);


                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif3.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif3.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.dualPost21);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.dualPost21,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.docContainer3.setVisibility(View.VISIBLE);
                    holder.binding.playGif3.setVisibility(View.GONE);
                    holder.binding.playVideo21.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost21);

                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName3.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif3.setVisibility(View.GONE);
                    holder.binding.docContainer3.setVisibility(View.GONE);
                    holder.binding.playVideo21.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.dualPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }

                break;
            case 3:
                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                holder.binding.additionalImageCount1.setVisibility(View.GONE);

                holder.binding.multiPostImageContainer1.setVisibility(View.VISIBLE);




                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    holder.binding.docContainer4.setVisibility(View.GONE);



                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif4.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif4.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost11);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost11,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif4.setVisibility(View.GONE);
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    holder.binding.docContainer4.setVisibility(View.VISIBLE);
                    loadDocumentFile(post_path,holder.binding.multiPost11);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName4.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif4.setVisibility(View.GONE);
                    holder.binding.docContainer4.setVisibility(View.GONE);
                    holder.binding.playVideo31.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    holder.binding.docContainer5.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif5.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif5.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost21);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost21,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif5.setVisibility(View.GONE);
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    holder.binding.docContainer5.setVisibility(View.VISIBLE);
                    loadDocumentFile(post_path,holder.binding.multiPost21);

                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName5.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif5.setVisibility(View.GONE);
                    holder.binding.playVideo41.setVisibility(View.VISIBLE);
                    holder.binding.docContainer5.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    holder.binding.docContainer6.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(2).getMimeType())){
                        holder.binding.playGif6.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif6.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost31);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost31,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif6.setVisibility(View.GONE);
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    holder.binding.docContainer6.setVisibility(View.VISIBLE);
                    loadDocumentFile(post_path,holder.binding.multiPost31);

                    if (list.get(index).getMedia().get(2).getFileName() != null){
                        holder.binding.docName6.setText(list.get(index).getMedia().get(2).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif6.setVisibility(View.GONE);
                    holder.binding.playVideo51.setVisibility(View.VISIBLE);
                    holder.binding.docContainer6.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost3);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }

                break;

            default:
                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                holder.binding.additionalImageCount1.setVisibility(View.VISIBLE);

                holder.binding.additionalImageCount1.setText("+ " + (media_count - 3));

                holder.binding.multiPostImageContainer1.setVisibility(View.VISIBLE);


                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    holder.binding.docContainer4.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif4.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif4.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost11);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost11,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif4.setVisibility(View.GONE);
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    holder.binding.docContainer4.setVisibility(View.VISIBLE);
                    loadDocumentFile(post_path,holder.binding.multiPost11);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName4.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif4.setVisibility(View.GONE);
                    holder.binding.playVideo31.setVisibility(View.VISIBLE);
                    holder.binding.docContainer4.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    holder.binding.docContainer5.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif5.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif5.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost21);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost21,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif5.setVisibility(View.GONE);
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    holder.binding.docContainer5.setVisibility(View.VISIBLE);
                    //loadDocumentFile(post_path,holder.binding.multiPost2);

                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName5.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif5.setVisibility(View.GONE);
                    holder.binding.playVideo41.setVisibility(View.VISIBLE);
                    holder.binding.docContainer5.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    holder.binding.docContainer6.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(2).getMimeType())){
                        holder.binding.playGif6.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif6.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost31);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost31,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif6.setVisibility(View.GONE);
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    holder.binding.docContainer6.setVisibility(View.VISIBLE);

                    if (list.get(index).getMedia().get(2).getFileName() != null){
                        holder.binding.docName6.setText(list.get(index).getMedia().get(2).getFileName());
                    }

                    //loadDocumentFile(post_path,holder.binding.multiPost3);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif6.setVisibility(View.GONE);
                    holder.binding.playVideo51.setVisibility(View.VISIBLE);
                    holder.binding.docContainer6.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost3);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }
        }
    }


    private void loadReceivePostMedia(ViewHolder holder,int index,int media_count)
    {


        String fileType= "";

        String post_path = "";


        switch (media_count) {
            case 1:

                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                Log.d("Entered_POst",post_base_url);
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.VISIBLE);



                if (fileType.equalsIgnoreCase("image"))
                {

                    Log.d("Post Main Url", post_path);

                    holder.binding.playVideo.setVisibility(View.GONE);
                    holder.binding.docContainer01.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif01.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif01.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.postImage);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.postImage,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playVideo.setVisibility(View.GONE);
                    holder.binding.playGif01.setVisibility(View.GONE);
                    holder.binding.docContainer01.setVisibility(View.VISIBLE);

                    loadDocumentFile(post_path,holder.binding.postImage);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName01.setText(list.get(index).getMedia().get(0).getFileName());
                    }


                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playVideo.setVisibility(View.VISIBLE);
                    holder.binding.playGif01.setVisibility(View.GONE);
                    holder.binding.docContainer01.setVisibility(View.GONE);

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



                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    holder.binding.docContainer02.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif02.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif02.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.dualPost1);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.dualPost1,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif02.setVisibility(View.GONE);
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    holder.binding.docContainer02.setVisibility(View.VISIBLE);

                    loadDocumentFile(post_path,holder.binding.dualPost1);

                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName02.setText(list.get(index).getMedia().get(0).getFileName());
                    }


                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif02.setVisibility(View.GONE);
                    holder.binding.playVideo1.setVisibility(View.VISIBLE);
                    holder.binding.docContainer02.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.dualPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo2.setVisibility(View.GONE);
                    holder.binding.docContainer03.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif03.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif03.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.dualPost2);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.dualPost2,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif03.setVisibility(View.GONE);
                    holder.binding.playVideo2.setVisibility(View.GONE);
                    holder.binding.docContainer03.setVisibility(View.VISIBLE);

                    loadDocumentFile(post_path,holder.binding.dualPost2);


                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName03.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.docContainer03.setVisibility(View.GONE);
                    holder.binding.playGif03.setVisibility(View.GONE);
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




                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    holder.binding.docContainer04.setVisibility(View.GONE);


                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif04.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif04.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost1);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost1,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif04.setVisibility(View.GONE);
                    holder.binding.playVideo3.setVisibility(View.GONE);

                    holder.binding.docContainer04.setVisibility(View.VISIBLE);

                    loadDocumentFile(post_path,holder.binding.multiPost1);


                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName04.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif04.setVisibility(View.GONE);
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
                    holder.binding.docContainer04.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    holder.binding.docContainer05.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif05.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif05.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost2);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost2,post_path);
                    }

                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif05.setVisibility(View.GONE);
                    holder.binding.playVideo4.setVisibility(View.GONE);

                    holder.binding.docContainer05.setVisibility(View.VISIBLE);
                    loadDocumentFile(post_path,holder.binding.multiPost2);


                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName05.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif05.setVisibility(View.GONE);
                    holder.binding.playVideo4.setVisibility(View.VISIBLE);

                    holder.binding.docContainer05.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    holder.binding.docContainer06.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(2).getMimeType())){
                        holder.binding.playGif06.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif06.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost3);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost3,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif06.setVisibility(View.GONE);
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    holder.binding.docContainer06.setVisibility(View.VISIBLE);

                    if (list.get(index).getMedia().get(2).getFileName() != null){
                        holder.binding.docName06.setText(list.get(index).getMedia().get(2).getFileName());
                    }


                    loadDocumentFile(post_path,holder.binding.multiPost3);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.docContainer06.setVisibility(View.GONE);
                    holder.binding.playGif06.setVisibility(View.GONE);
                    holder.binding.playVideo5.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost3);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }

                break;

            default:
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.additionalImageCount.setVisibility(View.VISIBLE);

                holder.binding.additionalImageCount.setText("+ " + (media_count - 3));

                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);


                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    holder.binding.docContainer04.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(0).getMimeType())){
                        holder.binding.playGif04.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif04.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost1);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost1,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif04.setVisibility(View.GONE);
                    holder.binding.playVideo3.setVisibility(View.GONE);

                    holder.binding.docContainer04.setVisibility(View.VISIBLE);


                    if (list.get(index).getMedia().get(0).getFileName() != null){
                        holder.binding.docName04.setText(list.get(index).getMedia().get(0).getFileName());
                    }

                    loadDocumentFile(post_path,holder.binding.multiPost1);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif04.setVisibility(View.GONE);
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
                    holder.binding.docContainer04.setVisibility(View.GONE);
                    //loadVideoFile(post_path,holder.binding.multiPost1);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(1).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    holder.binding.docContainer05.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(1).getMimeType())){
                        holder.binding.playGif05.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif05.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost2);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost2,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif05.setVisibility(View.GONE);
                    holder.binding.playVideo4.setVisibility(View.GONE);

                    holder.binding.docContainer05.setVisibility(View.VISIBLE);

                    if (list.get(index).getMedia().get(1).getFileName() != null){
                        holder.binding.docName05.setText(list.get(index).getMedia().get(1).getFileName());
                    }

                    //loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif05.setVisibility(View.GONE);
                    holder.binding.docContainer05.setVisibility(View.GONE);
                    holder.binding.playVideo4.setVisibility(View.VISIBLE);
                    //loadVideoFile(post_path,holder.binding.multiPost2);
                }
                else{
                    Log.d("File_Type_Error",fileType);
                }


                fileType = Utils.checkFileType(list.get(index).getMedia().get(2).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo5.setVisibility(View.GONE);
                    holder.binding.docContainer06.setVisibility(View.GONE);

                    if (Utils.isFileTypeGif(list.get(index).getMedia().get(2).getMimeType())){
                        holder.binding.playGif06.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.playGif06.setVisibility(View.GONE);
                    }

                    if (isGifAutoPlay){
                        loadImageFile(post_path,holder.binding.multiPost3);
                    }
                    else{
                        GlideUtils.loadImage(context,holder.binding.multiPost3,post_path);
                    }
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playGif06.setVisibility(View.GONE);
                    holder.binding.playVideo5.setVisibility(View.GONE);

                    holder.binding.docContainer06.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost3);

                    if (list.get(index).getMedia().get(2).getFileName() != null){
                        holder.binding.docName06.setText(list.get(index).getMedia().get(2).getFileName());
                    }


                } else if (fileType.equalsIgnoreCase("video")) {

                    holder.binding.playGif06.setVisibility(View.GONE);
                    holder.binding.playVideo5.setVisibility(View.VISIBLE);

                    holder.binding.docContainer06.setVisibility(View.GONE);
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

        Glide.with(context).load(imageFilePath).
                fitCenter().apply(requestOptions).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerGroupChatBinding binding;

        ViewHolder(@NonNull RecyclerGroupChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
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


    private void loadBottomSheet(ViewHolder holder, int position, boolean isSentMessage)
    {
        BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
        chat_options_dialog.setContentView(R.layout.bottomsheet_group_chat_actions);

        TextView select = chat_options_dialog.findViewById(R.id.select);

        TextView pin = chat_options_dialog.findViewById(R.id.pin);
        TextView quote = chat_options_dialog.findViewById(R.id.quote);
        TextView copy = chat_options_dialog.findViewById(R.id.copy);
        TextView remove = chat_options_dialog.findViewById(R.id.remove);
        TextView cancel = chat_options_dialog.findViewById(R.id.cancel);


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
            groupChatListener.pinMessage(list.get(position).getGCMemberID(),list.get(position).getGroupChannelID(),list.get(position).getUserID(),groupChatId);
        });


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

            groupChatListener.sendQuotedMessage(holder.binding.getRoot(), list.get(position).getGroupChatID(), list.get(position).getMessage(), name, time,mediaCount,previewUrl);
            chat_options_dialog.dismiss();
        });


        assert select != null;
        select.setOnClickListener(v -> {

            list.get(position).setPostSelected(true);
            groupChatListener.toggleMultipleMessageSelection(true);

            selectedPostCount = 1;
            groupChatListener.forwardMultiplePost(selectedPostCount,Integer.parseInt(list.get(position).getGroupChatID()),true);

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
                groupChatListener.deletePost(Integer.parseInt(userID),list.get(position).getGCMemberID(),Integer.parseInt(list.get(position).getGroupChatID()),list.get(position).getGroupChannelID());
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

                    if (infoDbEntity.getGcSetting().getAllowDiscussion() != null){
                        if (infoDbEntity.getGcSetting().getAllowDiscussion() == 1){

                            isAllowDiscussion = true;
                        }
                        else{
                            isAllowDiscussion = false;
                        }
                    }

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
            isAllowDiscussion = true;
            isSharingEnabled = true;
        }


        /**
         * Check user profile permissions
         */


    }


    public void updateMultipleMessageSelection(ArrayList<ChannelRowListDataModel> list){
        this.list = list;
        notifyDataSetChanged();
    }


    public void updateList(ArrayList<ChannelRowListDataModel> list,String postBaseUrl,String profileBaseUrl)
    {
        this.list = list;
        this.post_base_url = postBaseUrl;
        this.profileBaseUrl = profileBaseUrl;
        notifyDataSetChanged();
    }


    public void updateLikeResponse(int position, ArrayList<ChannelRowListDataModel> list){
        this.list = list;
        notifyItemChanged(position);
    }


    public void updateChat(ArrayList<ChannelRowListDataModel> list){
        this.list = list;
        notifyItemInserted(0);
    }


    public void updatePostBaseUrl(String post_base_url){
        this.post_base_url = post_base_url;
    }

    public void updateGcPermission(InfoDbEntity infoDbEntity){
        this.infoDbEntity = infoDbEntity;
        setUserPermissions();
        notifyDataSetChanged();
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