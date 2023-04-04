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

public class EmojiReactionListAdapter extends RecyclerView.Adapter<EmojiReactionListAdapter.ViewHolder> {

    private Context context;

    private EmojiReactionListAdapter.OnRecyclerViewItemClickListener mListener;

    String[] dummyReaction = {"👍","❤","\uD83D\uDE06","\uD83D\uDE32","\uD83D\uDE22","\uD83D\uDE21"};

    public  EmojiReactionListAdapter(Context context){
        this.context= context;
    }

    @NonNull
    @Override
    public EmojiReactionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new EmojiReactionListAdapter.ViewHolder(RecyclerReactionListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(EmojiReactionListAdapter.ViewHolder holder, int position) {

        holder.binding.reaction.setText(dummyReaction[position]);

        holder.binding.reaction.setOnClickListener(view -> mListener.onItemAccept(position+1,dummyReaction[position],"Dummy Emoji Name"));
    }

    @Override
    public int getItemCount() {
        return dummyReaction.length;
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


