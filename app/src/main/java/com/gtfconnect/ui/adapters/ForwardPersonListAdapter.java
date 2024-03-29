package com.gtfconnect.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerExclusiveItemBinding;
import com.gtfconnect.databinding.RecyclerForwardMessagePersonListItemBinding;
import com.gtfconnect.ui.screenUI.recentModule.ExclusiveOfferScreen;

public class ForwardPersonListAdapter extends RecyclerView.Adapter<ForwardPersonListAdapter.ViewHolder> {

    private Context context;

    public  ForwardPersonListAdapter(Context context){
        this.context= context;
    }

    @NonNull
    @Override
    public ForwardPersonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ForwardPersonListAdapter.ViewHolder(RecyclerForwardMessagePersonListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ForwardPersonListAdapter.ViewHolder holder, int position) {

        if (position == 0)
        {
            holder.binding.profileImage.setVisibility(View.GONE);
            holder.binding.saveImage.setVisibility(View.VISIBLE);
            holder.binding.userName.setText("Save Message");
        }
        else{
            holder.binding.profileImage.setVisibility(View.VISIBLE);
            holder.binding.saveImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerForwardMessagePersonListItemBinding binding;

        ViewHolder(@NonNull RecyclerForwardMessagePersonListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

