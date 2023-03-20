package com.gtfconnect.ui.screenUI.mentorModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivityMentorChatBinding;
import com.gtfconnect.ui.adapters.mentorModuleAdapter.MentorChatAdapter;
import com.gtfconnect.ui.screenUI.channelModule.ChannelPinnedMessageScreen;

import java.util.ArrayList;
import java.util.List;

public class MentorChatScreen extends AppCompatActivity {

    private ActivityMentorChatBinding binding;

    private List<Integer> message_type = new ArrayList<>();
    private List<Integer> message_status = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message_status.add(0); // Received
        message_status.add(0); // Received
        message_status.add(0); // Received
        message_status.add(1); // Sent
        message_status.add(1); // Sent

        message_type.add(0); // message
        message_type.add(1); // forwarded
        message_type.add(2); // Image
        message_type.add(0); // message
        message_type.add(1); // quote

        binding = ActivityMentorChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MentorChatScreen.this, ChannelPinnedMessageScreen.class));
            }
        });


        // Load Mentor List Data -----
        MentorChatAdapter recentViewAdapter= new MentorChatAdapter(MentorChatScreen.this,5,message_type,message_status);
        binding.mentorChatRecycler.setHasFixedSize(true);
        binding.mentorChatRecycler.setLayoutManager(new LinearLayoutManager(MentorChatScreen.this));
        binding.mentorChatRecycler.setAdapter(recentViewAdapter);
    }
}
