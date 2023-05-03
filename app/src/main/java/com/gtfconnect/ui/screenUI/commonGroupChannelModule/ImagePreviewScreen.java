package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtfconnect.databinding.ActivityImagePreviewBinding;
import com.gtfconnect.utilities.Constants;

public class ImagePreviewScreen extends AppCompatActivity {


    ActivityImagePreviewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Uri uri = Uri.parse(getIntent().getStringExtra("profile"));
        if (uri !=null) {
            binding.preview.setImageURI(uri);
        }
        else{
            setResult(Constants.NO_IMAGE_FOUND,new Intent());
            finish();
        }

        binding.close.setOnClickListener(v -> finish());

        binding.download.setOnClickListener(v -> {

        });
    }
}
