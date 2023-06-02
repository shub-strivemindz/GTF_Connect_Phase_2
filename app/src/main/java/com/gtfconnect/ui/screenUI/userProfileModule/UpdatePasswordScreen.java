package com.gtfconnect.ui.screenUI.userProfileModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityUpdatePasswordBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityUpdatePasswordBinding binding;

    Rest rest;

    AuthViewModel authViewModel;

    ApiResponseListener listener;

    private boolean isOldPasswordHidden = true;
    private boolean isNewPasswordHidden = true;
    private boolean isConfirmPasswordHidden = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();

        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());


        binding.updatePassword.setOnClickListener(view -> validationCheck());





        // Show/Hide password
        binding.oldPasswordToggleSwitch.setOnClickListener(view -> {
            if (isOldPasswordHidden) {
                binding.oldPasswordToggleSwitch.setImageResource(R.drawable.invisible);
                binding.oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isOldPasswordHidden = false;
            }
            else {
                binding.oldPasswordToggleSwitch.setImageResource(R.drawable.visible);
                binding.oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isOldPasswordHidden = true;
            }
        });


        // Show/Hide new password
        binding.newPasswordToggleSwitch.setOnClickListener(view -> {
            if (isNewPasswordHidden) {
                binding.newPasswordToggleSwitch.setImageResource(R.drawable.invisible);
                binding.newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isNewPasswordHidden = false;
            }
            else {
                binding.newPasswordToggleSwitch.setImageResource(R.drawable.visible);
                binding.newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isNewPasswordHidden = true;
            }
        });


        // Show/Hide confirm password
        binding.confirmPasswordToggleSwitch.setOnClickListener(view -> {
            if (isConfirmPasswordHidden) {
                binding.confirmPasswordToggleSwitch.setImageResource(R.drawable.invisible);
                binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isConfirmPasswordHidden = false;
            }
            else {
                binding.confirmPasswordToggleSwitch.setImageResource(R.drawable.visible);
                binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isConfirmPasswordHidden = true;
            }
        });
    }





    private void init() {

        Utils.registerInternetReceiver(this);

        rest = new Rest(this,true,false);
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
    }





    private void validationCheck(){
        String oldPassword = binding.oldPassword.getText().toString().trim();
        String newPassword = binding.newPassword.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();


        if (oldPassword.isEmpty() || oldPassword.length() < 6){
            Utils.showSnackMessage(this,binding.oldPassword,"Enter valid old password!");
        }
        else if (newPassword.isEmpty() || newPassword.length() < 6) {
            Utils.showSnackMessage(this,binding.newPassword,"Enter valid new password!");
        }
        else if (confirmPassword.isEmpty() || confirmPassword.length() < 6) {
            Utils.showSnackMessage(this,binding.confirmPassword,"Enter valid confirm password!");
        }
        else if (!confirmPassword.equals(newPassword)) {
            Utils.showSnackMessage(this,binding.oldPassword,"Enter valid password!");
        }
        else{

            int userID = PreferenceConnector.readInteger(this, PreferenceConnector.GTF_USER_ID, 0);



            String api_token = PreferenceConnector.readString(this, PreferenceConnector.API_CONNECT_TOKEN, "");


            Map<String, Object> params = new HashMap<>();

            params.put("UserID",userID);
            params.put("CurrentPassword",oldPassword);
            params.put("NewPassword",newPassword);
            params.put("NewConfirmPassword",confirmPassword);


            authViewModel.updatePassword(api_token,"","",params);
        }
    }





    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
    }

    @Override
    public void onAuthFailure(String message) {
        Utils.showSnackMessage(this,binding.getRoot(),message);
        Log.d("Update Profile ",message);
    }

    @Override
    public void onServerFailure(String message) {
        Utils.showSnackMessage(this,binding.getRoot(),message);
        Log.d("Update Profile ",message);
    }

    @Override
    public void onForbidden(String message) {
        Utils.showSnackMessage(this,binding.getRoot(),message);
        Log.d("Update Profile ",message);
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Utils.showSnackMessage(this,binding.getRoot(),jsonObject.get("message").toString());
        Log.d("Update Profile ",jsonObject.toString());
    }

    @Override
    public void onOtherFailure(String message) {
        Utils.showSnackMessage(this,binding.getRoot(),message);
        Log.d("Update Profile ",message);
    }



    private void renderResponse(JsonObject jsonObject){

        Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
    }

}
