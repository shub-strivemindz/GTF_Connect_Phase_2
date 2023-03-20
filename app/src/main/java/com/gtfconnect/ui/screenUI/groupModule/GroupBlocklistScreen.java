package com.gtfconnect.ui.screenUI.groupModule;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivityBlocklistBinding;
import com.gtfconnect.ui.adapters.userProfileAdapter.BlockListAdapter;

public class GroupBlocklistScreen extends AppCompatActivity {

    ActivityBlocklistBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlocklistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // load blocklist data  ------
        BlockListAdapter blocklistViewAdapter= new BlockListAdapter(GroupBlocklistScreen.this,3);
        binding.blocklistRecycler.setHasFixedSize(true);
        binding.blocklistRecycler.setLayoutManager(new LinearLayoutManager(GroupBlocklistScreen.this));
        binding.blocklistRecycler.setAdapter(blocklistViewAdapter);
    }
}
