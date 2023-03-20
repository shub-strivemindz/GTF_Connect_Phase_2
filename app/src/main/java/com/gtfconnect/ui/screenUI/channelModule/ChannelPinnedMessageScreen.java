package com.gtfconnect.ui.screenUI.channelModule;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivityPinnedMessageBinding;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelPinnedMessageAdapter;

public class ChannelPinnedMessageScreen extends AppCompatActivity {

    ActivityPinnedMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinnedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Loading pinned message data ------
        ChannelPinnedMessageAdapter pinnedMessageViewAdapter= new ChannelPinnedMessageAdapter(4);
        binding.pinnedMessageRecycler.setHasFixedSize(true);
        binding.pinnedMessageRecycler.setLayoutManager(new LinearLayoutManager(ChannelPinnedMessageScreen.this));
        binding.pinnedMessageRecycler.setAdapter(pinnedMessageViewAdapter);

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
