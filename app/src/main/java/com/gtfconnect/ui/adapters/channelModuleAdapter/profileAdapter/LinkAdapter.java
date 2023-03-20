package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserLinkItemBinding;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {

    private int tempItemCount;

    public  LinkAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new LinkAdapter.ViewHolder(FragmentUserLinkItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(LinkAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentUserLinkItemBinding binding;

        ViewHolder(@NonNull FragmentUserLinkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

