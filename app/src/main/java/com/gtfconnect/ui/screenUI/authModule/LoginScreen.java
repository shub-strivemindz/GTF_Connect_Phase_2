package com.gtfconnect.ui.screenUI.authModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
//import com.gtfconnect.database.AppDatabase;
import com.gtfconnect.databinding.ActivityLoginBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.models.authResponseModels.LoginResponseModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.ui.screenUI.HomeScreen;
import com.gtfconnect.ui.screenUI.authModule.registerModule.RegisterScreen1;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class LoginScreen extends AppCompatActivity implements ApiResponseListener{

    private ActivityLoginBinding binding;
    private boolean isTextHidden = true;

    private final int LOGIN_AUTH = 1;
    private final int RESEND_OTP = 2;

    private static boolean exitDoublePressed = false;

    private int type_fetcher = 0;

    private AuthViewModel authViewModel;
    private ApiResponseListener listener;
    private Rest rest;

    private LoginResponseModel loginResponse = new LoginResponseModel();

    private DatabaseViewModel databaseViewModel;

    //private AppDao appDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.errorDialog.setVisibility(View.GONE);

        clearDatabase();



        // Getting API data fetch
        //auth_rest = new Rest(this);            // for authentication ------------
        rest = new Rest(this,true,false);                              // for other apis ----------------
        listener = this;
        //appDao = AppDatabase.getInstance(getApplication()).appDao();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse,rest);
                }

            }
        });


        // Navigate for Register Credentials
        binding.registerNow.setOnClickListener(view -> startActivity(new Intent(LoginScreen.this, RegisterScreen1.class)));

        // Navigate for Forget Password
        binding.forgetPassword.setOnClickListener(view -> startActivity(new Intent(LoginScreen.this, ForgetPasswordScreen.class)));

        // Navigate for Resend OTP
        binding.resendOtp.setOnClickListener(view -> {

            type_fetcher = RESEND_OTP;
            validateResendOtp();

        });

        // Navigate for Home View
        binding.login.setOnClickListener(view -> validationLoginCheck());


        // Show/Hide password
        binding.passwordToggleSwitch.setOnClickListener(view -> {
            if (isTextHidden) {
                binding.passwordToggleSwitch.setImageResource(R.drawable.invisible);
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isTextHidden = false;
            }
            else {
                binding.passwordToggleSwitch.setImageResource(R.drawable.visible);
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isTextHidden = true;
            }
        });
    }




/*
    private void checkThemeAndSetBoxBorders()
    {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //process

                binding.usernameContainer.setStrokeColor(getResources().getColor(R.color.authEditTextBg));
                binding.passwordContainer.setStrokeColor(getResources().getColor(R.color.authEditTextBg));
                binding.registerNow.setStrokeColor(getResources().getColor(R.color.authEditTextBg));

                binding.usernameContainer.invalidate();
                binding.passwordContainer.invalidate();
                binding.registerNow.invalidate();

                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // process



                binding.usernameContainer.setStrokeColor(getResources().getColor(R.color.theme_green));
                binding.passwordContainer.setStrokeColor(getResources().getColor(R.color.theme_green));
                binding.registerNow.setStrokeColor(getResources().getColor(R.color.theme_green));

                binding.usernameContainer.invalidate();
                binding.passwordContainer.invalidate();
                binding.registerNow.invalidate();

                break;
        }
    }*/







    private void clearDatabase(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseViewModel = new ViewModelProvider(LoginScreen.this).get(DatabaseViewModel.class);
                databaseViewModel.delete_database();

                PreferenceConnector.cleanPreferences(LoginScreen.this);
            }
        }).start();
    }


    private void validationLoginCheck()
    {
        binding.errorDialog.setVisibility(View.GONE);

        String get_email = binding.emailId.getText().toString().trim();
        String get_password = binding.password.getText().toString().trim();
        if (get_email.isEmpty()) {

            Utils.showSnackMessage(this, binding.emailId, "please enter email..");
            binding.emailId.requestFocus();
        } else if (get_password.isEmpty()) {
            Utils.showSnackMessage(this, binding.password, "please enter password");
            binding.password.requestFocus();
        }
        else {


            Map<String, Object> login_data = new HashMap<>();
            login_data.put("Email", get_email);
            login_data.put("Password", get_password);

            //Todo: Needs to be Changed as default set for 0 for testing purpose
            login_data.put("is_gtf_connect", 1);

            Log.d("Login params ----",login_data.toString());

            type_fetcher = LOGIN_AUTH;
            //startActivity(new Intent(LoginScreen.this, HomeScreen.class));
            authViewModel.Login("android","test_token",login_data);

        }
    }

    private void validateResendOtp()
    {
        String get_email = binding.emailId.getText().toString().trim();
        if (get_email.isEmpty()) {

            Utils.showSnackMessage(this, binding.emailId, "please enter email..");
            binding.emailId.requestFocus();
        }
        else {
            authViewModel.resendOTP(get_email);
        }
    }

    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {

        //PreferenceConnector.writeString(this,PreferenceConnector.API_TOKEN_CONNECT_0,"Bearer "+jsonObject.getAsJsonObject("data").get("api_token").getAsString());
        //Toast.makeText(this, jsonObject.getAsJsonObject("data").get("api_token").getAsString(), Toast.LENGTH_SHORT).show();
        renderSuccessResponse(jsonObject);
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderSuccessResponse(jsonObject);
    }

    @Override
    public void onAuthFailure(String message) {

        rest.dismissProgressdialog();
    }


    @Override
    public void onServerFailure(String message) {
        binding.errorDialog.setVisibility(View.VISIBLE);
        Log.d("Render Error :",message);
        rest.dismissProgressdialog();
    }

    @Override
    public void onForbidden(String message) {
        rest.dismissProgressdialog();
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        rest.dismissProgressdialog();
    }

    @Override
    public void onOtherFailure(String message) {
        rest.dismissProgressdialog();
    }


    private void renderSuccessResponse(JsonObject data) {

        if (type_fetcher == 1) {

            Gson gson = new Gson();
            Type type = new TypeToken<LoginResponseModel>() {
            }.getType();

            Log.d("LOGIN RESPONSE ",data.toString());

            loginResponse = gson.fromJson(data, type);
/*
            // Todo : Can be removed after App Dao Database has been setup --------------------------------------------------------------------

            Gson loadData = new Gson();
            String json = loadData.toJson(loginResponse);*/


            PreferenceConnector.writeInteger(this,PreferenceConnector.GTF_USER_ID,loginResponse.getData().getGTFUserID());
            PreferenceConnector.writeInteger(this,PreferenceConnector.CONNECT_USER_ID,loginResponse.getData().getUserID());


            //String apt_token_0 = "Bearer "+

            PreferenceConnector.writeString(this,PreferenceConnector.FIRST_NAME,loginResponse.getData().getFirstname());
            PreferenceConnector.writeString(this,PreferenceConnector.LAST_NAME,loginResponse.getData().getLastname());
            PreferenceConnector.writeString(this,PreferenceConnector.EMAIL_ID,loginResponse.getData().getEmail());

            PreferenceConnector.writeString(this,PreferenceConnector.API_GTF_TOKEN_,"Bearer "+loginResponse.getData().getApiToken());
            PreferenceConnector.writeString(this,PreferenceConnector.API_CONNECT_TOKEN,"Bearer "+loginResponse.getData().getApiTokenGtf());

            /*Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //appDao.delete();
                    //appDao.insert(profileData);

                }
            });*/
            startActivity(new Intent(LoginScreen.this, HomeScreen.class));
        }
        else if (type_fetcher == 2)
        {
            startActivity(new Intent(LoginScreen.this, OtpVerificationScreen.class));
        }
        finishAffinity();
    }

    @Override
    public void onBackPressed() {



        if (exitDoublePressed) {
            rest.dismissProgressdialog();
            super.onBackPressed();
            return;
        }

        exitDoublePressed = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> exitDoublePressed=false, 2000);
    }
}
