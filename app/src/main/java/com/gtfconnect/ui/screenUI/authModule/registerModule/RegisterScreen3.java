package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.controller.RestAdapter;
import com.gtfconnect.databinding.ActivityRegister3Binding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.SelectCityListener;
import com.gtfconnect.interfaces.SelectCountryListener;
import com.gtfconnect.interfaces.SelectStateListener;
import com.gtfconnect.models.CityResponse;
import com.gtfconnect.models.CountryData;
import com.gtfconnect.models.CountryResponse;
import com.gtfconnect.models.StateResponse;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.ui.adapters.authModuleAdapter.CountryListAdapter;
import com.gtfconnect.ui.screenUI.authModule.OtpVerificationScreen;
import com.gtfconnect.ui.screenUI.userProfileModule.UserProfileScreen;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterScreen3 extends AppCompatActivity implements ApiResponseListener{

    ActivityRegister3Binding binding;
    static boolean isConditionAccepted = true;
    private boolean isTextHidden = true;
    private boolean isTextHidden2 = true;


    private final int COUNTRY_FETCHER = 1;
    private final int STATE_FETCHER = 2;
    private final int CITY_FETCHER = 3;

    private final int REGISTRATION_LISTENER = 4;

    private int typeFetcher = 0;
    private int countryCode,stateCode,cityCode ;

    private ApiResponseListener listener;
    private Rest rest;
    private AuthViewModel authViewModel;

    private CountryResponse countryResponse = new CountryResponse();
    private StateResponse stateResponse = new StateResponse();
    private CityResponse cityResponse = new CityResponse();

    Map<String, Object> registrationData = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegister3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registrationData= (Map<String, Object>) getIntent().getSerializableExtra("MapData");
        registrationData.put("find_us", "telegram");

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

        binding.chooseCountry.setOnClickListener(view -> {

            typeFetcher = COUNTRY_FETCHER;
            authViewModel.getCountryDataList();
        });


        binding.chooseState.setOnClickListener(view -> {
            if (countryResponse == null || countryResponse.getData().isEmpty() ) {
                Utils.showSnackMessage(this,binding.chooseCountry,"Please select country ");
            }
            else {
                typeFetcher = STATE_FETCHER;
                authViewModel.getStateList(countryCode);
            }
        });


        binding.chooseCity.setOnClickListener(view -> {
            if (stateResponse == null || stateResponse.getData().isEmpty() ) {
                Utils.showSnackMessage(this,binding.chooseState,"Please select state ");
            }
            else {
                typeFetcher = CITY_FETCHER;
                authViewModel.getCityList(stateCode);
            }
        });



        // Navigate for OTP Verification

        binding.register.setOnClickListener(view -> {
            validationCheck();
            typeFetcher = REGISTRATION_LISTENER;
                }
        );

        binding.back.setOnClickListener(view -> {
            //startActivity(new Intent(RegisterScreen3.this, RegisterScreen2.class));
            onBackPressed();
        });

        // Acceptance of T&C
        binding.acceptConditions.setOnClickListener(view -> {
            if (isConditionAccepted) {
                binding.acceptConditions.setImageResource(R.drawable.checked_checkbox);
                isConditionAccepted = false;
            }
            else {
                binding.acceptConditions.setImageResource(R.drawable.unchecked_checkbox);
                isConditionAccepted = true;
            }
        });



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


        // Show/Hide confirm password
        binding.confirmPasswordToggleSwitch.setOnClickListener(view -> {
            if (isTextHidden2) {
                binding.confirmPasswordToggleSwitch.setImageResource(R.drawable.invisible);
                binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isTextHidden2 = false;
            }
            else {
                binding.confirmPasswordToggleSwitch.setImageResource(R.drawable.visible);
                binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isTextHidden2 = true;
            }
        });
    }

    @Override
    public void onLoading() {
        rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderListResponse(jsonObject);
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderListResponse(jsonObject);
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


    private void renderListResponse(JsonObject data) {
        Gson gson = new Gson();
        Type type;
        switch (typeFetcher)
        {
            case COUNTRY_FETCHER:

                type = new TypeToken<CountryResponse>() {
                }.getType();

                countryResponse = gson.fromJson(data, type);
                Utils.Show_Select_Country_list(this, countryResponse.getData(), new SelectCountryListener() {
                    @Override
                    public void Select_value(int id, String name,int phoneCode) {
                        // Set the value to Text Box.....
                        countryCode = id;
                        binding.country.setText(name);
                    }
                });
                break;
            case STATE_FETCHER:

                type = new TypeToken<StateResponse>() {
                }.getType();

                stateResponse = gson.fromJson(data, type);
                Utils.Show_Select_State_list(this, stateResponse.getData(), new SelectStateListener() {
                    @Override
                    public void Select_value(int id,String name) {
                        stateCode = id;
                        binding.state.setText(name);
                    }
                });
                break;

            case CITY_FETCHER:

                type = new TypeToken<CityResponse>() {
                }.getType();

                cityResponse = gson.fromJson(data, type);
                Utils.Show_Select_City_list(this, cityResponse.getData(), new SelectCityListener() {
                    @Override
                    public void Select_value(int id, String name) {
                        binding.city.setText(name);
                        cityCode = id;
                    }
                });
                break;

            case REGISTRATION_LISTENER:
                Toast.makeText(this, data.getAsJsonObject("data").get("Otp").getAsString(), Toast.LENGTH_SHORT).show();
                Log.d("OTP Verification -----",data.getAsJsonObject("data").get("Otp").getAsString());
                startActivity(new Intent(RegisterScreen3.this,OtpVerificationScreen.class));
                finishAffinity();
        }
    }



    private void validationCheck() {
        String address = binding.address.getText().toString().trim();
        String pincode = binding.pincode.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();

        if (address.isEmpty()) {

            Utils.showSnackMessage(this, binding.address, "please enter valid address!");
            binding.address.requestFocus();
        } else if (pincode.length() != 6) {
            Utils.showSnackMessage(this, binding.pincode, "please enter valid pincode!");
            binding.pincode.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            Log.d("asdasdsfasdgafgafg",String.valueOf(password.length()));
            Utils.showSnackMessage(this, binding.password, "Please enter valid password!");
            binding.password.requestFocus();
        } else if (!(confirmPassword.equals(password))) {
            Utils.showSnackMessage(this, binding.confirmPassword, "Password Mismatch!");
            binding.confirmPassword.requestFocus();
        } else {


            registrationData.put("Address", address);
            registrationData.put("Pincode", pincode);
            registrationData.put("Password", password);
            registrationData.put("CountryID", countryCode);
            registrationData.put("StateID", stateCode);
            registrationData.put("CityID", cityCode);


            authViewModel.Registration("test_token",registrationData);
        }
    }
}
