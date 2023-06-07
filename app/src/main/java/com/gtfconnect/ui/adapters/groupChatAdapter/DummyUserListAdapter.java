package com.gtfconnect.ui.adapters.groupChatAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerDummyUserItemsBinding;
import com.gtfconnect.databinding.RecyclerExclusiveItemBinding;
import com.gtfconnect.interfaces.UpdateGroupDummyUserListener;
import com.gtfconnect.models.groupResponseModel.GetDummyUserModel;
import com.gtfconnect.ui.adapters.ExclusiveOfferAdapter;
import com.gtfconnect.ui.screenUI.recentModule.ExclusiveOfferScreen;

public class DummyUserListAdapter extends RecyclerView.Adapter<DummyUserListAdapter.ViewHolder> {

    private Context context;

    private GetDummyUserModel getDummyUserModel;

    private UpdateGroupDummyUserListener listener;

    public  DummyUserListAdapter(Context context,GetDummyUserModel getDummyUserModel, UpdateGroupDummyUserListener listener){
        this.context= context;
        this.getDummyUserModel = getDummyUserModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DummyUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new DummyUserListAdapter.ViewHolder(RecyclerDummyUserItemsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DummyUserListAdapter.ViewHolder holder, int position) {

        String dummy_user = getDummyUserModel.getData().getList().get(position).getFirstname() + getDummyUserModel.getData().getList().get(position).getLastname();

        holder.binding.dummyRadioButton.setText(dummy_user);

        holder.binding.dummyUserContainer.setOnClickListener(view -> listener.updateDummyUser(getDummyUserModel.getData().getList().get(position).getDummyUserID(), getDummyUserModel.getData().getList().get(position).getIsAdmin()));

    }

    @Override
    public int getItemCount() {
        if (getDummyUserModel != null && getDummyUserModel.getData() != null && getDummyUserModel.getData().getList() != null) {
            return getDummyUserModel.getData().getList().size();
        }
        else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerDummyUserItemsBinding binding;

        ViewHolder(@NonNull RecyclerDummyUserItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

