package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserSettingBinding;
import com.gtfconnect.ui.screenUI.channelModule.ChannelBlocklistScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelManagePermissionScreen;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    Context context;

    public  SettingAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new SettingAdapter.ViewHolder(FragmentUserSettingBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SettingAdapter.ViewHolder holder, int position) {
        holder.binding.managePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChannelManagePermissionScreen.class));
            }
        });


        holder.binding.editChannelDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.editChannelDescription.setVisibility(View.GONE);
                holder.binding.updateChannelDescription.setVisibility(View.VISIBLE);
                holder.binding.channelDescription.setEnabled(true);
            }
        });

        holder.binding.updateChannelDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.updateChannelDescription.setVisibility(View.GONE);
                holder.binding.editChannelDescription.setVisibility(View.VISIBLE);
                holder.binding.channelDescription.setEnabled(false);
            }
        });

        holder.binding.blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChannelBlocklistScreen.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentUserSettingBinding binding;

        ViewHolder(@NonNull FragmentUserSettingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

