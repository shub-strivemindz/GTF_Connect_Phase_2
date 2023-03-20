package com.gtfconnect.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentSavedMessageBinding;
import com.gtfconnect.databinding.RecyclerMembershipSubscriptionOffersBinding;
import com.gtfconnect.ui.adapters.userProfileAdapter.SavedMessageAdapter;

public class MembershipSubscriptionAdapter extends RecyclerView.Adapter<MembershipSubscriptionAdapter.ViewHolder> {

    private Context context;

    public  MembershipSubscriptionAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public MembershipSubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MembershipSubscriptionAdapter.ViewHolder(RecyclerMembershipSubscriptionOffersBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MembershipSubscriptionAdapter.ViewHolder holder, int position) {

        if (position == 1)
            holder.binding.title.setText("GTF Options 2.0");
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerMembershipSubscriptionOffersBinding binding;

        ViewHolder(@NonNull RecyclerMembershipSubscriptionOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

