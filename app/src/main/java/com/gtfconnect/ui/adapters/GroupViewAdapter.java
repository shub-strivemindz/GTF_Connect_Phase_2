package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentHomeItemsBinding;
import com.gtfconnect.models.groupResponseModel.GroupResponseModel;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewHolder> {

    private GroupResponseModel responseModel;
    private Context context;


    public  GroupViewAdapter(Context context, GroupResponseModel responseModel){
        this.responseModel = responseModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentHomeItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int index = position;

        holder.binding.title.setText(responseModel.getData().get(index).getGroup().getName());

        if (responseModel.getData().get(index).getGroup().getMessage() != null && !responseModel.getData().get(index).getGroup().getMessage().isEmpty()) {
            if (responseModel.getData().get(index).getGroup().getMessage().get(0).getMessage() != null)
                holder.binding.subTitle.setText(responseModel.getData().get(index).getGroup().getMessage().get(0).getMessage());


            holder.binding.time.setText(Utils.getDisplayableTime(responseModel.getData().get(index).getGroup().getMessage().get(0).getCreatedAt()));
        }





        if (responseModel.getData().get(index).getUnreadcount() != null && !responseModel.getData().get(index).getUnreadcount().isEmpty()){
            holder.binding.notificationCount.setText(responseModel.getData().get(index).getUnreadcount());
        }


        //Todo : Add Glide to fetch profile image when provide base url

        holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChannelChatsScreen.class);


                PreferenceConnector.writeString(context,PreferenceConnector.GC_MEMBER_ID,responseModel.getData().get(index).getGCMemberID());
                PreferenceConnector.writeString(context,PreferenceConnector.GC_CHANNEL_ID,responseModel.getData().get(index).getGroupChannelID().toString());
                PreferenceConnector.writeString(context,PreferenceConnector.GC_NAME,responseModel.getData().get(index).getGroup().getName());

                //PreferenceConnector.writeString(context,PreferenceConnector.connect_userID,responseModel.getData().get(index).getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("LIST_SIZE",String.valueOf(responseModel.getData().size()));
        return responseModel.getData().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentHomeItemsBinding binding;

        ViewHolder(@NonNull FragmentHomeItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}

