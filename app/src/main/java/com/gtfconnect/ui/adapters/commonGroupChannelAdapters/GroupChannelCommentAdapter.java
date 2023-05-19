package com.gtfconnect.ui.adapters.commonGroupChannelAdapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentMessageCommentItemBinding;
import com.gtfconnect.interfaces.GroupCommentListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatCommentModel;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupChannelCommentAdapter extends RecyclerView.Adapter<GroupChannelCommentAdapter.ViewHolder> {

    private List<ChannelChatCommentModel> commentList;
    private Context context;

    private GroupCommentListener groupCommentListener;

    String userID;

    String profileBaseUrl ;

    public GroupChannelCommentAdapter(Context context, List<ChannelChatCommentModel> commentList, String userID, GroupCommentListener groupCommentListener,String profileBaseUrl){

        this.context = context;
        this.commentList = commentList;
        this.userID = userID;
        this.groupCommentListener = groupCommentListener;

        this.profileBaseUrl = profileBaseUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentMessageCommentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {


        final int position = index;

        String userName = "";


        Log.d("comment user id ",commentList.get(position).getUserID().toString());
        String commentUserId = String.valueOf(commentList.get(position).getUserID());

        if (commentUserId.equalsIgnoreCase(userID)){
            userName = "You";
            holder.binding.editContainer.setVisibility(View.VISIBLE);
        }
        else {
            holder.binding.editContainer.setVisibility(View.GONE);
            userName = commentList.get(position).getUser().getFirstname() + " " + commentList.get(position).getUser().getLastname();
        }
        holder.binding.commentUser.setText(userName);

        String time = Utils.getHeaderDate(commentList.get(position).getUpdatedAt());
        holder.binding.commentDate.setText(time);

        holder.binding.commentText.setText(commentList.get(position).getComment());



        if (commentList.get(position).getUser().getProfileImage() != null){
            String image_path = profileBaseUrl + commentList.get(position).getUser().getProfileImage();
            GlideUtils.loadImage(context,holder.binding.commentProfilePic,image_path);
        }

        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupCommentListener.oldMessage(commentList.get(position).getComment(),commentList.get(position).getCommentID(),position);
            }
        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog delete_comment_dialog = new Dialog(context);

                delete_comment_dialog.setContentView(R.layout.dialog_delete_comment);
                delete_comment_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                delete_comment_dialog.setCancelable(false);
                delete_comment_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView delete = (TextView) delete_comment_dialog.findViewById(R.id.delete);
                TextView cancel = (TextView) delete_comment_dialog.findViewById(R.id.cancel);

                delete.setOnClickListener(view1 -> {
                    groupCommentListener.notifyDeleteComment(commentList.get(position).getCommentID(),commentList.get(position).getGroupChatID(),commentList.get(position).getGroupChannelID(),Integer.parseInt(userID));
                    delete_comment_dialog.dismiss();
                });

                cancel.setOnClickListener(view1 -> {
                    delete_comment_dialog.dismiss();
                });

                delete_comment_dialog.show();
            }
        });
    }


    public void updateList(List<ChannelChatCommentModel> commentList)
    {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d("commentSize",""+commentList.size());
        return commentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentMessageCommentItemBinding binding;

        ViewHolder(@NonNull FragmentMessageCommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

