package com.gtfconnect.ui.adapters.channelModuleAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentMessageCommentItemBinding;

public class ChannelCommentAdapter extends RecyclerView.Adapter<ChannelCommentAdapter.ViewHolder> {

    private int tempItemCount;

    public ChannelCommentAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public ChannelCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ChannelCommentAdapter.ViewHolder(FragmentMessageCommentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ChannelCommentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentMessageCommentItemBinding binding;

        ViewHolder(@NonNull FragmentMessageCommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

