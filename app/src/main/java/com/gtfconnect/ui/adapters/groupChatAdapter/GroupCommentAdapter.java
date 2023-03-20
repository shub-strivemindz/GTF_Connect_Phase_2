package com.gtfconnect.ui.adapters.groupChatAdapter;

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

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentMessageCommentItemBinding;
import com.gtfconnect.interfaces.GroupCommentListener;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupCommentAdapter extends RecyclerView.Adapter<GroupCommentAdapter.ViewHolder> {

    private List<GroupChatResponseModel.CommentResponseModel> commentList;
    private Context context;

    private GroupCommentListener groupCommentListener;

    String userID;

    public GroupCommentAdapter(Context context, List<GroupChatResponseModel.CommentResponseModel> commentList, String userID, GroupCommentListener groupCommentListener){

        this.context = context;
        this.commentList = commentList;
        this.userID = userID;
        this.groupCommentListener = groupCommentListener;

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
        holder.binding.userName.setText(userName);

        String time = Utils.getDisplayableTime(commentList.get(position).getUpdatedAt());
        holder.binding.commentTime.setText(time);

        holder.binding.comment.setText(commentList.get(position).getComment());

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


    public void updateList(List<GroupChatResponseModel.CommentResponseModel> commentList)
    {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (commentList != null)
            return commentList.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentMessageCommentItemBinding binding;

        ViewHolder(@NonNull FragmentMessageCommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

