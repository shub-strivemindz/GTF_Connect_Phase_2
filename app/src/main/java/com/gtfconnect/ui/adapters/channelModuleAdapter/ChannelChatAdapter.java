package com.gtfconnect.ui.adapters.channelModuleAdapter;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerChannelChatItemBinding;
import com.gtfconnect.interfaces.ChannelChatListener;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.ui.screenUI.groupModule.GroupCommentScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
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

    String userName = "";
    String message = "";
    String time = "";

    String post_base_url= "";

    static int likeCounter;

//    private int commentCount;

    // ArrayList<Boolean> isMessageLiked = new ArrayList<>();

    public ChannelChatAdapter(Context context, List<ChannelRowListDataModel> list, String userID, String post_base_url, ChannelChatListener channelChatListener) {
        this.list = list;
        this.context = context;
        this.userID = userID;
        this.channelChatListener = channelChatListener;
        this.post_base_url = post_base_url;
        ///this.commentCount = commentCount;
    }

    public void updateList(List<ChannelRowListDataModel> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(RecyclerChannelChatItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        final int position = index;

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
            holder.binding.quoteContainer.setVisibility(View.VISIBLE);
            holder.binding.headerDivider.setVisibility(View.GONE);
            holder.binding.messageContainer.setVisibility(View.GONE);

            if (list.get(position).getQuote() != null) {
                if (list.get(position).getQuote().getMessage() != null) {
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setCardBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));
                    }
                }
                holder.binding.oldMessage.setTypeface(holder.binding.oldMessage.getTypeface(), Typeface.ITALIC);
                holder.binding.oldMessage.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser.setText(username);

                holder.binding.oldMsgTime.setText(Utils.getDisplayableTime(list.get(position).getQuote().getUpdatedAt()));


                holder.binding.newMessage.setText(list.get(position).getMessage());

            }
        } else {
            holder.binding.headerDivider.setVisibility(View.VISIBLE);
            holder.binding.messageContainer.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer.setVisibility(View.GONE);
        }

        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty()) {

            ChannelMediaAdapter mediaAdapter = new ChannelMediaAdapter(context,holder.binding.mediaRecycler, list.get(position).getMedia(), post_base_url,String.valueOf(userID));
            holder.binding.mediaRecycler.setHasFixedSize(true);
            holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.binding.mediaRecycler.setAdapter(mediaAdapter);

            //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
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
            holder.binding.message.setText(list.get(position).getMessage());
        } else {
            message = "No message found";
            holder.binding.message.setText("No message found");
        }

        if (list.get(position).getCreatedAt() != null) {
            time = Utils.getDisplayableTime(list.get(position).getUpdatedAt());
            holder.binding.time.setText(Utils.getDisplayableTime(list.get(position).getUpdatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }



        if (list.get(position).getCommentData() == null) {
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
            }

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
                channelChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });



        // Reply into the Chat
        holder.binding.comment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("replyOnComment", true);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);
            context.startActivity(intent);
                /*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
        });

        // Bottom-sheet for chat options --
        holder.binding.bottomsheetChatOption.setOnClickListener(view -> loadBottomsheet(holder,position));

        // Bottom-sheet for image options --

        /*holder.binding.postImageContainer.setOnLongClickListener(view -> {
            BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
            chat_options_dialog.setContentView(R.layout.bottomsheet_post_action_options2);
            chat_options_dialog.show();
            return false;
        });


        holder.binding.postImageContainer.setOnClickListener(view -> {

            Gson gson1  = new Gson();
            String mediaData =  gson1.toJson(list.get(position).getMedia());

            Intent intent = new Intent(context, MultiPreviewImage.class);
            intent.putExtra("mediaList",mediaData);
            intent.putExtra("base_url",post_base_url);

            String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
            intent.putExtra("title",title);

            context.startActivity(intent);
        });*/

        holder.binding.like.setOnLongClickListener(view -> {
            channelChatListener.likeAsEmote(position,holder.binding.likeIcon);
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
        });
    }


    private void loadBottomsheet(ViewHolder holder,int position)
    {
        BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
        chat_options_dialog.setContentView(R.layout.bottomsheet_group_chat_actions);

        TextView pin = chat_options_dialog.findViewById(R.id.pin);
        TextView quote = chat_options_dialog.findViewById(R.id.quote);
        TextView copy = chat_options_dialog.findViewById(R.id.copy);
        TextView remove = chat_options_dialog.findViewById(R.id.remove);
        TextView cancel = chat_options_dialog.findViewById(R.id.cancel);

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
            channelChatListener.pinMessage(list.get(position).getGCMemberID(),list.get(position).getGroupChannelID(),list.get(position).getUserID(),groupChatId);
        });

        quote.setOnClickListener(view -> {
            String name = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
            channelChatListener.sendQuotedMessage(holder.binding.getRoot(), list.get(position).getGroupChatID(), list.get(position).getMessage(), name, time);
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
                channelChatListener.deletePost(Integer.parseInt(userID),list.get(position).getGCMemberID(),Integer.parseInt(list.get(position).getGroupChatID()),list.get(position).getGroupChannelID());
                delete_post_dialog.dismiss();
            });

            cancel_post.setOnClickListener(view1 -> delete_post_dialog.dismiss());

        });

        cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

        chat_options_dialog.show();
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
}