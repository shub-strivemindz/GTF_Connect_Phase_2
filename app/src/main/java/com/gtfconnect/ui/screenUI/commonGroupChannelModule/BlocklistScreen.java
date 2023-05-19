package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.gtfconnect.databinding.ActivityBlocklistBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.commonGroupChannelResponseModels.BlocklistResponseModel;
import com.gtfconnect.ui.adapters.userProfileAdapter.BlockListAdapter;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ChatViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;

public class BlocklistScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityBlocklistBinding binding;

    Rest rest;

    ConnectViewModel connectViewModel;

    ApiResponseListener listener;


    private int requestType ;

    private BlocklistResponseModel blocklistResponseModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlocklistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });



        binding.searchContainer.setVisibility(View.GONE);
        binding.blocklistRecycler.setVisibility(View.GONE);
        binding.noDataFound.setVisibility(View.VISIBLE);

        init();
    }




    public void init(){
        rest = new Rest(this, false, false);
        listener = this;
        //appDao = AppDatabase.getInstance(getApplication()).appDao();
        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });

        String channelID = PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,"");
        String api_token = PreferenceConnector.readString(this,PreferenceConnector.API_GTF_TOKEN_,"");

        Log.d("Channel ID ==",channelID);

        requestType = Constants.GET_GROUP_CHANNEL_BLOCKLIST;
        connectViewModel.group_channel_blocklist(Integer.parseInt(channelID),api_token);
    }






    @Override
    public void onLoading() {
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
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

        Log.d("blockist",jsonObject.toString());
        Gson gson = new Gson();

        Type type;

        if (requestType == Constants.GET_GROUP_CHANNEL_BLOCKLIST){

            blocklistResponseModel = new BlocklistResponseModel();
            type = new TypeToken<BlocklistResponseModel>(){}.getType();

            blocklistResponseModel = gson.fromJson(jsonObject,type);

            if (blocklistResponseModel != null && blocklistResponseModel.getData() != null && !blocklistResponseModel.getData().isEmpty()) {

                binding.searchContainer.setVisibility(View.VISIBLE);
                binding.blocklistRecycler.setVisibility(View.VISIBLE);
                binding.noDataFound.setVisibility(View.GONE);

                // load blocklist data  ------
                BlockListAdapter blocklistViewAdapter = new BlockListAdapter(BlocklistScreen.this, blocklistResponseModel);
                binding.blocklistRecycler.setHasFixedSize(true);
                binding.blocklistRecycler.setLayoutManager(new LinearLayoutManager(BlocklistScreen.this));
                binding.blocklistRecycler.setAdapter(blocklistViewAdapter);

                binding.blockedCount.setText(""+blocklistResponseModel.getData().size());
            }
            else{
                binding.noDataFound.setVisibility(View.VISIBLE);

                binding.searchContainer.setVisibility(View.GONE);
                binding.blocklistRecycler.setVisibility(View.GONE);

            }
        }

    }
}
