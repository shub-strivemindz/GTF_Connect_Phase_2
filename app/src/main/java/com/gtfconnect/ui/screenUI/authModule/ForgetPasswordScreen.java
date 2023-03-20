package com.gtfconnect.ui.screenUI.authModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityForgetPasswordBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;


public class ForgetPasswordScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityForgetPasswordBinding binding;

    private ApiResponseListener listener;
    private Rest rest;
    private AuthViewModel authViewModel;
    private boolean timerFlag = false;

    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Getting API data fetch
        rest = new Rest(this,true,false);
        listener = this;
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("asasdasdsadswww", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    listener.putResponse(apiResponse, rest);

                }

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.sendResetLink.setOnClickListener(view -> {
                    email = binding.oldEmail.getText().toString().trim();

                    if (!Utils.checkEmail_validation(email)) {
                        Utils.showSnackMessage(this, binding.oldEmail, "Please enter valid email");
                        binding.oldEmail.requestFocus();
                        //startActivity(new Intent(ForgetPasswordScreen.this, LoginScreen.class));
                    }
                    else
                    {
                        authViewModel.forgetPassword(email);
                    }
                }
        );


        binding.timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerFlag)
                {
                    timerFlag = false;
                    authViewModel.forgetPassword(email);
                    setCountdownTimer();
                }

            }
        });
    }

    private void setCountdownTimer()
    {
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    binding.timer.setText("00 : " + millisUntilFinished / 1000);
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    binding.timer.setText("Resend Link");
                    timerFlag = true;
                }

            }.start();
    }

    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        setCountdownTimer();
        binding.resetLinkButtonText.setText("Link Sent Successfully");
        binding.sendResetLink.setClickable(false);
        binding.sendResetLink.setEnabled(false);
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        setCountdownTimer();
        binding.resetLinkButtonText.setText("Link Sent Successfully");
        binding.sendResetLink.setClickable(false);
        binding.sendResetLink.setEnabled(false);
    }

    @Override
    public void onAuthFailure(String message) {

    }

    @Override
    public void onServerFailure(String message) {

    }

    @Override
    public void onForbidden(String message) {

    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {

    }

    @Override
    public void onOtherFailure(String message) {

    }
}
