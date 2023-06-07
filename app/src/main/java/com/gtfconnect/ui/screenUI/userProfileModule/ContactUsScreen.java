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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityContactUsBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.ContactUsModel;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;

public class ContactUsScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityContactUsBinding binding;

    private Rest rest;

    private ApiResponseListener listener;

    private AuthViewModel authViewModel;

    private ContactUsModel contactUsModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();


        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());
    }


    private void init(){

        rest = new Rest(this,false,false);
        listener = this;

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

        authViewModel.getContactUs();
    }




    private void initializeData(){

        if (contactUsModel != null && contactUsModel.getData() != null){
            if (contactUsModel.getData().getContactAddress() != null){
                binding.contactUsAddress.setText(contactUsModel.getData().getContactAddress());
            }
            if (contactUsModel.getData().getContactNumber() != null){
                binding.contactUsNumber.setText(contactUsModel.getData().getContactNumber());
            }
            if (contactUsModel.getData().getContactFacebook() != null){
                binding.contactUsFacebook.setText(contactUsModel.getData().getContactFacebook());
            }
            if (contactUsModel.getData().getContactInsta() != null){
                binding.contactUsInstagram.setText(contactUsModel.getData().getContactInsta());
            }
        }
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


    private void renderResponse(JsonObject jsonObject) {

        Gson gson = new Gson();
        Type type = new TypeToken<ContactUsModel>() {
        }.getType();;

        contactUsModel = new ContactUsModel();

        contactUsModel = gson.fromJson(jsonObject,type);

        initializeData();
    }
}