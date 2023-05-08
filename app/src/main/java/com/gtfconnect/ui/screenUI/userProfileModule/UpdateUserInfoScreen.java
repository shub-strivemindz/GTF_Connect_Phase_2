package com.gtfconnect.ui.screenUI.userProfileModule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityUpdateProfileBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.SelectCityListener;
import com.gtfconnect.interfaces.SelectCountryListener;
import com.gtfconnect.interfaces.SelectStateListener;
import com.gtfconnect.models.authResponseModels.CityResponse;
import com.gtfconnect.models.authResponseModels.CountryResponse;
import com.gtfconnect.models.ProfileResponseModel;
import com.gtfconnect.models.authResponseModels.StateResponse;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.AuthViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserInfoScreen extends AppCompatActivity implements ApiResponseListener {

    ActivityUpdateProfileBinding binding;

    private final int COUNTRY_FETCHER = 1;
    private final int STATE_FETCHER = 2;
    private final int CITY_FETCHER = 3;


    private final int UPDATE_PROFILE = 4;

    private final int GET_PROFILE_DATA = 5;


    private int requestType = 0;

    private ConnectViewModel connectViewModel;

    private ProfileResponseModel profileResponseModel;

    private Rest rest;
    private ApiResponseListener listener;
    private AuthViewModel authViewModel;



    private int countryCode,stateCode,cityCode ;
    private CountryResponse countryResponse = new CountryResponse();
    private StateResponse stateResponse = new StateResponse();
    private CityResponse cityResponse = new CityResponse();


    String firstName,lastName,gender,email,dob,number,postalCode;


    MaterialStyledDatePickerDialog.OnDateSetListener date_picker;
    String date_of_birth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();




        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());

        binding.genderSelection.setOnClickListener(view -> {
            BottomSheetDialog gender_selection_dialog = new BottomSheetDialog(UpdateUserInfoScreen.this);
            gender_selection_dialog.setContentView(R.layout.bottomsheet_choose_gender);
            gender_selection_dialog.show();
        });



        binding.chooseCountry.setOnClickListener(view -> {

            requestType = COUNTRY_FETCHER;
            authViewModel.getCountryDataList();
        });


        binding.chooseState.setOnClickListener(view -> {
            if (countryResponse == null || countryResponse.getData().isEmpty() ) {
                Utils.showSnackMessage(this,binding.chooseCountry,"Please select country ");
            }
            else {
                requestType = STATE_FETCHER;
                authViewModel.getStateList(countryCode);
            }
        });


        binding.chooseCity.setOnClickListener(view -> {
            if (stateResponse == null || stateResponse.getData().isEmpty() ) {
                Utils.showSnackMessage(this,binding.chooseState,"Please select state ");
            }
            else {
                requestType = CITY_FETCHER;
                authViewModel.getCityList(stateCode);
            }
        });


        binding.genderSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog gender_selection_dialog = new BottomSheetDialog(UpdateUserInfoScreen.this);
                gender_selection_dialog.setContentView(R.layout.bottomsheet_choose_gender);

                RadioGroup genderGroup = gender_selection_dialog.findViewById(R.id.gender_radio_group);

                gender_selection_dialog.show();

                genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int selectedPosition = genderGroup.getCheckedRadioButtonId();
                        RadioButton radioSexButton=(RadioButton) gender_selection_dialog.findViewById(selectedPosition);

                        gender = radioSexButton.getText().toString().trim();

                        binding.gender.setText(gender);
                        switch (gender) {
                            case "Female":
                                binding.genderIcon.setImageResource(R.drawable.female);
                                break;
                            case "Others":
                                binding.genderIcon.setImageResource(R.drawable.others);
                                break;
                            case "Male":
                            default:
                                binding.genderIcon.setImageResource(R.drawable.male);
                        }
                        gender_selection_dialog.dismiss();
                    }
                });

            }
        });



        binding.openDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, date_picker, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                datePickerDialog.show();
            }
        });
        date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (month<10) {
                    if (dayOfMonth<10) {
                        date_of_birth = year + "-0" + month + "-0" + dayOfMonth;
                    }
                    else {
                        date_of_birth = year + "-0" + month + "-" + dayOfMonth;
                    }
                }
                else {
                    if (dayOfMonth<10) {
                        date_of_birth = year + "-" + month + "-0" + dayOfMonth;
                    }
                    else {
                        date_of_birth = year + "-" + month + "-" + dayOfMonth;
                    }
                }
                binding.dobTitle.setText(date_of_birth);
            }
        };


        binding.updateProfile.setOnClickListener(view -> validationCheck());
    }


    private void init() {

        Utils.registerInternetReceiver(this);


        // Getting API data fetch
        //auth_rest = new Rest(this);            // for authentication ------------

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

        requestType = GET_PROFILE_DATA;
        connectViewModel.getUserProfile(PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));

    }



    private void prefillUserProfileData(ProfileResponseModel model)
    {

        firstName = model.getData().getProfileInfo().getFirstname();
        lastName = model.getData().getProfileInfo().getLastname();
        gender = model.getData().getProfileInfo().getGender();
        email = model.getData().getProfileInfo().getEmail();
        dob = model.getData().getProfileInfo().getDob();
        number = model.getData().getProfileInfo().getPhone();
        postalCode = String.valueOf(model.getData().getProfileInfo().getPincode());


        binding.firstName.setHint(firstName);
        binding.lastName.setHint(lastName);
        binding.gender.setHint(gender);
        binding.email.setHint(email);
        binding.dobTitle.setHint(dob);
        binding.number.setHint(number);


        Log.d("Country_ID",  model.toString());
        countryCode = model.getData().getProfileInfo().getCountryList().getCountryID();
        stateCode = model.getData().getProfileInfo().getStateList().getStateID();
        cityCode = model.getData().getProfileInfo().getCityList().getCityID();

        date_of_birth = model.getData().getProfileInfo().getDob();

//        binding.address.setHint(model.getData().getProfileInfo().getAddress());
        binding.country.setHint(model.getData().getProfileInfo().getCountryList().getName());
        binding.state.setHint(model.getData().getProfileInfo().getStateList().getName());
        binding.city.setHint(model.getData().getProfileInfo().getCityList().getName());

        binding.pincode.setHint(postalCode);
        binding.postalCode.setCountryForPhoneCode(Integer.parseInt(model.getData().getProfileInfo().getPhoneCode()));

    }

    private void validationCheck()
    {

        if (binding.firstName.getText().toString().trim() != null && !binding.firstName.getText().toString().trim().isEmpty())
        {
            firstName = binding.firstName.getText().toString().trim();
        }
        if (binding.lastName.getText().toString().trim() != null && !binding.lastName.getText().toString().trim().isEmpty()) {
            lastName = binding.lastName.getText().toString().trim();
        }
        if (binding.email.getText().toString().trim() != null && !binding.email.getText().toString().trim().isEmpty()) {
            email = binding.email.getText().toString().trim();
        }
        if (binding.number.getText().toString().trim() != null && !binding.number.getText().toString().trim().isEmpty()) {
            number = binding.number.getText().toString().trim();
        }
        if (binding.pincode.getText().toString().trim() != null && !binding.pincode.getText().toString().trim().isEmpty()) {
            postalCode =  binding.pincode.getText().toString().trim();
        }

            Map<String, Object> params = new HashMap<>();
            params.put("UserID",String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.GTF_USER_ID,0)));
            params.put("Email",email);
            params.put("Gender",gender);
            params.put("FirstName",firstName);
            params.put("LastName",lastName);
            params.put("Phone",number);
            params.put("CountryID",countryCode);
            params.put("StateID",stateCode);
            params.put("CityID",cityCode);
            params.put("Pincode",postalCode);
            params.put("DOB",date_of_birth);


            requestType = UPDATE_PROFILE;
            authViewModel.updateUserProfile(PreferenceConnector.readString(this,PreferenceConnector.API_CONNECT_TOKEN,""),"android","test_token","1",params);

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
        Log.d("Update Profile ",message);
    }

    @Override
    public void onServerFailure(String message) {
        Log.d("Update Profile ",message);
    }

    @Override
    public void onForbidden(String message) {
        Log.d("Update Profile ",message);
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Log.d("Update Profile ",jsonObject.toString());
    }

    @Override
    public void onOtherFailure(String message) {
        Log.d("Update Profile ",message);
    }


    private void renderResponse(JsonObject data) {
        Gson gson = new Gson();
        Type type;
        switch (requestType) {
            case COUNTRY_FETCHER:

                type = new TypeToken<CountryResponse>() {
                }.getType();

                countryResponse = gson.fromJson(data, type);
                Utils.Show_Select_Country_list(this, countryResponse.getData(), new SelectCountryListener() {
                    @Override
                    public void Select_value(int id, String name, int phoneCode) {
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
                    public void Select_value(int id, String name) {
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
            case UPDATE_PROFILE:

              /*  type = new TypeToken<ProfileResponseModel>() {
                }.getType();

                ProfileResponseModel model = gson.fromJson(data, type);*/


                PreferenceConnector.writeString(this,PreferenceConnector.FIRST_NAME,firstName);
                PreferenceConnector.writeString(this,PreferenceConnector.LAST_NAME,lastName);
                PreferenceConnector.writeString(this,PreferenceConnector.EMAIL_ID,email);

                Log.d("Update Profile response",data.toString());

                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();

                break;
            case GET_PROFILE_DATA:

                type = new TypeToken<ProfileResponseModel>() {
                }.getType();


                profileResponseModel = gson.fromJson(data, type);
                prefillUserProfileData(profileResponseModel);

                Log.d("PROFILE ","JSON RESPONSE "+data.toString());
                Log.d("PROFILE ","MODEL RESPONSE "+profileResponseModel.getData().getProfileInfo().getAddress());

                Gson loadData = new Gson();
                String json = loadData.toJson(profileResponseModel);

                PreferenceConnector.writeString(this,PreferenceConnector.KEY_IS_USERINFO,json);



                rest = new Rest(this,true,false);

                //listener = this;
                authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
                authViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(ApiResponse apiResponse) {

                        Log.d("Auth Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                        if (apiResponse != null) {

                            //listener.putResponse(apiResponse, auth_rest);
                            listener.putResponse(apiResponse, rest);
                        }

                    }
                });
                break;
        }
    }
}
