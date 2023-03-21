package com.gtfconnect.ui.screenUI.groupModule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.databinding.ActivityPreviewMediaBinding;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.ui.adapters.ImagePreviewAdapter;
import com.gtfconnect.utilities.SnapOneItemView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MultiPreviewImage extends AppCompatActivity {

    ActivityPreviewMediaBinding binding;

    ArrayList<GroupChatResponseModel.Medium> mediaList;

    String postBaseUrl = "";

    String title_name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPreviewMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaList = new ArrayList<>();


        Intent intent = getIntent();
        postBaseUrl = intent.getStringExtra("base_url");
        title_name = intent.getStringExtra("title");


        Gson gson = new Gson();
        Type type = new TypeToken<List<GroupChatResponseModel.Medium>>(){}.getType();
        String mediaObject = intent.getStringExtra("mediaList");
        mediaList = gson.fromJson(mediaObject, type);


        LinearSnapHelper linearSnapHelper = new SnapOneItemView();
        linearSnapHelper.attachToRecyclerView(binding.previewRecycler);


        ImagePreviewAdapter imagePreviewAdapter = new ImagePreviewAdapter(this,mediaList,postBaseUrl);
        binding.previewRecycler.setHasFixedSize(true);
        binding.previewRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.previewRecycler.setAdapter(imagePreviewAdapter);


        binding.previewTitle.setText(title_name);

        binding.backClick.setOnClickListener(view -> onBackPressed());
    }
}
