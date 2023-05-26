package com.gtfconnect.ui.adapters.dashboardAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentHomeItemsBinding;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.ui.screenUI.groupModule.GroupChatScreen;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class RecentViewAdapter extends RecyclerView.Adapter<RecentViewAdapter.ViewHolder> {

    private List<DashboardListEntity> responseModel;
    private Context context;

    private String profileImageBaseUrl;


    public  RecentViewAdapter(Context context, List<DashboardListEntity> responseModel,String profileImageBaseUrl){
        this.responseModel = responseModel;
        this.context = context;
        this.profileImageBaseUrl = profileImageBaseUrl;
    }

    @NonNull
    @Override
    public RecentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new RecentViewAdapter.ViewHolder(FragmentHomeItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecentViewAdapter.ViewHolder holder, int position) {

        final int index = position;

        if (responseModel.get(index).getGroup() != null) {

            if (responseModel.get(index).getGroup().getName() != null) {
                holder.binding.title.setText(responseModel.get(index).getGroup().getName());
            }

            if(responseModel.get(position).getGroup().getMessage() != null && !responseModel.get(position).getGroup().getMessage().isEmpty())
            {

                if (responseModel.get(position).getGroup().getMessage().get(0).getChatType() != null){

                    Log.d("chat_type",responseModel.get(position).getGroup().getMessage().get(0).getChatType().trim()+" = !");
                    if (responseModel.get(position).getGroup().getMessage().get(0).getChatType().trim().equalsIgnoreCase("file")){
                        holder.binding.subTitle.setText("Media File");
                    }
                    else if (responseModel.get(position).getGroup().getMessage().get(0).getMessage() != null && !responseModel.get(position).getGroup().getMessage().get(0).getMessage().trim().isEmpty()){
                        holder.binding.subTitle.setText(responseModel.get(position).getGroup().getMessage().get(0).getMessage());
                    }
                }


                if (responseModel.get(index).getGroup().getMessage().get(0).getUpdatedAt() != null) {
                    holder.binding.time.setText(Utils.getChipDate(responseModel.get(index).getGroup().getMessage().get(0).getUpdatedAt()));
                }
                else{
                    holder.binding.time.setText("");
                }
            }
            else{
                holder.binding.subTitle.setText("");
                holder.binding.time.setText("");
            }



            if (responseModel.get(index).getUnreadcount() != null && !responseModel.get(index).getUnreadcount().isEmpty()) {
                if (responseModel.get(index).getUnreadcount().equalsIgnoreCase("0")){
                    holder.binding.notification.setVisibility(View.GONE);
                }
                else {
                    holder.binding.notificationCount.setText(responseModel.get(index).getUnreadcount());
                    holder.binding.notification.setVisibility(View.VISIBLE);
                }
            }
            else{
                holder.binding.notification.setVisibility(View.GONE);
            }


            if (responseModel.get(index).getGroup().getProfileImage() != null) {
                String url = profileImageBaseUrl + responseModel.get(index).getGroup().getProfileImage();
                GlideUtils.loadImage(context, holder.binding.groupChannelIcon, url);
            }
            else{
                holder.binding.groupChannelIcon.setImageDrawable(context.getDrawable(R.drawable.no_image_logo_background));
            }


            holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PreferenceConnector.writeString(context, PreferenceConnector.GC_MEMBER_ID, responseModel.get(index).getGCMemberID());
                    PreferenceConnector.writeString(context, PreferenceConnector.GC_CHANNEL_ID, responseModel.get(index).getGroupChannelID().toString());
                    PreferenceConnector.writeString(context, PreferenceConnector.GC_NAME, responseModel.get(index).getGroup().getName());


                    if (responseModel.get(index).getGroup().getType().equalsIgnoreCase("group")) {
                        Intent intent = new Intent(context, GroupChatScreen.class);
                        context.startActivity(intent);
                    } else if (responseModel.get(index).getGroup().getType().equalsIgnoreCase("channel")) {
                        Intent intent = new Intent(context, ChannelChatsScreen.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return responseModel.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentHomeItemsBinding binding;

        ViewHolder(@NonNull FragmentHomeItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



    public void updateList(List<DashboardListEntity> responseModel, String profileImageBaseUrl){
        this.responseModel = responseModel;
        this.profileImageBaseUrl = profileImageBaseUrl;
        notifyDataSetChanged();
    }
}
