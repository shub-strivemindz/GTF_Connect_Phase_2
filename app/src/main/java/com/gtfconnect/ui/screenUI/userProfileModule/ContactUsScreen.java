package com.gtfconnect.ui.screenUI.userProfileModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityContactUsBinding;

public class ContactUsScreen extends AppCompatActivity {

    ActivityContactUsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());
    }


}
