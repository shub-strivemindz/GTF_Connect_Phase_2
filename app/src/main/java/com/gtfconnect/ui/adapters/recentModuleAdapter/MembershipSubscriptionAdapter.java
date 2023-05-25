package com.gtfconnect.ui.adapters.recentModuleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerMembershipSubscriptionOffersBinding;
import com.gtfconnect.databinding.RecyclerSubscriberListItemsBinding;
import com.gtfconnect.databinding.RecyclerSubscriptionPlanItemBinding;
import com.gtfconnect.interfaces.MembershipSubscriptionPlanListener;
import com.gtfconnect.models.authResponseModels.LocationPickerModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class MembershipSubscriptionAdapter extends RecyclerView.Adapter<MembershipSubscriptionAdapter.ViewHolder> {

    private Context context;

    List<InfoDbEntity.GcSubscriptionPlan> subscriptionPlans;

    private int selectedItem;

    private MembershipSubscriptionPlanListener listener;

    public MembershipSubscriptionAdapter(Context context, List<InfoDbEntity.GcSubscriptionPlan> subscriptionPlans, MembershipSubscriptionPlanListener listener) {
        this.context = context;
        this.subscriptionPlans = subscriptionPlans;

        this.listener = listener;

        selectedItem = 0;
    }

    @NonNull
    @Override
    public MembershipSubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MembershipSubscriptionAdapter.ViewHolder(RecyclerSubscriptionPlanItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MembershipSubscriptionAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;


       /* if (position == 1)
            holder.binding.title.setText("GTF Options 2.0");*/

        if (selectedItem == itemPosition) {
            holder.binding.monthContainer.setCardBackgroundColor(context.getColor(R.color.theme_green));
            holder.binding.validity.setTextColor(context.getColor(R.color.white));

        } else {
            holder.binding.monthContainer.setCardBackgroundColor(context.getColor(R.color.subscriptionMonthCardBackgroundColor));
            holder.binding.validity.setTextColor(context.getColor(R.color.subscriptionMonthCardTextColor));
        }


        if (subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration() != null && subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays() != null) {

            if (subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays() != 0) {
                holder.binding.validity.setText(Utils.getValidityDuration(subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays()));
            }
        }


        holder.binding.monthContainer.setOnClickListener(view -> {

            if (selectedItem != itemPosition) {
                selectedItem = itemPosition;

                listener.selectPlan(selectedItem);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionPlans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerSubscriptionPlanItemBinding binding;

        ViewHolder(@NonNull RecyclerSubscriptionPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

