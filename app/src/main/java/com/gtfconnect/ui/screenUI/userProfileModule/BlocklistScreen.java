package com.gtfconnect.ui.screenUI.userProfileModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivityBlocklistBinding;
import com.gtfconnect.ui.adapters.userProfileAdapter.BlockListAdapter;
import com.gtfconnect.ui.adapters.userProfileAdapter.SavedMessageAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;


public class BlocklistScreen extends AppCompatActivity {

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
        BlockListAdapter blocklistViewAdapter= new BlockListAdapter(BlocklistScreen.this,2);
        binding.blocklistRecycler.setHasFixedSize(true);
        binding.blocklistRecycler.setLayoutManager(new LinearLayoutManager(BlocklistScreen.this));
        binding.blocklistRecycler.setAdapter(blocklistViewAdapter);
    }
}
