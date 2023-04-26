package com.gtfconnect.ui.screenUI.channelModule;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityManagePermissionBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.util.HashMap;
import java.util.Map;

public class ChannelManagePermissionScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityManagePermissionBinding binding;

    InfoDbEntity detailModel;

    private final int UPDATE_DATA = 201;

    private final int GC_REFRESH_UPDATED_DATA_CODE = 1001;

    private int requestType = 0;

    private Rest rest;
    private ApiResponseListener listener;
    private ConnectViewModel connectViewModel;

    private int channelID;

    private String api_token;
    private int slowModeTime = 0,
            sendMessage = 0,
            sendMedia = 0,
            sendSticker = 0,
            embedLinks = 0,
            pinMessage = 0,
            sharingContent = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagePermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        loadLocalData();

        init();
        setData();
        setSwitchListeners();


        binding.backClick.setOnClickListener(view -> onBackPressed());



        binding.slowModeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedPosition = binding.slowModeRadioGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioSlowMode=(AppCompatRadioButton) findViewById(selectedPosition);

            int index = binding.slowModeRadioGroup.indexOfChild(radioSlowMode);
            setSlowModeBackground(index);
        });


        binding.updatePermission.setOnClickListener(view -> {
            Map<String,Object> params = new HashMap<>();

            params.put("SendMessage",sendMessage);
            params.put("SendMedia",sendMedia);
            params.put("SendStickerGIF",sendSticker);
            params.put("EmbedLinks",embedLinks);
            params.put("PinMessage",pinMessage);
            params.put("SlowMode",slowModeTime);

            requestType = UPDATE_DATA;
            connectViewModel.update_groupChannel_permission_settings(channelID,api_token,"android","test",params);
        });
    }



    private void setSwitchListeners()
    {
        binding.sendMessageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                sendMessage = 1;
            }
            else{
                sendMessage = 0;
            }
        });

        binding.sendMediaSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                sendMedia = 1;
            }
            else{
                sendMedia = 0;
            }
        });

        binding.sendStickerGifSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                sendSticker = 1;
            }
            else{
                sendSticker = 0;
            }
        });

        binding.sendEmbedLinkSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                embedLinks = 1;
            }
            else{
                embedLinks = 0;
            }
        });

        binding.pinMessageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                pinMessage = 1;
            }
            else{
                pinMessage = 0;
            }
        });

        binding.sharingContentSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Map<String,Object> params = new HashMap<>();
            if (b){
                params.put("RestrictSharingContent",1);
                Toast.makeText(this, String.valueOf(channelID), Toast.LENGTH_SHORT).show();
                connectViewModel.update_groupChannel_settings(channelID,api_token,"android","test",params);
            }
            else{
                params.put("RestrictSharingContent",0);
                connectViewModel.update_groupChannel_settings(channelID,api_token,"android","test",params);
            }
        });

        binding.slowModeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                slowModeTime = 10;
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);

                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));


            }
            else{
                slowModeTime = 0;
                binding.slowModeOptionContainer.setVisibility(View.GONE);
            }
        });
    }






    private void setSlowModeBackground(int position)
    {
        switch (position){
            case 1:
                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                slowModeTime = 10;
                break;
            case 3:

                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                slowModeTime = 30;
                break;
            case 5:

                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                slowModeTime = 1;
                break;
            case 7:

                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                slowModeTime = 5;
                break;
            case 9:

                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.radioButtonDefaultColor)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.white));

                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                slowModeTime = 24;
                break;
        }
    }


    private void setData() {
        if (detailModel.getGcPermission() != null) {
            if (detailModel.getGcPermission().getSendMedia() == 1){
                binding.sendMediaSwitch.setChecked(true);
                sendMedia = 1;
            }
            else{
                binding.sendMediaSwitch.setChecked(false);
                sendMedia = 0;
            }


            if (detailModel.getGcPermission().getEmbedLinks() == 1){
                binding.sendEmbedLinkSwitch.setChecked(true);
                embedLinks = 1;
            }
            else{
                binding.sendEmbedLinkSwitch.setChecked(false);
                embedLinks = 0;
            }

            if (detailModel.getGcPermission().getPinMessage() == 1){
                binding.pinMessageSwitch.setChecked(true);
                pinMessage = 1;
            }
            else{
                binding.pinMessageSwitch.setChecked(false);
                pinMessage = 0;
            }

            if (detailModel.getGcPermission().getSendStickerGIF() == 1){
                binding.sendStickerGifSwitch.setChecked(true);
                sendSticker = 1;
            }
            else{
                binding.sendStickerGifSwitch.setChecked(false);
                sendSticker = 0;
            }

            if (detailModel.getGcPermission().getSendMessage() == 1){
                binding.sendMessageSwitch.setChecked(true);
                sendMessage = 1;
            }
            else{
                binding.sendMessageSwitch.setChecked(false);
                sendMessage = 0;
            }

            if (detailModel.getGcSetting()!=null){
                if (detailModel.getGcSetting().getRestrictSharingContent() == 1){
                    binding.sharingContentSwitch.setChecked(true);
                    sharingContent = 1;
                }
                else{
                    binding.sharingContentSwitch.setChecked(false);
                    sharingContent = 0;
                }
            }

            if (detailModel.getGcPermission().getSlowMode() == 10){
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);
                binding.slowModeSwitch.setChecked(true);
                binding.gap10.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap10.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap10.setChecked(true);
                slowModeTime = 10;
            }
            else if (detailModel.getGcPermission().getSlowMode() == 30){
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);
                binding.slowModeSwitch.setChecked(true);
                binding.gap30.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap30.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap30.setChecked(true);
                slowModeTime = 30;
            }
            else if (detailModel.getGcPermission().getSlowMode() == 1){
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);
                binding.slowModeSwitch.setChecked(true);
                binding.gap1.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap1.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap1.setChecked(true);
                slowModeTime = 1;
            }
            else if (detailModel.getGcPermission().getSlowMode() == 5){
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);
                binding.slowModeSwitch.setChecked(true);
                binding.gap5.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap5.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap5.setChecked(true);
                slowModeTime = 5;
            }
            else if (detailModel.getGcPermission().getSlowMode() == 24){
                binding.slowModeOptionContainer.setVisibility(View.VISIBLE);
                binding.slowModeSwitch.setChecked(true);
                binding.gap24.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(ChannelManagePermissionScreen.this, R.color.theme_green)));
                binding.gap24.setBackgroundColor(ContextCompat.getColor(ChannelManagePermissionScreen.this,R.color.radioButtonSelectedBackgroundColor));

                binding.gap24.setChecked(true);
                slowModeTime = 24;
            }
            else{
                binding.slowModeOptionContainer.setVisibility(View.GONE);
                binding.slowModeSwitch.setChecked(false);
            }
        }
    }



    private void loadLocalData(){

        detailModel = new InfoDbEntity();
        Gson gson = new Gson();
        String data = getIntent().getStringExtra("data");
        detailModel = gson.fromJson(data, InfoDbEntity.class);
    }



    private void init() {

        rest = new Rest(this,false,false);

        rest = new Rest(this,true,false);
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
    }


    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {


        if (requestType == UPDATE_DATA) {
            Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();

            setResult(GC_REFRESH_UPDATED_DATA_CODE, new Intent());
            finish();
        }
        Log.d("json response : ",jsonObject.toString());
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {

        if (requestType == UPDATE_DATA) {
            Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();

            setResult(GC_REFRESH_UPDATED_DATA_CODE, new Intent());
            finish();
        }
        Log.d("json response : ",jsonObject.toString());

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, LoginScreen.class));
        finishAffinity();
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
}
