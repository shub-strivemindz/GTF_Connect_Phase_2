package com.gtfconnect.ui.adapters.groupChatAdapter.profileAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserDocumentItemBinding;

public class GroupDocumentAdapter extends RecyclerView.Adapter<GroupDocumentAdapter.ViewHolder> {

    private int tempItemCount;

    public GroupDocumentAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentUserDocumentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

