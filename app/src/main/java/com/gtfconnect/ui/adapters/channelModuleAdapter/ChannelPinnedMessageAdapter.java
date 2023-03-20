package com.gtfconnect.ui.adapters.channelModuleAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentPinnedMessageBinding;

public class ChannelPinnedMessageAdapter extends RecyclerView.Adapter<ChannelPinnedMessageAdapter.ViewHolder> {

    private int tempItemCount;

    public ChannelPinnedMessageAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public ChannelPinnedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ChannelPinnedMessageAdapter.ViewHolder(FragmentPinnedMessageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ChannelPinnedMessageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPinnedMessageBinding binding;

        ViewHolder(@NonNull FragmentPinnedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

