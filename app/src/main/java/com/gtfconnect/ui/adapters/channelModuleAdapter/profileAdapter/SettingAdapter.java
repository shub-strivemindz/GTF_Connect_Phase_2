package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gtfconnect.databinding.FragmentUserSettingBinding;
import com.gtfconnect.interfaces.ChannelSettingListener;
import com.gtfconnect.models.GroupChannelProfileDetailModel;
import com.gtfconnect.ui.screenUI.channelModule.ChannelBlocklistScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelManagePermissionScreen;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    Context context;

    GroupChannelProfileDetailModel profileDetailModel;

    private int accessType = 0, discussion = 0, signMessage = 0,manipulateView = 0, viewChatHistory = 0;

    private ChannelSettingListener listener;

    public  SettingAdapter(Context context,GroupChannelProfileDetailModel profileDetailModel,ChannelSettingListener listener){
        this.context = context;
        this.profileDetailModel = profileDetailModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new SettingAdapter.ViewHolder(FragmentUserSettingBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SettingAdapter.ViewHolder holder, int position) {


        if(profileDetailModel.getData().getGcInfo() != null){
            if (profileDetailModel.getData().getGcInfo().getDescription() != null){
                holder.binding.channelDescription.setText(profileDetailModel.getData().getGcInfo().getDescription());
            }
        }


        if(profileDetailModel.getData().getGcInfo().getAccessType() != null && !profileDetailModel.getData().getGcInfo().getAccessType().isEmpty()){
            if (profileDetailModel.getData().getGcInfo().getAccessType().equalsIgnoreCase("public")){
                holder.binding.privateChannelSwitch.setChecked(true);
                holder.binding.privateChannelSwitchText.setText("On");
                accessType = 1;
            }
            else{
                holder.binding.privateChannelSwitch.setChecked(false);
                holder.binding.privateChannelSwitchText.setText("Off");
                accessType = 0;
            }
        }

        if(profileDetailModel.getData().getGcSetting().getAllowDiscussion() != null){
            if (profileDetailModel.getData().getGcSetting().getAllowDiscussion() == 1){
                holder.binding.discussionSwitch.setChecked(true);
                holder.binding.discussionSwitchText.setText("On");
                discussion = 1;
            }
            else{
                holder.binding.discussionSwitch.setChecked(false);
                holder.binding.discussionSwitchText.setText("Off");
                discussion = 0;
            }
        }

        if(profileDetailModel.getData().getGcSetting().getSignedMsg() != null){
            if (profileDetailModel.getData().getGcSetting().getSignedMsg() == 1){
                holder.binding.signMessageSwitch.setChecked(true);
                holder.binding.signMessageSwitchText.setText("On");
                signMessage = 1;
            }
            else{
                holder.binding.signMessageSwitch.setChecked(false);
                holder.binding.signMessageSwitchText.setText("Off");
                signMessage = 0;
            }
        }

        if(profileDetailModel.getData().getGcSetting().getManipulateViewsPercent() != null){
            if (profileDetailModel.getData().getGcSetting().getManipulateViewsPercent() == 0){
                holder.binding.manipulateViewSwitch.setChecked(false);
                holder.binding.manipulateViewSwitchText.setText("On");
                manipulateView = 0;
            }
            else{
                holder.binding.manipulateViewSwitch.setChecked(true);
                holder.binding.manipulateViewSwitchText.setText("On");
                manipulateView = profileDetailModel.getData().getGcSetting().getManipulateViewsPercent();
            }
        }



        holder.binding.managePermission.setOnClickListener(view -> listener.callPermissionClass());


        holder.binding.editChannelDescription.setOnClickListener(view -> {
            holder.binding.editChannelDescription.setVisibility(View.GONE);
            holder.binding.updateChannelDescription.setVisibility(View.VISIBLE);
            holder.binding.channelDescription.setEnabled(true);
        });

        holder.binding.updateChannelDescription.setOnClickListener(view -> {
            holder.binding.updateChannelDescription.setVisibility(View.GONE);
            holder.binding.editChannelDescription.setVisibility(View.VISIBLE);
            holder.binding.channelDescription.setEnabled(false);
        });

        holder.binding.blocklist.setOnClickListener(view -> context.startActivity(new Intent(context, ChannelBlocklistScreen.class)));


        holder.binding.privateChannelSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                accessType = 1;
                listener.updateAccessTypeStatus(accessType);
                holder.binding.privateChannelSwitchText.setText("On");
            }
            else {
                accessType = 0;
                listener.updateAccessTypeStatus(accessType);
                holder.binding.privateChannelSwitchText.setText("Off");
            }
        });

        holder.binding.discussionSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                discussion = 1;
                listener.updateDiscussionStatus(discussion);
                holder.binding.discussionSwitchText.setText("On");
            }
            else {
                discussion = 0;
                listener.updateDiscussionStatus(discussion);
                holder.binding.discussionSwitchText.setText("Off");
            }
        });

        holder.binding.signMessageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                signMessage = 1;
                listener.updateSignMessageStatus(signMessage);
                holder.binding.signMessageSwitchText.setText("On");
            }
            else {
                signMessage = 0;
                listener.updateSignMessageStatus(signMessage);
                holder.binding.signMessageSwitchText.setText("Off");
            }
        });

        holder.binding.viewChatHistorySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                viewChatHistory = 1;
                listener.updateViewChatHistoryStatus(viewChatHistory);
                holder.binding.viewChatHistorySwitchText.setText("On");
            }
            else {
                viewChatHistory = 0;
                listener.updateViewChatHistoryStatus(viewChatHistory);
                holder.binding.viewChatHistorySwitchText.setText("Off");
            }
        });

        holder.binding.manageReactionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.binding.manageReactionSwitchText.setText("On");
                    listener.callManageReactionsClass();
                }
                else{
                    holder.binding.manageReactionSwitchText.setText("Off");
                }
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


    public void updateData(GroupChannelProfileDetailModel profileDetailModel){
        this.profileDetailModel = profileDetailModel;
        notifyDataSetChanged();
    }
}

