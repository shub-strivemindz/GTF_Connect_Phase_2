package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerManageReactionsListItemBinding;
import com.gtfconnect.databinding.RecyclerSubscriberListItemsBinding;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.models.channelResponseModel.ChannelManageSubscriberResponseModel;

import java.util.ArrayList;

public class ManageSubscribersListAdapter extends RecyclerView.Adapter<ManageSubscribersListAdapter.ViewHolder> {


    private Context context;

    ChannelManageSubscriberResponseModel subscriberResponseModel;


    public  ManageSubscribersListAdapter(Context context,ChannelManageSubscriberResponseModel subscriberResponseModel){
        this.context = context;
        this.subscriberResponseModel = subscriberResponseModel;


    }

    @NonNull
    @Override
    public ManageSubscribersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ManageSubscribersListAdapter.ViewHolder(RecyclerSubscriberListItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ManageSubscribersListAdapter.ViewHolder holder, int position) {

        if (subscriberResponseModel.getData()!=null){
            if (!subscriberResponseModel.getData().getList().isEmpty() && subscriberResponseModel.getData().getList() != null){
                String username = "";
                if (subscriberResponseModel.getData().getList().get(position).getUser().getFirstname()!=null &&  !subscriberResponseModel.getData().getList().get(position).getUser().getFirstname().isEmpty()){
                    username = subscriberResponseModel.getData().getList().get(position).getUser().getFirstname();
                }
                if (subscriberResponseModel.getData().getList().get(position).getUser().getLastname()!=null &&  !subscriberResponseModel.getData().getList().get(position).getUser().getLastname().isEmpty()){
                    username += subscriberResponseModel.getData().getList().get(position).getUser().getLastname();
                }

                holder.binding.title.setText(username);


                if (subscriberResponseModel.getData().getList().get(position).getUser().getProfileImage()!=null &&  !subscriberResponseModel.getData().getList().get(position).getUser().getProfileImage().isEmpty()) {

                    //Setting up loader on post
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(circularProgressDrawable);
                    requestOptions.error(R.drawable.image_not_found);
                    requestOptions.skipMemoryCache(true);
                    requestOptions.fitCenter();

                    Glide.with(context).load(subscriberResponseModel.getData().getList().get(position).getUser().getProfileImage()).
                            fitCenter().apply(requestOptions).
                            transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.profilePic);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return subscriberResponseModel.getData().getList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerSubscriberListItemsBinding binding;

        ViewHolder(@NonNull RecyclerSubscriberListItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

