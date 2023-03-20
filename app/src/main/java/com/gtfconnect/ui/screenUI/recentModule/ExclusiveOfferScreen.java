package com.gtfconnect.ui.screenUI.recentModule;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivityExclusiveOfferBinding;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.ui.adapters.MembershipSubscriptionAdapter;

public class ExclusiveOfferScreen extends AppCompatActivity {

    ActivityExclusiveOfferBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExclusiveOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.close.setOnClickListener(view -> onBackPressed());

        MembershipSubscriptionAdapter groupViewAdapter= new MembershipSubscriptionAdapter(this);
        binding.offerRecycler.setHasFixedSize(true);
        binding.offerRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.offerRecycler.setAdapter(groupViewAdapter);
    }
}
