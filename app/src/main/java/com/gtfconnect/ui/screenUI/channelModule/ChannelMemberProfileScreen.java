package com.gtfconnect.ui.screenUI.channelModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.impl.model.Preference;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityGroupChannelMemberProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.groupChannelModels.GroupChannelMediaResponseModel;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.DocumentAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.LinkAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.MediaAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter.SettingAdapter;
import com.gtfconnect.ui.adapters.commonGroupChannelAdapters.GroupChannelMemberMediaAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.GridItemDecoration;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.Objects;

public class ChannelMemberProfileScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityGroupChannelMemberProfileBinding binding;


    private Rest rest;

    private ConnectViewModel connectViewModel;

    private ApiResponseListener listener;


    private GroupChannelMediaResponseModel groupChannelMediaResponseModel;

    private GroupChannelMemberMediaAdapter mediaAdapter;

    //private boolean isDataResponseLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroupChannelMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        groupChannelMediaResponseModel = new GroupChannelMediaResponseModel();


        binding.backClick.setOnClickListener(v -> onBackPressed());

        binding.blockUserContainer.setOnClickListener(view -> {
            Dialog block_user_dialog = new Dialog(this);

            block_user_dialog.setContentView(R.layout.dialog_block_user);
            block_user_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            block_user_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView block = block_user_dialog.findViewById(R.id.block);
            TextView cancel = block_user_dialog.findViewById(R.id.cancel);

            block.setOnClickListener(view1 -> block_user_dialog.dismiss());
            cancel.setOnClickListener(view1 ->  block_user_dialog.dismiss());

            block_user_dialog.show();
        });


        binding.reportUser.setOnClickListener(view -> {
            BottomSheetDialog report_user_bottomSheet = new BottomSheetDialog(this);
            report_user_bottomSheet.setContentView(R.layout.bottomsheet_report_user_options);

            MaterialCardView report_user =  report_user_bottomSheet.findViewById(R.id.report_user);
            ImageView back = report_user_bottomSheet.findViewById(R.id.back);

            report_user.setOnClickListener(view1 -> report_user_bottomSheet.dismiss());
            back.setOnClickListener(view1 -> report_user_bottomSheet.dismiss());


            report_user_bottomSheet.show();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        binding.mediaRecycler.setVisibility(View.GONE);
                        binding.documentRecycler.setVisibility(View.VISIBLE);

                        mediaAdapter = new GroupChannelMemberMediaAdapter(ChannelMemberProfileScreen.this,1,groupChannelMediaResponseModel);
                        binding.documentRecycler.setHasFixedSize(true);
                        binding.documentRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
                        binding.documentRecycler.setAdapter(mediaAdapter);
                        break;

                    case 1:

                        binding.mediaRecycler.setVisibility(View.VISIBLE);
                        binding.documentRecycler.setVisibility(View.GONE);

                        int spanCount = 3; // 3 columns
                        int spacing = 10; // 50px
                        boolean includeEdge = true;

                        mediaAdapter = new GroupChannelMemberMediaAdapter(ChannelMemberProfileScreen.this,3,groupChannelMediaResponseModel);
                        binding.mediaRecycler.setHasFixedSize(true);
                        binding.mediaRecycler.setLayoutManager(new GridLayoutManager(ChannelMemberProfileScreen.this,3));
                        binding.mediaRecycler.setAdapter(mediaAdapter);

                        binding.mediaRecycler.addItemDecoration(new GridItemDecoration(spanCount, spacing, includeEdge));
                        break;
/*
                    case 2:
                        MediaAdapter mediaViewAdapter = new MediaAdapter(4);
                        binding.profileRecycler.setHasFixedSize(true);
                        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
                        binding.profileRecycler.setAdapter(mediaViewAdapter);
                        break;*/
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }




    private void init(){

        rest = new Rest(this,false,false);
        listener = this;

        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });


        String gc_member_id = getIntent().getStringExtra("gc_member_id");
        int channel_id = Integer.parseInt(PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,""));
        String api_token = PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,"");

        connectViewModel.get_group_channel_member_media(channel_id,api_token,"android","test",gc_member_id);
    }


    @Override
    public void onLoading() {
        //isDataResponseLoaded = false;
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);

        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        /*startActivity(new Intent(this, LoginScreen.class));
        finishAffinity();*/
    }

    @Override
    public void onServerFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForbidden(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtherFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    private void renderResponse(JsonObject jsonObject){

        Gson gson = new Gson();
        Type type = new TypeToken<GroupChannelMediaResponseModel>(){}.getType();

        groupChannelMediaResponseModel = new GroupChannelMediaResponseModel();

        groupChannelMediaResponseModel = gson.fromJson(jsonObject,type);

        binding.mediaRecycler.setVisibility(View.GONE);
        binding.documentRecycler.setVisibility(View.VISIBLE);

        mediaAdapter = new GroupChannelMemberMediaAdapter(ChannelMemberProfileScreen.this,1,groupChannelMediaResponseModel);
        binding.documentRecycler.setHasFixedSize(true);
        binding.documentRecycler.setLayoutManager(new LinearLayoutManager(ChannelMemberProfileScreen.this));
        binding.documentRecycler.setAdapter(mediaAdapter);

    }



    /*private void recyclerAnimator()
    {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.loading_item);
        LottieAnimationView animationView = dialog.findViewById(R.id.loading_animation);
        animationView.setAnimation(R.raw.round_loader);
        animationView.playAnimation();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(window).getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
    }*/
}
