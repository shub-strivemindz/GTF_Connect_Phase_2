package com.gtfconnect.ui.screenUI.userProfileModule;

import android.content.Intent;
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
import com.gtfconnect.databinding.ActivitySavedMessageBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.SavedMessageListener;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.models.savedMessageModels.SavedMessageResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.adapters.userProfileAdapter.SavedMessageAdapter;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.LoginScreen;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;

public class SavedMessagesScreen extends AppCompatActivity implements ApiResponseListener , SavedMessageListener {

    ActivitySavedMessageBinding binding;

    private int requestType;


    private Rest rest;

    private ConnectViewModel connectViewModel;

    private ApiResponseListener listener;

    private DatabaseViewModel databaseViewModel;

    private int userID;

    private String api_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userID = PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0);
        api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");

        init();


        // Navigate to Previous Screen :
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SavedMessagesScreen.this, UserProfileScreen.class));
                finish();
            }
        });
    }


    private void init() {

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);




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



        requestType = Constants.GET_SAVED_MESSAGE;
        connectViewModel.get_saved_messages(api_token,20,1);
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


    private void renderResponse(JsonObject jsonObject) {

        if (requestType == Constants.GET_SAVED_MESSAGE) {

            Gson gson = new Gson();
            Type type = new TypeToken<SavedMessageResponseModel>() {
            }.getType();


            SavedMessageResponseModel savedMessageResponseModel = gson.fromJson(jsonObject, type);


            if (savedMessageResponseModel != null && savedMessageResponseModel.getData() != null && savedMessageResponseModel.getData().getList() != null && !savedMessageResponseModel.getData().getList().isEmpty()) {

                // load saved messages  ------
                SavedMessageAdapter savedMessageViewAdapter = new SavedMessageAdapter(SavedMessagesScreen.this, savedMessageResponseModel.getData().getList(),userID+"",this);
                binding.savedMessageRecycler.setHasFixedSize(true);
                binding.savedMessageRecycler.setLayoutManager(new LinearLayoutManager(SavedMessagesScreen.this));
                binding.savedMessageRecycler.setAdapter(savedMessageViewAdapter);

            }
        } else if (requestType == Constants.DELETE_SAVED_MESSAGE) {
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

            requestType = Constants.GET_SAVED_MESSAGE;
            connectViewModel.get_saved_messages(api_token,20,1);
        }
    }

    @Override
    public void deleteSavedPost(int messageID) {
        requestType = Constants.DELETE_SAVED_MESSAGE;
        connectViewModel.delete_saved_message(messageID,api_token);
    }
}
