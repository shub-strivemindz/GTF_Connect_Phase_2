package com.gtfconnect.ui.screenUI.channelModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityManageReactionsBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelChatAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ManageReactionsListAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;

public class ChannelManageReactionScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityManageReactionsBinding binding;

    ChannelManageReactionModel reactionModel;
    private Rest rest;
    private ApiResponseListener listener;
    private ConnectViewModel connectViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageReactionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();

        binding.reactionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.reactionSwitchText.setText("On");
                }
                else {
                    binding.reactionSwitchText.setText("Off");
                }
            }
        });
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



        int channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));
        String api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");
        connectViewModel.get_group_channel_manage_reaction_list(channelID,api_token,"android","test",1,25);
    }



    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
       /* Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();
        setResult(GC_PERMISSION_UPDATED_CODE,new Intent());
        finish();
        Log.d("json response : ",jsonObject.toString());*/
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        /*Toast.makeText(this, "Permissions Updated", Toast.LENGTH_SHORT).show();
        setResult(GC_PERMISSION_UPDATED_CODE,new Intent());
        finish();
        Log.d("json response : ",jsonObject.toString());*/

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


    private void renderResponse(JsonObject jsonObject){

        reactionModel = new ChannelManageReactionModel();
        Gson gson = new Gson();
        Type type = new TypeToken<ChannelManageReactionModel>(){}.getType();

        reactionModel = gson.fromJson(jsonObject,type);


        ManageReactionsListAdapter adapter = new ManageReactionsListAdapter(this,reactionModel);
        binding.reactionsRecycler.setHasFixedSize(true);
        binding.reactionsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.reactionsRecycler.setAdapter(adapter);
    }
}
