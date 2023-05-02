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
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.ui.screenUI.groupModule.GroupChatScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewHolder> {

    private List<DashboardListEntity> responseModel;
    private Context context;


    public  GroupViewAdapter(Context context, List<DashboardListEntity> responseModel){
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

        holder.binding.title.setText(responseModel.get(index).getGroup().getName());

        if (responseModel.get(index).getGroup().getMessage() != null && !responseModel.get(index).getGroup().getMessage().isEmpty()) {
            if (responseModel.get(index).getGroup().getMessage().get(0).getMessage() != null)
                holder.binding.subTitle.setText(responseModel.get(index).getGroup().getMessage().get(0).getMessage());


            holder.binding.time.setText(Utils.getHeaderDate(responseModel.get(index).getGroup().getMessage().get(0).getUpdatedAt()));
        }





        if (responseModel.get(index).getUnreadcount() != null && !responseModel.get(index).getUnreadcount().isEmpty()){
            holder.binding.notificationCount.setText(responseModel.get(index).getUnreadcount());
        }


        //Todo : Add Glide to fetch profile image when provide base url

        holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GroupChatScreen.class);


                PreferenceConnector.writeString(context,PreferenceConnector.GC_MEMBER_ID,responseModel.get(index).getGCMemberID());
                PreferenceConnector.writeString(context,PreferenceConnector.GC_CHANNEL_ID,responseModel.get(index).getGroupChannelID().toString());
                PreferenceConnector.writeString(context,PreferenceConnector.GC_NAME,responseModel.get(index).getGroup().getName());

                //PreferenceConnector.writeString(context,PreferenceConnector.connect_userID,responseModel.get(index).getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("LIST_SIZE",String.valueOf(responseModel.size()));
        return responseModel.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentHomeItemsBinding binding;

        ViewHolder(@NonNull FragmentHomeItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}

