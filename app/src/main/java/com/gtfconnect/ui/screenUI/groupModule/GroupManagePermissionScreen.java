package com.gtfconnect.ui.screenUI.groupModule;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityManagePermissionBinding;

public class GroupManagePermissionScreen extends AppCompatActivity {

    ActivityManagePermissionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagePermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
