package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentBlocklistItemBinding;
import com.gtfconnect.databinding.FragmentSavedMessageBinding;
import com.gtfconnect.utilities.Utils;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

    private int tempItemCount;
    private Context context;

    public  BlockListAdapter(Context context,int tempItemCount){
        this.tempItemCount = tempItemCount;
        this.context = context;
    }

    @NonNull
    @Override
    public BlockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new BlockListAdapter.ViewHolder(FragmentBlocklistItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BlockListAdapter.ViewHolder holder, int position) {
        holder.binding.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.showSnackMessage(context,holder.binding.getRoot().findViewById(R.id.unblock),"Removed from Blocklist");
            }
        });

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentBlocklistItemBinding binding;

        ViewHolder(@NonNull FragmentBlocklistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

