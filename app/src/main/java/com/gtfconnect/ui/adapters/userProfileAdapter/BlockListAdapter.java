package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentBlocklistItemBinding;
import com.gtfconnect.databinding.FragmentSavedMessageBinding;
import com.gtfconnect.models.commonGroupChannelResponseModels.BlocklistResponseModel;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.Utils;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

    private int tempItemCount;
    private Context context;

    private BlocklistResponseModel blocklistResponseModel;

    public  BlockListAdapter(Context context,BlocklistResponseModel blocklistResponseModel){
        this.blocklistResponseModel = blocklistResponseModel;
        this.context = context;
    }

    @NonNull
    @Override
    public BlockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new BlockListAdapter.ViewHolder(FragmentBlocklistItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BlockListAdapter.ViewHolder holder, int position) {
        holder.binding.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.showSnackMessage(context,holder.binding.getRoot().findViewById(R.id.unblock),"Removed from Blocklist");
            }
        });

        String username = "";

        if (blocklistResponseModel.getData().get(position).getUser() != null){

            if (blocklistResponseModel.getData().get(position).getUser().getFirstname() != null){
                username = blocklistResponseModel.getData().get(position).getUser().getFirstname();
            }
            if (blocklistResponseModel.getData().get(position).getUser().getLastname() != null){
                username += " "+blocklistResponseModel.getData().get(position).getUser().getLastname();
            }

            holder.binding.username.setText(username);



            if (blocklistResponseModel.getData().get(position).getUser().getProfileThumbnail() != null){
                loadImage(position,holder.binding.userImage,blocklistResponseModel.getData().get(position).getUser().getProfileThumbnail());
            }
        }


    }



    private void loadImage(int position, ImageView image, String url){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.image_not_found);
        requestOptions.skipMemoryCache(false);
        requestOptions.fitCenter();

        Glide.with(context).load(url).
                fitCenter().apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade()).into(image);
    }


    @Override
    public int getItemCount() {
        if (blocklistResponseModel != null) {
            return blocklistResponseModel.getData().size();
        }
        else{
            return 2;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentBlocklistItemBinding binding;

        ViewHolder(@NonNull FragmentBlocklistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

