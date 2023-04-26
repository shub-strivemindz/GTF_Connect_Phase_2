package com.gtfconnect.ui.adapters.groupChatAdapter;

import android.animation.ValueAnimator;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChannelChatItemBinding;
import com.gtfconnect.databinding.RecyclerGroupChatBinding;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.ui.adapters.GroupChannel_MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelChatAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelMediaAdapter;
import com.gtfconnect.ui.screenUI.groupModule.GroupCommentScreen;
import com.gtfconnect.ui.screenUI.groupModule.MultiPreviewImage;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private ArrayList<GroupChatResponseModel.Row> list;
    private Context context;

    private GroupChatResponseModel.Row item;

    private JSONObject jsonRawObject;

    private String userID;

    private boolean isDayTagShown = false;

    private GroupChatListener groupChatListener;

    private ViewHolder viewHolder;
    private boolean isMessageClicked = false;

    private int selectedPostCount;

    String userName = "";
    String message = "";
    String time = "";

    String post_base_url= "";

    private String messageTime = "";

    private int messageUserID;

    private boolean isDummyUser = true;



    public GroupChatAdapter(Context context, ArrayList<GroupChatResponseModel.Row> list, String userID, String post_base_url, GroupChatListener groupChatListener) {
        this.list = list;
        this.context = context;
        this.userID = userID;
        this.groupChatListener = groupChatListener;
        this.post_base_url = post_base_url;
        ///this.commentCount = commentCount;


        selectedPostCount = 0;
    }

    public void updateList(ArrayList<GroupChatResponseModel.Row> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ViewHolder(RecyclerGroupChatBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        final int position = index;
        viewHolder = holder;

        if (list != null && list.get(position) != null && !list.isEmpty()) {


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
                                            holder.binding.dateChipContainer.setVisibility(View.GONE);
                                        } else {
                                            holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                                            holder.binding.currentDate.setText(Utils.getChipDate(list.get(position).getUpdatedAt()));
                                            messageTime = "";
                                        }
                                    }
                                }
                            }
                        } else if (position+1 == list.size()) {
                            holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                            holder.binding.currentDate.setText(Utils.getChipDate(list.get(position).getUpdatedAt()));
                            messageTime = "";
                        }

                    }
                }
            }




            if (userID.equalsIgnoreCase(String.valueOf(list.get(position).getUserID()))){
                sentMessageView(holder,position);
            }
            else{
                receivedMessageView(holder,position);
            }


            if (list.get(position).isShowPostSelection()){
                holder.binding.selectPost1.setVisibility(View.VISIBLE);
                holder.binding.selectPost.setVisibility(View.VISIBLE);
            }
            else{
                holder.binding.selectPost1.setVisibility(View.GONE);
                holder.binding.selectPost.setVisibility(View.GONE);
            }
        }


    }



    private void sentMessageView(ViewHolder holder,int position){

        holder.binding.sentMessageContainer.setVisibility(View.VISIBLE);
        holder.binding.receivedMessageContainer.setVisibility(View.GONE);

        if (isDummyUser){
            holder.binding.dummyUserName.setVisibility(View.VISIBLE);
            holder.binding.dummyUserTimeDivider.setVisibility(View.VISIBLE);
            isDummyUser =false ;
        }
        else{
            holder.binding.dummyUserName.setVisibility(View.GONE);
            holder.binding.dummyUserTimeDivider.setVisibility(View.GONE);
        }



        if (list.get(position).isPostSelected()){
            holder.binding.selectPost1.setChecked(true);
        }
        else {
            holder.binding.selectPost1.setChecked(false);
        }




        if (list.get(position).getUser() != null) {
            if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.userName.setText("Bot");
            } else {

                Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                int userId= Integer.parseInt(list.get(position).getUser().getUserID());
                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                    holder.binding.userName.setText("You");
                }
                else {
                    userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                    holder.binding.userName.setText(userName);
                }
            }
        }

        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer1.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageDivider1.setVisibility(View.GONE);
            holder.binding.messageContainer1.setVisibility(View.GONE);
            holder.binding.quoteDivider1.setVisibility(View.VISIBLE);

            if (list.get(position).getQuote() != null) {
                /*if (list.get(position).getQuote().getMessage() != null) {
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setCardBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));
                    }
                }*/
                holder.binding.oldMessage1.setTypeface(holder.binding.oldMessage.getTypeface(), Typeface.ITALIC);
                holder.binding.oldMessage1.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser1.setText(username);

                holder.binding.oldMsgTime1.setText(Utils.getHeaderDate(list.get(position).getQuote().getUpdatedAt()));


                holder.binding.newMessage1.setText(list.get(position).getMessage());

            }
        } else {
            holder.binding.headerDivider.setVisibility(View.VISIBLE);
            holder.binding.messageContainer1.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer1.setVisibility(View.GONE);
            holder.binding.quoteDivider1.setVisibility(View.GONE);
            holder.binding.messageDivider1.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            holder.binding.postImageContainer1.setVisibility(View.VISIBLE);
                    /*holder.binding.mediaRecycler1.setVisibility(View.VISIBLE);

                    GroupChannel_MediaAdapter mediaAdapter = new GroupChannel_MediaAdapter(context,holder.binding.mediaRecycler, list.get(position).getMedia(), post_base_url,String.valueOf(userID));
                    holder.binding.mediaRecycler1.setHasFixedSize(true);
                    holder.binding.mediaRecycler1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    holder.binding.mediaRecycler1.setAdapter(mediaAdapter);*/

            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);

            loadSentPostMedia(holder, position, list.get(position).getMedia().size());
        }
        else{
            //holder.binding.mediaRecycler1.setVisibility(View.GONE);
            holder.binding.postImageContainer1.setVisibility(View.GONE);
        }

       /*     // Todo : Uncomment below code once get thumbnail for the video and remove below line -----------------
            loadPostMedia(holder, position, list.get(position).getMedia().size());
            *//*if (Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType()).equalsIgnoreCase("video")) {
                String post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                holder.binding.postImageContainer.setVisibility(View.GONE);
                loadVideoFile(post_path);
        }
            else {
                loadPostMedia(holder, position, list.get(position).getMedia().size());
            }*//*
        }
        else{
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }*/





        if (list.get(position).getMessage() != null) {
            message = list.get(position).getMessage();
            holder.binding.message1.setText(list.get(position).getMessage());
        } else {
            message = "No message found";
            holder.binding.message1.setText("No message found");
        }

        if (list.get(position).getCreatedAt() != null) {
            time = Utils.getHeaderDate(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getHeaderDate(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }



        holder.binding.selectPost1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){

                list.get(position).setPostSelected(true);

                selectedPostCount++;
                groupChatListener.forwardMultiplePost(selectedPostCount,true);
            }
            else{
                list.get(position).setPostSelected(false);

                selectedPostCount--;
                if (selectedPostCount <= 0){

                    holder.binding.selectPost1.setVisibility(View.GONE);
                    holder.binding.dummyUserTimeDivider.setVisibility(View.GONE);

                    groupChatListener.forwardMultiplePost(-1,false);

                    toggleSelectionCheckbox(false);
                }
                else{
                    groupChatListener.forwardMultiplePost(selectedPostCount,true);
                }
            }
        });


     /*   if (list.get(position).getCommentData() == null) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        } else if (list.get(position).getCommentData().size() == 0) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        }
        else {
            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText(String.valueOf(list.get(position).getCommentData().size()));
        }

        if (list.get(position).getCommentCount() == 0) {


        }
        else {
            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText(list.get(position).getCommentCount());
        }



            if (list.get(position).getLike() != null && list.get(position).getLike().size() != 0) {
                if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1) {
                    holder.binding.likeIcon.setColorFilter(context.getColor(R.color.theme_green));
                }
                else {
                    holder.binding.likeIcon.setColorFilter(context.getColor(R.color.chatIconColor));
                }
            }*/

         /*   holder.binding.message.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("MAX_Text_Line",String.valueOf(holder.binding.message.getLineCount()));
                    if (holder.binding.message.getLineCount() >= 3)
                    {
                        Utils.makeTextViewResizable(holder.binding.message,3,"Load More",true);
                    }
                }
            });*/



//            if (holder.binding.message.getLineCount() > 3)
//            {
//                holder.binding.expandMessage.setVisibility(View.VISIBLE);
//            }
//            else{
//                holder.binding.expandMessage.setVisibility(View.GONE);
//            }

        holder.binding.message1.setOnClickListener(view -> {
            if(isMessageClicked){
                //This will shrink textview to 2 lines if it is expanded.
                holder.binding.message1.setMaxLines(3);
                isMessageClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                holder.binding.message1.setMaxLines(Integer.MAX_VALUE);
                isMessageClicked = true;
            }
        });

      /*  holder.binding.viewComment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);

            Log.d("Sending user ID ---", userID);
            context.startActivity(intent);
        });*/



        holder.binding.quoteMsgContainer1.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                groupChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });



       /* // Reply into the Chat
        holder.binding.comment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("replyOnComment", true);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);
            context.startActivity(intent);
                *//*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*//*
        });
        */

        // Bottom-sheet for chat options --


        holder.binding.sentMessageContainer.setOnLongClickListener(view -> {
            Log.d("Not going ","why2");
            loadBottomSheet(holder,position,true);

            return false;
        });

        // Bottom-sheet for image options --

        /*holder.binding.postImageContainer.setOnLongClickListener(view -> {
            BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
            chat_options_dialog.setContentView(R.layout.bottomsheet_post_action_options2);
            chat_options_dialog.show();
            return false;
        });*/


        holder.binding.postImageContainer1.setOnClickListener(view -> {

            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getMedia());

            Intent intent = new Intent(context, MultiPreviewImage.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });

        /*holder.binding.like.setOnLongClickListener(view -> {
            groupChatListener.likeAsEmote(position,holder.binding.bottomsheetChatOption);
            return false;
        });*/

        /*holder.binding.like1.setOnClickListener(view -> {

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
        });*/


        holder.binding.like1.setOnLongClickListener(view -> {
            groupChatListener.likeAsEmote(position,holder.binding.likeIcon1);
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





        /*//holder.binding.playVideo.setOnClickListener(view -> playVideo);

        holder.binding.forward.setOnClickListener(view -> {
            Dialog forward_dialog = new Dialog(context);

            forward_dialog.setContentView(R.layout.dialog_forward_message);
            forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            forward_dialog.setCancelable(false);
            forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
            close.setOnClickListener(view1 -> forward_dialog.dismiss());


            RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);

            ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(context);
            personList.setHasFixedSize(true);
            personList.setLayoutManager(new LinearLayoutManager(context));
            personList.setAdapter(forwardPersonListAdapter);

            forward_dialog.show();
        });*/
    }



    private void receivedMessageView(ViewHolder holder,int position){
        holder.binding.sentMessageContainer.setVisibility(View.GONE);
        holder.binding.receivedMessageContainer.setVisibility(View.VISIBLE);


        holder.binding.receivedMessageContainer.setOnLongClickListener(view -> {
            Log.d("Not going ","why");
            loadBottomSheet(holder,position,false);
            return false;
        });



        if (list.get(position).isPostSelected()){
            holder.binding.selectPost.setChecked(true);
        }
        else {
            holder.binding.selectPost.setChecked(false);
        }


        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));

        if (list.get(position).getUser() != null) {
            if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.userName.setText("Bot");
            } else {

                Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                int userId= Integer.parseInt(list.get(position).getUser().getUserID());
                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                    holder.binding.userName.setText("You");
                }
                else {
                    userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                    holder.binding.userName.setText(userName);
                }
            }
        }



        holder.binding.selectPost.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                list.get(position).setPostSelected(true);
                selectedPostCount++;
                groupChatListener.forwardMultiplePost(selectedPostCount,true);
            }
            else{
                list.get(position).setPostSelected(false);

                selectedPostCount--;
                if (selectedPostCount <= 0){

                    holder.binding.selectPost.setVisibility(View.GONE);

                    groupChatListener.forwardMultiplePost(-1,false);

                    toggleSelectionCheckbox(false);
                }
                else{
                    groupChatListener.forwardMultiplePost(selectedPostCount,true);
                }
            }
        });



        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageContainer.setVisibility(View.GONE);
            holder.binding.quoteDivider.setVisibility(View.VISIBLE);

            if (list.get(position).getQuote() != null) {
                /*if (list.get(position).getQuote().getMessage() != null) {
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setCardBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));
                    }
                }*/
                holder.binding.oldMessage.setTypeface(holder.binding.oldMessage.getTypeface(), Typeface.ITALIC);
                holder.binding.oldMessage.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser.setText(username);

                holder.binding.oldMsgTime.setText(Utils.getHeaderDate(list.get(position).getQuote().getUpdatedAt()));


                holder.binding.newMessage.setText(list.get(position).getMessage());

            }
        } else {
            holder.binding.headerDivider.setVisibility(View.VISIBLE);
            holder.binding.messageContainer.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer.setVisibility(View.GONE);
            holder.binding.quoteDivider.setVisibility(View.GONE);
        }



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
                                                    /*holder.binding.highWhiteSpacer.setVisibility(View.GONE);
                                                    holder.binding.lowWhiteSpacer.setVisibility(View.VISIBLE);*/
                                        holder.binding.userContainer.setVisibility(View.GONE);
                                    } else {
                                                    /*holder.binding.highWhiteSpacer.setVisibility(View.VISIBLE);
                                                    holder.binding.lowWhiteSpacer.setVisibility(View.GONE);*/
                                        holder.binding.userContainer.setVisibility(View.VISIBLE);
                                        messageUserID = 0;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }






        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            //holder.binding.mediaRecycler.setVisibility(View.VISIBLE);
            holder.binding.postImageContainer.setVisibility(View.VISIBLE);
            holder.binding.greenHighlightDivider.setVisibility(View.VISIBLE);

                    /*GroupChannel_MediaAdapter mediaAdapter = new GroupChannel_MediaAdapter(context,holder.binding.mediaRecycler, list.get(position).getMedia(), post_base_url,String.valueOf(userID));
                    holder.binding.mediaRecycler.setHasFixedSize(true);
                    holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    holder.binding.mediaRecycler.setAdapter(mediaAdapter);*/


            loadReceivePostMedia(holder, position, list.get(position).getMedia().size());
            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
        }
        else{
            //holder.binding.mediaRecycler.setVisibility(View.GONE);
            holder.binding.greenHighlightDivider.setVisibility(View.GONE);
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }

       /*     // Todo : Uncomment below code once get thumbnail for the video and remove below line -----------------
            loadPostMedia(holder, position, list.get(position).getMedia().size());
            *//*if (Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType()).equalsIgnoreCase("video")) {
                String post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                holder.binding.postImageContainer.setVisibility(View.GONE);
                loadVideoFile(post_path);
        }
            else {
            loadPostMedia(holder, position, list.get(position).getMedia().size());
            }
        }
        else{
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }*/

        if (list.get(position).getMessage() != null) {
            message = list.get(position).getMessage();
            holder.binding.message.setText(list.get(position).getMessage());
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



     /*   if (list.get(position).getCommentData() == null) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        } else if (list.get(position).getCommentData().size() == 0) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        }
        else {
            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText(String.valueOf(list.get(position).getCommentData().size()));
        }

        if (list.get(position).getCommentCount() == 0) {


        }
        else {
            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText(list.get(position).getCommentCount());
        }



            if (list.get(position).getLike() != null && list.get(position).getLike().size() != 0) {
                if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1) {
                    holder.binding.likeIcon.setColorFilter(context.getColor(R.color.theme_green));
                }
                else {
                    holder.binding.likeIcon.setColorFilter(context.getColor(R.color.chatIconColor));
                }
            }*/

         /*   holder.binding.message.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("MAX_Text_Line",String.valueOf(holder.binding.message.getLineCount()));
                    if (holder.binding.message.getLineCount() >= 3)
                    {
                        Utils.makeTextViewResizable(holder.binding.message,3,"Load More",true);
                    }
                }
            });*/



//            if (holder.binding.message.getLineCount() > 3)
//            {
//                holder.binding.expandMessage.setVisibility(View.VISIBLE);
//            }
//            else{
//                holder.binding.expandMessage.setVisibility(View.GONE);
//            }

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

      /*  holder.binding.viewComment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);

            Log.d("Sending user ID ---", userID);
            context.startActivity(intent);
        });*/



        holder.binding.quoteMsgContainer.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                groupChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });



       /* // Reply into the Chat
        holder.binding.comment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("replyOnComment", true);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);
            context.startActivity(intent);
                *//*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*//*
        });

        // Bottom-sheet for chat options --
        holder.binding.bottomsheetChatOption.setOnClickListener(view -> loadBottomsheet(holder,position));*/

        // Bottom-sheet for image options --

        /*holder.binding.postImageContainer.setOnLongClickListener(view -> {
            BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
            chat_options_dialog.setContentView(R.layout.bottomsheet_post_action_options2);
            chat_options_dialog.show();
            return false;
        });*/


        holder.binding.postImageContainer.setOnClickListener(view -> {

            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getMedia());

            Intent intent = new Intent(context, MultiPreviewImage.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });



        holder.binding.like.setOnLongClickListener(view -> {
            groupChatListener.likeAsEmote(position,holder.binding.likeIcon);
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

/*
        //holder.binding.playVideo.setOnClickListener(view -> playVideo);

        holder.binding.forward.setOnClickListener(view -> {
            Dialog forward_dialog = new Dialog(context);

            forward_dialog.setContentView(R.layout.dialog_forward_message);
            forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            forward_dialog.setCancelable(false);
            forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
            close.setOnClickListener(view1 -> forward_dialog.dismiss());


            RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);

            ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(context);
            personList.setHasFixedSize(true);
            personList.setLayoutManager(new LinearLayoutManager(context));
            personList.setAdapter(forwardPersonListAdapter);

            forward_dialog.show();
        });*/
    }




    private void loadBottomSheet(ViewHolder holder,int position,boolean isSentMessage)
    {
        BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
        chat_options_dialog.setContentView(R.layout.bottomsheet_group_chat_actions);

        TextView pin = chat_options_dialog.findViewById(R.id.pin);
        TextView quote = chat_options_dialog.findViewById(R.id.quote);
        TextView copy = chat_options_dialog.findViewById(R.id.copy);
        TextView remove = chat_options_dialog.findViewById(R.id.remove);
        TextView cancel = chat_options_dialog.findViewById(R.id.cancel);

        TextView select = chat_options_dialog.findViewById(R.id.select);

        if(list.get(position).getUserID() == PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0))
        {
            remove.setVisibility(View.VISIBLE);
        }
        else{
        remove.setVisibility(View.GONE);
         }

        pin.setOnClickListener(view -> {
            chat_options_dialog.dismiss();


            Integer groupChatId = Integer.parseInt(list.get(position).getGroupChatID());
            groupChatListener.pinMessage(list.get(position).getGCMemberID(),list.get(position).getGroupChannelID(),list.get(position).getUserID(),groupChatId);
        });

        quote.setOnClickListener(view -> {
            String name = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
            groupChatListener.sendQuotedMessage(holder.binding.getRoot(), list.get(position).getGroupChatID(), list.get(position).getMessage(), name, time);
            chat_options_dialog.dismiss();
        });


        select.setOnClickListener(v -> {
            groupChatListener.forwardMultiplePost(selectedPostCount,true);

            if (isSentMessage) {
                holder.binding.selectPost1.setChecked(true);
            }
            else{
                holder.binding.selectPost.setChecked(true);
            }

            toggleSelectionCheckbox(true);
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

        cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

        chat_options_dialog.show();
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
                    loadImageFile(post_path,holder.binding.postImage);
                }
                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                    holder.binding.playVideo01.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.postImage);
                } else if (fileType.equalsIgnoreCase("video")) {

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
                    loadImageFile(post_path,holder.binding.dualPost11);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo11.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost11);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo11.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.dualPost21);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo21.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost21);
                } else if (fileType.equalsIgnoreCase("video")) {
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
                    loadImageFile(post_path,holder.binding.multiPost11);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost11);
                } else if (fileType.equalsIgnoreCase("video")) {
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
                    loadImageFile(post_path,holder.binding.multiPost21);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost21);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo41.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.multiPost31);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost31);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo51.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.multiPost11);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo31.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost11);
                } else if (fileType.equalsIgnoreCase("video")) {
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
                    loadImageFile(post_path,holder.binding.multiPost21);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo41.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo41.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.multiPost31);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo51.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost3);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo51.setVisibility(View.VISIBLE);
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



                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.dualPost1);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo1.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.dualPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo1.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.dualPost2);
                }
                else if (fileType.equalsIgnoreCase("document")) {
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




                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost1);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.multiPost2);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
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
                    loadImageFile(post_path,holder.binding.multiPost3);
                }
                else if (fileType.equalsIgnoreCase("document")) {
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


                fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());
                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();

                if (fileType.equalsIgnoreCase("image"))
                {
                    Log.d("Post Main Url", post_path);
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadImageFile(post_path,holder.binding.multiPost1);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo3.setVisibility(View.GONE);
                    loadDocumentFile(post_path,holder.binding.multiPost1);
                } else if (fileType.equalsIgnoreCase("video")) {
                    holder.binding.playVideo3.setVisibility(View.VISIBLE);
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
                    loadImageFile(post_path,holder.binding.multiPost2);
                }
                else if (fileType.equalsIgnoreCase("document")) {
                    holder.binding.playVideo4.setVisibility(View.GONE);
                    //loadDocumentFile(post_path,holder.binding.multiPost2);
                } else if (fileType.equalsIgnoreCase("video")) {
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
                    loadImageFile(post_path,holder.binding.multiPost3);
                }
                else if (fileType.equalsIgnoreCase("document")) {
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

        Glide.with(context).load(imageFilePath).
                fitCenter().apply(requestOptions).
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
        Log.d("position item",""+position);

        String date = "";
        if (list!=null) {
            if (list.size() > position) {
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



    private void toggleSelectionCheckbox(boolean showCheckbox){

        if (showCheckbox){
            for (int i=0;i< list.size();i++){
                list.get(i).setShowPostSelection(true);
            }
        }
        else{
            for (int i=0;i< list.size();i++){
                list.get(i).setShowPostSelection(false);
            }
        }

        groupChatListener.updateChatList(list);
        notifyDataSetChanged();
    }




    public View getSelectedView(int position){
        if (userID.equalsIgnoreCase(String.valueOf(list.get(position).getUserID()))){
            return viewHolder.binding.sentMessageContainer;
        }
        else {
            return viewHolder.binding.receivedMessageContainer;
        }
    }

}