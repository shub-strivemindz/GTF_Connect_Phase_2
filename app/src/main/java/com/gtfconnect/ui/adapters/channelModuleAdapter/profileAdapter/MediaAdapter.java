package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserDocumentItemBinding;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private int tempItemCount;

    public  MediaAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public MediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MediaAdapter.ViewHolder(FragmentUserDocumentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MediaAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentUserDocumentItemBinding binding;

        ViewHolder(@NonNull FragmentUserDocumentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

