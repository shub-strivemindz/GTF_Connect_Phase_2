package com.gtfconnect.ui.screenUI.recentModule;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.databinding.ActivityExclusiveOfferBinding;
import com.gtfconnect.interfaces.MembershipSubscriptionPlanListener;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.recentModuleAdapter.MembershipSubscriptionAdapter;
import com.gtfconnect.utilities.Utils;

import java.lang.reflect.Type;
import java.util.List;

public class ExclusiveOfferScreen extends AppCompatActivity implements MembershipSubscriptionPlanListener {

    ActivityExclusiveOfferBinding binding;

    List<InfoDbEntity.GcSubscriptionPlan> subscriptionPlans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExclusiveOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.close.setOnClickListener(view -> onBackPressed());




        String data = getIntent().getStringExtra("plans");
        Type type = new TypeToken<List<InfoDbEntity.GcSubscriptionPlan>>(){}.getType();

        subscriptionPlans = new Gson().fromJson(data,type);


        if (subscriptionPlans != null && !subscriptionPlans.isEmpty()) {

            updatePlanDetails(0);

            MembershipSubscriptionAdapter groupViewAdapter = new MembershipSubscriptionAdapter(this,subscriptionPlans,this);
            binding.offerRecycler.setHasFixedSize(true);
            binding.offerRecycler.setLayoutManager(new FlexboxLayoutManager(this));
            binding.offerRecycler.setAdapter(groupViewAdapter);

        }
    }

    @Override
    public void selectPlan(int position) {

        updatePlanDetails(position);
    }



    private void updatePlanDetails(int position){

        if (subscriptionPlans.get(position).getSubscriptionPlan() != null){

            if (subscriptionPlans.get(position).getSubscriptionPlan().getName() != null){
                binding.planName.setText(subscriptionPlans.get(position).getSubscriptionPlan().getName());
            }

            if (subscriptionPlans.get(position).getSubscriptionPlan().getBenefit() != null){
                binding.benefitsContainer.setVisibility(View.VISIBLE);
            }
            else{
                binding.benefitsContainer.setVisibility(View.GONE);
            }

            if (subscriptionPlans.get(position).getSubscriptionPlan().getDescription() != null){
                binding.planDescription.setText(subscriptionPlans.get(position).getSubscriptionPlan().getDescription());

                binding.planDescriptionContainer.setVisibility(View.VISIBLE);
            }
            else{
                binding.planDescriptionContainer.setVisibility(View.GONE);
            }

            if (subscriptionPlans.get(position).getSubscriptionPlan().getNetPrice() != null){
                binding.subscriptionAmount.setText(""+ subscriptionPlans.get(position).getSubscriptionPlan().getNetPrice());
            }

            if (subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration() != null && subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays() != null){

                if (subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays() != 0){
                    binding.validity.setText(Utils.getValidityDuration(subscriptionPlans.get(position).getSubscriptionPlan().getTermDuration().getValueInDays()));
                }
            }
        }
    }
}
