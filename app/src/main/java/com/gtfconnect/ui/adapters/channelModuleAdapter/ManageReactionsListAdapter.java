package com.gtfconnect.ui.adapters.channelModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.RecyclerManageReactionsListItemBinding;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;

public class ManageReactionsListAdapter extends RecyclerView.Adapter<ManageReactionsListAdapter.ViewHolder> {

    private Context context;
    ChannelManageReactionModel reactionModel;

    public  ManageReactionsListAdapter(Context context,ChannelManageReactionModel reactionModel){
        this.context = context;
        this.reactionModel = reactionModel;
    }

    @NonNull
    @Override
    public ManageReactionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ManageReactionsListAdapter.ViewHolder(RecyclerManageReactionsListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ManageReactionsListAdapter.ViewHolder holder, int position) {

        if (reactionModel.getData().getList()!=null) {
            holder.binding.emoji.setText(reactionModel.getData().getList().get(position).getEmojiCode());
            holder.binding.emojiTitle.setText(reactionModel.getData().getList().get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return reactionModel.getData().getList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerManageReactionsListItemBinding binding;

        ViewHolder(@NonNull RecyclerManageReactionsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

