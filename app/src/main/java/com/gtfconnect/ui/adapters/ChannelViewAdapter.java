package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentHomeItemsBinding;

import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class ChannelViewAdapter extends RecyclerView.Adapter<ChannelViewAdapter.ViewHolder> {

    private List<DashboardListEntity> responseModel;
    private Context context;

    private String profileImageBaseUrl;

    public  ChannelViewAdapter(Context context, List<DashboardListEntity> responseModel,String profileImageBaseUrl){
        this.responseModel = responseModel;
        this.context = context;
        this.profileImageBaseUrl = profileImageBaseUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentHomeItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int index = position;

        if (responseModel.get(index).getGroup() != null) {

            holder.binding.title.setText(responseModel.get(index).getGroup().getName());

           /* if (responseModel.get(index).getGroup().getMessage() != null && !responseModel.get(index).getGroup().getMessage().isEmpty()) {
                if (responseModel.get(index).getGroup().getMessage().get(0).getMessage() != null)
                    holder.binding.subTitle.setText(responseModel.get(index).getGroup().getMessage().get(0).getMessage());


                holder.binding.time.setText(Utils.getChipDate(responseModel.get(index).getGroup().getMessage().get(0).getUpdatedAt()));
            }*/


            if(responseModel.get(position).getGroup().getMessage() != null)
            {
                if (responseModel.get(position).getGroup().getMessage().get(0).getMessage() != null && !responseModel.get(position).getGroup().getMessage().get(0).getMessage().trim().isEmpty()){
                    holder.binding.subTitle.setText(responseModel.get(position).getGroup().getMessage().get(0).getMessage());
                }
                else{
                    holder.binding.subTitle.setText("Media File");
                }

                holder.binding.time.setText(Utils.getChipDate(responseModel.get(index).getGroup().getMessage().get(0).getUpdatedAt()));
            }


            if (responseModel.get(index).getGroup().getProfileImage() != null) {
                String url = profileImageBaseUrl+responseModel.get(index).getGroup().getProfileImage();
                GlideUtils.loadImage(context,holder.binding.groupChannelIcon,url);
            }


            if (responseModel.get(index).getUnreadcount() != null && !responseModel.get(index).getUnreadcount().isEmpty()) {
                holder.binding.notificationCount.setText(responseModel.get(index).getUnreadcount());
            }


            //Todo : Add Glide to fetch profile image when provide base url

            holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ChannelChatsScreen.class);


                    PreferenceConnector.writeString(context, PreferenceConnector.GC_MEMBER_ID, responseModel.get(index).getGCMemberID());
                    PreferenceConnector.writeString(context, PreferenceConnector.GC_CHANNEL_ID, responseModel.get(index).getGroupChannelID().toString());
                    PreferenceConnector.writeString(context, PreferenceConnector.GC_NAME, responseModel.get(index).getGroup().getName());

                    //PreferenceConnector.writeString(context,PreferenceConnector.connect_userID,responseModel.get(index).getUserID());
                    context.startActivity(intent);
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

