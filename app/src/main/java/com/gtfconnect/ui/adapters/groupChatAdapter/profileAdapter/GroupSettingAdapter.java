package com.gtfconnect.ui.adapters.groupChatAdapter.profileAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentUserSettingBinding;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.BlocklistScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.ManagePermissionScreen;

public class GroupSettingAdapter extends RecyclerView.Adapter<GroupSettingAdapter.ViewHolder> {

    Context context;

    public GroupSettingAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentUserSettingBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.managePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ManagePermissionScreen.class));
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
                context.startActivity(new Intent(context, BlocklistScreen.class));
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

