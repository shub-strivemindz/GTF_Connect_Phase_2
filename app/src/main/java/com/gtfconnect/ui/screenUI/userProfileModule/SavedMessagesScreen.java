package com.gtfconnect.ui.screenUI.userProfileModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.ActivitySavedMessageBinding;
import com.gtfconnect.ui.adapters.userProfileAdapter.SavedMessageAdapter;

public class SavedMessagesScreen extends AppCompatActivity {

    ActivitySavedMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SavedMessagesScreen.this, UserProfileScreen.class));
                finish();
            }
        });


        // load saved messages  ------
        SavedMessageAdapter savedMessageViewAdapter= new SavedMessageAdapter(SavedMessagesScreen.this,4);
        binding.savedMessageRecycler.setHasFixedSize(true);
        binding.savedMessageRecycler.setLayoutManager(new LinearLayoutManager(SavedMessagesScreen.this));
        binding.savedMessageRecycler.setAdapter(savedMessageViewAdapter);
    }
}
