package com.gtfconnect.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerExclusiveItemBinding;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.ui.screenUI.recentModule.ExclusiveOfferScreen;

import java.util.List;

public class ExclusiveOfferAdapter extends RecyclerView.Adapter<ExclusiveOfferAdapter.ViewHolder> {

    private Context context;

    private List<ExclusiveOfferDataModel> exclusiveOfferDataModels;

    public  ExclusiveOfferAdapter(Context context,List<ExclusiveOfferDataModel> exclusiveOfferDataModels){
        this.context= context;
        this.exclusiveOfferDataModels = exclusiveOfferDataModels;
    }

    @NonNull
    @Override
    public ExclusiveOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ExclusiveOfferAdapter.ViewHolder(RecyclerExclusiveItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ExclusiveOfferAdapter.ViewHolder holder, int position) {

        holder.binding.offerTag.setTypeface(holder.binding.offerTag.getTypeface(), Typeface.BOLD_ITALIC);


        if (exclusiveOfferDataModels.get(position).getName() != null ){
            holder.binding.title.setText(exclusiveOfferDataModels.get(position).getName());
        }

        if (exclusiveOfferDataModels.get(position).getPopupInfoImage() != null){
            loadImage(exclusiveOfferDataModels.get(position).getPopupInfoImage(),holder.binding.userAvatar);
        }

        holder.binding.offerTag.setTextColor(context.getResources().getColor(R.color.exclusive_pink));
        holder.binding.offerTag.setText(context.getResources().getString(R.string.trending));
        holder.binding.offerTagBackground.setCardBackgroundColor(context.getResources().getColor(R.color.exclusive_pink_background));












/*
        if (position == 0){
            holder.binding.title.setText("Trading In The Zone 2.0 Extended");


            holder.binding.chatItem.setOnClickListener(view -> context.startActivity(new Intent(context, ExclusiveOfferScreen.class)));
        }
        if (position == 1){
            holder.binding.title.setText("GTF Option 2.0");
            holder.binding.offerTag.setTypeface(holder.binding.offerTag.getTypeface(), Typeface.BOLD_ITALIC);
            holder.binding.offerTag.setTextColor(context.getResources().getColor(R.color.exclusive_orange));
            holder.binding.offerTag.setText(context.getResources().getString(R.string.best_seller));
            holder.binding.offerTagBackground.setCardBackgroundColor(context.getResources().getColor(R.color.exclusive_orange_background));
            holder.binding.chatItem.setOnClickListener(view -> context.startActivity(new Intent(context, ExclusiveOfferScreen.class)));
        }
        if (position == 2){
            holder.binding.title.setText("Trading In The Zone 2.0");
            holder.binding.offerTag.setTypeface(holder.binding.offerTag.getTypeface(), Typeface.BOLD_ITALIC);
            holder.binding.offerTag.setTextColor(context.getResources().getColor(R.color.exclusive_purple));
            holder.binding.offerTagBackground.setCardBackgroundColor(context.getResources().getColor(R.color.exclusive_purple_background));
            holder.binding.offerTag.setText(context.getResources().getString(R.string.free_get_started));
             holder.binding.chatItem.setOnClickListener(view -> {
                 Dialog free_membership_dialog = new Dialog(context);

                 free_membership_dialog.setContentView(R.layout.dialog_free_group_channel_banner);
                 free_membership_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 free_membership_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 free_membership_dialog.show();
             });
        }*/

    }



    private void loadImage(String imageUrl, ImageView image)
    {
        //Setting up loader on post
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();

        Glide.with(context).load(imageUrl).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(image);
    }

    @Override
    public int getItemCount() {
        return exclusiveOfferDataModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerExclusiveItemBinding binding;

        ViewHolder(@NonNull RecyclerExclusiveItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

