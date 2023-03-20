package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentBlocklistItemBinding;
import com.gtfconnect.databinding.RecyclerImagePreviewItemBinding;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.models.CityData;
import com.gtfconnect.models.ImagePreviewModel;
import com.gtfconnect.ui.adapters.authModuleAdapter.CityListAdapter;
import com.gtfconnect.ui.adapters.userProfileAdapter.BlockListAdapter;
import com.gtfconnect.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class ImageMiniPreviewAdapter extends RecyclerView.Adapter<ImageMiniPreviewAdapter.ViewHolder> {

    private Context context;

    private ArrayList<ImagePreviewModel> uriList;

    private int oldPosition = 0;

    private ImagePreviewListener listener;

    public  ImageMiniPreviewAdapter(Context context, ArrayList<ImagePreviewModel> uriList, ImagePreviewListener listener){
        this.uriList = uriList;
        this.context = context;
        this.listener = listener;

        listener.imagePreviewListener(0,uriList);
        uriList.get(oldPosition).setActive(true);
    }



    @NonNull
    @Override
    public ImageMiniPreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ImageMiniPreviewAdapter.ViewHolder(RecyclerImagePreviewItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ImageMiniPreviewAdapter.ViewHolder holder, int index) {

        final int position = index;

        holder.binding.miniPreview.setImageURI(uriList.get(position).getUri());

        if (uriList.get(position).isActive())
        {
            holder.binding.miniPreviewLayer.setVisibility(View.VISIBLE);
        }
        else{
            holder.binding.miniPreviewLayer.setVisibility(View.GONE);
        }


        holder.binding.miniPreview.setOnClickListener(view -> {

            if (oldPosition != position){
                uriList.get(oldPosition).setActive(false);
                notifyItemChanged(oldPosition);
            }
            oldPosition = position;
            uriList.get(position).setActive(true);
            notifyItemChanged(position);


            listener.imagePreviewListener(position,uriList);

        });
    }

    public void updateList(ArrayList<ImagePreviewModel> uriList,int selectedIndex)
    {
        this.uriList = uriList;
        oldPosition = selectedIndex;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return uriList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerImagePreviewItemBinding binding;

        ViewHolder(@NonNull RecyclerImagePreviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}