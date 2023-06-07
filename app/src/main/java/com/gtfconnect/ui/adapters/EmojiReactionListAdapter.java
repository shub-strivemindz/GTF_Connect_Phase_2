package com.gtfconnect.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.RecyclerReactionListItemBinding;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;

public class EmojiReactionListAdapter extends RecyclerView.Adapter<EmojiReactionListAdapter.ViewHolder> {

    private Context context;

    private EmojiReactionListAdapter.OnRecyclerViewItemClickListener mListener;

    String[] dummyReaction = {"👍","❤","\uD83D\uDE06","\uD83D\uDE32","\uD83D\uDE22","\uD83D\uDE21"};

    private ChannelManageReactionModel reactionModel;

    public  EmojiReactionListAdapter(Context context, ChannelManageReactionModel reactionModel){
        this.context= context;
        this.reactionModel = reactionModel;
    }

    @NonNull
    @Override
    public EmojiReactionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new EmojiReactionListAdapter.ViewHolder(RecyclerReactionListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(EmojiReactionListAdapter.ViewHolder holder, int position) {

            holder.binding.reaction.setText(reactionModel.getData().getList().get(position).getEmojiCode());
            holder.binding.reaction.setOnClickListener(view -> mListener.onItemAccept(reactionModel.getData().getList().get(position).getReactionID(),reactionModel.getData().getList().get(position).getEmojiCode(),reactionModel.getData().getList().get(position).getName()));

    }

    @Override
    public int getItemCount() {
            if (reactionModel != null && reactionModel.getData()!= null && reactionModel.getData().getList() != null && !reactionModel.getData().getList().isEmpty()){
                return reactionModel.getData().getList().size();
            }
            else {
                return 0;
            }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerReactionListItemBinding binding;

        ViewHolder(@NonNull RecyclerReactionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public void setOnRecyclerViewItemClickListener(EmojiReactionListAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemAccept(int id,String emoji_code,String emoji_name);

    }
}


