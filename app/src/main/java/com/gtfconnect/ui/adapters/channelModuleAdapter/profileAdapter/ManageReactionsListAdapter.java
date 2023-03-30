package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.RecyclerManageReactionsListItemBinding;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;

import java.util.ArrayList;

public class ManageReactionsListAdapter extends RecyclerView.Adapter<ManageReactionsListAdapter.ViewHolder> {


    private Context context;
    ChannelManageReactionModel reactionModel;

    ArrayList<Integer> selectedEmojiID;

    public  ManageReactionsListAdapter(Context context,ChannelManageReactionModel reactionModel){
        this.context = context;
        this.reactionModel = reactionModel;

        selectedEmojiID = new ArrayList<>();
    }

    @NonNull
    @Override
    public ManageReactionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ManageReactionsListAdapter.ViewHolder(RecyclerManageReactionsListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ManageReactionsListAdapter.ViewHolder holder, int position) {

        if (reactionModel.getData().getList()!=null) {
            holder.binding.emoji.setText(reactionModel.getData().getList().get(position).getEmojiCode());
            holder.binding.emojiTitle.setText(reactionModel.getData().getList().get(position).getName());

            if (reactionModel.getData().getList().get(position).getGcReactionStatus().equalsIgnoreCase("Active")) {
                holder.binding.emojiCheckbox.setVisibility(View.VISIBLE);
                reactionModel.getData().getList().get(position).setChecked(true);
                selectedEmojiID.add(reactionModel.getData().getList().get(position).getReactionID());
            }
            else {
                holder.binding.emojiCheckbox.setVisibility(View.GONE);
            }
        }



        holder.binding.smileSelection.setOnClickListener(view -> {
            if (reactionModel.getData().getList().get(position).isChecked()){
                for (int i =0;i<selectedEmojiID.size();i++)
                {
                    if (selectedEmojiID.get(i) == reactionModel.getData().getList().get(position).getReactionID()){
                        selectedEmojiID.remove(i);
                        holder.binding.emojiCheckbox.setVisibility(View.GONE);
                        reactionModel.getData().getList().get(position).setChecked(false);
                        break;
                    }
                }
            }
            else{
                reactionModel.getData().getList().get(position).setChecked(true);
                holder.binding.emojiCheckbox.setVisibility(View.VISIBLE);
                selectedEmojiID.add(reactionModel.getData().getList().get(position).getReactionID());
            }

            //Log.d("Total item selected ",String.valueOf(selectedEmojiID.size()));
        });
    }

    public ArrayList<Integer> getReactionList()
    {
        return selectedEmojiID;
    }

    @Override
    public int getItemCount() {
        return reactionModel.getData().getList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerManageReactionsListItemBinding binding;

        ViewHolder(@NonNull RecyclerManageReactionsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public void updateReactionList(ChannelManageReactionModel data)
    {
        reactionModel = data;
        notifyDataSetChanged();
    }
}

