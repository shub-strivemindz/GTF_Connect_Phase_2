package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentHomeItemsBinding;

public class RecentViewAdapter extends RecyclerView.Adapter<RecentViewAdapter.ViewHolder> {

    private int tempItemCount;
    private Context context;

    public  RecentViewAdapter(Context context,int tempItemCount){
        this.tempItemCount = tempItemCount;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(FragmentHomeItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Temporary Condition for Recent View Layout

        switch (position)
        {
            case 2 :
                holder.binding.title.setText("Mentor");
                holder.binding.subTitle.setText("Typing....");
                holder.binding.notificationCount.setText("4");

                /*holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, MentorChatScreen.class));
                    }
                });*/
                break;

            case 3 :
                holder.binding.subTitle.setText("Trending");
                holder.binding.subTitle.setTextColor(context.getResources().getColor(R.color.card_yellow));
                holder.binding.trendingTag.setVisibility(View.VISIBLE);
                holder.binding.notification.setVisibility(View.GONE);
                break;

            default:

                /*holder.binding.chatItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, ChannelChatsScreen.class));
                    }
                });*/
        }
    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentHomeItemsBinding binding;

        ViewHolder(@NonNull FragmentHomeItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

