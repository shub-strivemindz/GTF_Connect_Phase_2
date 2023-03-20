package com.gtfconnect.ui.screenUI.authModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityOtpVerificationBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.viewModels.AuthViewModel;

import java.util.HashMap;
import java.util.Map;


public class OtpVerificationScreen extends AppCompatActivity implements ApiResponseListener{

    ActivityOtpVerificationBinding binding;
    private boolean isTextHidden = false;

    private AuthViewModel authViewModel;
    private ApiResponseListener listener;
    private Rest rest;

    private String emailID = "";

    private boolean timerFlag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        emailID = PreferenceConnector.readString(this,PreferenceConnector.REGISTRATION_ONE_TIME_EMAIL,"");

        setCountdownTimer();

        binding.timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerFlag)
                {
                    timerFlag = false;
                    authViewModel.resendOTP(emailID);
                    setCountdownTimer();
                }

            }
        });

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


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> verifyOTPData = new HashMap<>();
                verifyOTPData.put("email",emailID);
                verifyOTPData.put("otp",binding.otp.getText().toString().trim());

                authViewModel.VerifyOTP(verifyOTPData);
            }
        });

        // Show/Hide password
        binding.otpVisibilityToggleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTextHidden) {
                    binding.otpVisibilityToggleSwitch.setImageResource(R.drawable.invisible);
                    binding.otp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isTextHidden = false;
                }
                else {
                    binding.otpVisibilityToggleSwitch.setImageResource(R.drawable.visible);
                    binding.otp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isTextHidden = true;
                }
            }
        });

    }


    private void showDialog()
    {
        BottomSheetDialog registered_success_dialog = new BottomSheetDialog(OtpVerificationScreen.this);
        registered_success_dialog.setContentView(R.layout.bottomsheet_register_successful);

        MaterialCardView login_button = registered_success_dialog.findViewById(R.id.login_button);

        login_button.setOnClickListener(view1 -> {
            startActivity(new Intent(OtpVerificationScreen.this,LoginScreen.class));
            finishAffinity();
        });
        registered_success_dialog.show();
    }


    private void setCountdownTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.timer.setText("00 : " + millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }

            public void onFinish() {
                binding.timer.setText("Resend OTP");
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
        //Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        //Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
        showDialog();
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
