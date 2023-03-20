package com.gtfconnect.ui.screenUI.groupModule;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityEditGroupChannelProfileBinding;

public class GroupEditProfileScreen extends AppCompatActivity {

    ActivityEditGroupChannelProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGroupChannelProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
