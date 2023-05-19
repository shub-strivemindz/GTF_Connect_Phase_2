package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityGroupChannelMemberProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.ReportReasonListener;
import com.gtfconnect.models.commonGroupChannelResponseModels.MemberMediaResponseModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.MemberReportReasonResponseModel;
import com.gtfconnect.ui.adapters.commonGroupChannelAdapters.GroupChannelMemberMediaAdapter;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.GridItemDecoration;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MemberProfileScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityGroupChannelMemberProfileBinding binding;

    private final int GET_MEMBER_DETAILS = 1;

    private final int BLOCK_USER = 2;

    private final int GET_REPORT_REASON_LIST = 3;

    private final int REPORT_USER = 4;

    private int requestType ;

    private Rest rest;

    private ConnectViewModel connectViewModel;

    private AuthViewModel authViewModel;

    private ApiResponseListener listener;


    private MemberMediaResponseModel groupChannelMediaResponseModel;

    private GroupChannelMemberMediaAdapter mediaAdapter;

    private String api_token;

    private int channel_id;

    private int memberID;
    //private boolean isDataResponseLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroupChannelMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        api_token = PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,"");
        channel_id = Integer.parseInt(PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,""));
        memberID = Integer.parseInt(PreferenceConnector.readString(MemberProfileScreen.this,PreferenceConnector.GC_MEMBER_ID,""));

        init();

        groupChannelMediaResponseModel = new MemberMediaResponseModel();


        binding.backClick.setOnClickListener(v -> onBackPressed());

        binding.blockUserContainer.setOnClickListener(view -> {
            Dialog block_user_dialog = new Dialog(this);

            block_user_dialog.setContentView(R.layout.dialog_block_user);
            block_user_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            block_user_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView block = block_user_dialog.findViewById(R.id.block);
            TextView cancel = block_user_dialog.findViewById(R.id.cancel);

            block.setOnClickListener(view1 -> {


                Map<String,Object> params = new HashMap<>();
                params.put("Status","Blocked");
                params.put("GCMemberID",memberID);

                requestType = BLOCK_USER;
                connectViewModel.block_user(channel_id,api_token,params);

                block_user_dialog.dismiss();
            });
            cancel.setOnClickListener(view1 ->  block_user_dialog.dismiss());

            block_user_dialog.show();
        });



        binding.reportUser.setOnClickListener(view -> {

           requestType = GET_REPORT_REASON_LIST;
           connectViewModel.group_channel_report_reason_list(api_token,1,200);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        binding.mediaRecycler.setVisibility(View.GONE);
                        binding.documentRecycler.setVisibility(View.VISIBLE);

                        mediaAdapter = new GroupChannelMemberMediaAdapter(MemberProfileScreen.this,1,groupChannelMediaResponseModel);
                        binding.documentRecycler.setHasFixedSize(true);
                        binding.documentRecycler.setLayoutManager(new LinearLayoutManager(MemberProfileScreen.this));
                        binding.documentRecycler.setAdapter(mediaAdapter);
                        break;

                    case 1:

                        binding.mediaRecycler.setVisibility(View.VISIBLE);
                        binding.documentRecycler.setVisibility(View.GONE);

                        int spanCount = 3; // 3 columns
                        int spacing = 10; // 50px
                        boolean includeEdge = true;

                        mediaAdapter = new GroupChannelMemberMediaAdapter(MemberProfileScreen.this,3,groupChannelMediaResponseModel);
                        binding.mediaRecycler.setHasFixedSize(true);
                        binding.mediaRecycler.setLayoutManager(new GridLayoutManager(MemberProfileScreen.this,3));
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

        initializeConnectViewModel();
        initializeAuthViewModel();


        String gc_member_id = getIntent().getStringExtra("gc_member_id");

        requestType = GET_MEMBER_DETAILS;
        connectViewModel.get_group_channel_member_media(channel_id,api_token,gc_member_id);
    }






    private void initializeConnectViewModel(){
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
    }



    private void initializeAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }
            }
        });
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
        Type type;

        if (requestType == GET_MEMBER_DETAILS) {
            type = new TypeToken<MemberMediaResponseModel>() {
            }.getType();

            groupChannelMediaResponseModel = new MemberMediaResponseModel();

            groupChannelMediaResponseModel = gson.fromJson(jsonObject, type);

            if (groupChannelMediaResponseModel != null && groupChannelMediaResponseModel.getData() != null) {

                binding.mediaRecycler.setVisibility(View.GONE);
                binding.documentRecycler.setVisibility(View.VISIBLE);

                mediaAdapter = new GroupChannelMemberMediaAdapter(MemberProfileScreen.this, 1, groupChannelMediaResponseModel);
                binding.documentRecycler.setHasFixedSize(true);
                binding.documentRecycler.setLayoutManager(new LinearLayoutManager(MemberProfileScreen.this));
                binding.documentRecycler.setAdapter(mediaAdapter);


                if (groupChannelMediaResponseModel.getData().getUserInfo() != null) {

                    String username = "";
                    if (groupChannelMediaResponseModel.getData().getUserInfo().getFirstname() != null) {
                        username = groupChannelMediaResponseModel.getData().getUserInfo().getFirstname();
                    }
                    if (groupChannelMediaResponseModel.getData().getUserInfo().getLastname() != null) {
                        username += " " + groupChannelMediaResponseModel.getData().getUserInfo().getLastname();
                    }
                    binding.username.setText(username);

                    if (groupChannelMediaResponseModel.getData().getUserInfo().getProfileImage() != null) {
                        GlideUtils.loadImage(this, binding.logo, groupChannelMediaResponseModel.getData().getUserInfo().getProfileImage());
                    }

                }
            }
        } else if (requestType == GET_REPORT_REASON_LIST) {

            type = new TypeToken<MemberReportReasonResponseModel>(){}.getType();

            MemberReportReasonResponseModel reasonResponseModel = gson.fromJson(jsonObject,type);

           if (reasonResponseModel != null && reasonResponseModel.getData() != null && reasonResponseModel.getData().getList() != null){
               Utils.ShowReportReasonList(this, reasonResponseModel.getData().getList(), new ReportReasonListener() {
                   @Override
                   public void SelectReason(int reasonID, String reasonText, String reasonCode) {


                       Map<String, Object> params = new HashMap<>();

                       params.put("ReportReasonID",reasonID);
                       params.put("GCMemberID",memberID);
                       params.put("GroupChannelID",channel_id);
                       params.put("UserReportID","");

                       Log.d("report_params",params.toString());

                       requestType = REPORT_USER;
                       authViewModel.report_user(api_token,"android","test",params);

                       //Toast.makeText(MemberProfileScreen.this, "Reason selected = "+reasonText, Toast.LENGTH_SHORT).show();
                   }
               });
           }
        } else if (requestType == REPORT_USER) {
            Toast.makeText(MemberProfileScreen.this, "Reported user", Toast.LENGTH_SHORT).show();
        } else if (requestType == BLOCK_USER) {
            Toast.makeText(MemberProfileScreen.this, "Blocked user", Toast.LENGTH_SHORT).show();
        }
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
