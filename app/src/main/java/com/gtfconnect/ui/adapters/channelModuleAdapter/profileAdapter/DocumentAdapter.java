package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserDocumentItemBinding;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private int tempItemCount;

    public  DocumentAdapter(int tempItemCount){
        this.tempItemCount = tempItemCount;
    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new DocumentAdapter.ViewHolder(FragmentUserDocumentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DocumentAdapter.ViewHolder holder, int position) {

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

