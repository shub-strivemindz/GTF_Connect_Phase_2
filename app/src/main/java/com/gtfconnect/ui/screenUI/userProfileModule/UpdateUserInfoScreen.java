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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.gtfconnect.ui.screenUI.authModule.registerModule.HowDidYouFindUs;
import com.gtfconnect.ui.screenUI.authModule.registerModule.LocationPickerScreen;
import com.gtfconnect.ui.screenUI.authModule.registerModule.RegisterScreen1;
import com.gtfconnect.ui.screenUI.authModule.registerModule.RegisterScreen3;
import com.gtfconnect.utilities.Constants;
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


    String firstName,lastName,gender,email,dob,number,address,postalCode, gst_number,billing_name,additional_info,how_did_find_us,how_did_find_us_text;

    String dob_year,dob_month,dob_date;

    MaterialStyledDatePickerDialog.OnDateSetListener date_picker;

    private boolean is_find_us_others;
    RadioButton maleCheck,femaleCheck,otherCheck;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.genderSelection.setOnClickListener(view -> {
            BottomSheetDialog gender_selection_dialog = new BottomSheetDialog(UpdateUserInfoScreen.this);
            gender_selection_dialog.setContentView(R.layout.bottomsheet_choose_gender);

            RadioGroup genderGroup = gender_selection_dialog.findViewById(R.id.gender_radio_group);
            maleCheck = gender_selection_dialog.findViewById(R.id.male);
            femaleCheck = gender_selection_dialog.findViewById(R.id.female);
            otherCheck = gender_selection_dialog.findViewById(R.id.others);

            if (gender != null && !gender.isEmpty()){
                switch (gender)
                {
                    case "Female":
                        femaleCheck.setChecked(true);
                        break;
                    case "Others":
                        otherCheck.setChecked(true);
                        break;
                    default:
                        maleCheck.setChecked(true);
                }
            }

            gender_selection_dialog.show();

            genderGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                int selectedPosition = genderGroup.getCheckedRadioButtonId();
                RadioButton radioSexButton=(RadioButton) gender_selection_dialog.findViewById(selectedPosition);

                gender = radioSexButton.getText().toString().trim();

                binding.gender.setText(gender);
                switch (gender) {
                    case "Male":
                        binding.genderIcon.setImageResource(R.drawable.male);
                        break;
                    case "Female":
                        binding.genderIcon.setImageResource(R.drawable.female);
                        break;
                    case "Others":
                    default:
                        binding.genderIcon.setImageResource(R.drawable.others);
                }
                gender_selection_dialog.dismiss();
            });

        });

        clickEventListeners();

        init();


    }





    private void clickEventListeners(){


        // Navigate to Previous Screen :
        binding.back.setOnClickListener(view -> finish());


        binding.howFindUs.setOnClickListener(view -> startForResult.launch(new Intent(UpdateUserInfoScreen.this, HowDidYouFindUs.class)));


        binding.chooseCountry.setOnClickListener(view -> {

            requestType = COUNTRY_FETCHER;
            authViewModel.getCountryDataList();
        });


        binding.chooseState.setOnClickListener(view -> {

            if (countryResponse != null && countryResponse.getData() != null && !countryResponse.getData().isEmpty()){
                requestType = STATE_FETCHER;
                authViewModel.getStateList(countryCode);
            }
            else{
                Utils.showSnackMessage(this,binding.chooseCountry,"Please select country ");
            }
        });


        binding.chooseCity.setOnClickListener(view -> {

            if (stateResponse != null && stateResponse.getData() != null && !stateResponse.getData().isEmpty()){
                requestType = CITY_FETCHER;
                authViewModel.getCityList(stateCode);
            }
            else {
                Utils.showSnackMessage(this,binding.chooseState,"Please select state ");
            }
        });


        binding.chooseDob.setOnClickListener(view -> {

            Calendar calendar = Calendar.getInstance();

            int year,month,day;
            if (dob_year != null && !dob_year.isEmpty()) {
                year = Integer.parseInt(dob_year);
            }
            else{
                year = calendar.get(Calendar.YEAR);
            }

            if (dob_month != null && !dob_month.isEmpty()) {
                month = Integer.parseInt(dob_month);
            }
            else{
                month = calendar.get(Calendar.MONTH);
            }

            if (dob_date != null && !dob_date.isEmpty()) {
                day = Integer.parseInt(dob_date);
            }
            else{
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }



            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, date_picker, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });
        date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (month<10) {
                    if (dayOfMonth<10) {
                        dob = year + "-0" + month + "-0" + dayOfMonth;
                    }
                    else {
                        dob = year + "-0" + month + "-" + dayOfMonth;
                    }
                }
                else {
                    if (dayOfMonth<10) {
                        dob = year + "-" + month + "-0" + dayOfMonth;
                    }
                    else {
                        dob = year + "-" + month + "-" + dayOfMonth;
                    }
                }
                binding.dobTitle.setText(dob);
            }
        };


        binding.updateProfile.setOnClickListener(view -> validationCheck());
    }







    private void init() {

        Utils.registerInternetReceiver(this);

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

        if (model.getData().getProfileInfo().getFirstname() != null) {
            firstName = model.getData().getProfileInfo().getFirstname();
        }
        if (model.getData().getProfileInfo().getLastname() != null) {
            lastName = model.getData().getProfileInfo().getLastname();
        }
        if (model.getData().getProfileInfo().getGender() != null) {
            gender = model.getData().getProfileInfo().getGender();
        }
        if (model.getData().getProfileInfo().getEmail() != null) {
            email = model.getData().getProfileInfo().getEmail();
        }

        if (model.getData().getProfileInfo().getAddress() != null) {
            address = model.getData().getProfileInfo().getAddress();
        }

        if (model.getData().getProfileInfo().getDob() != null) {
            dob = model.getData().getProfileInfo().getDob();
            separateDob();
        }
        if (model.getData().getProfileInfo().getPhone() != null) {
            number = model.getData().getProfileInfo().getPhone();
        }
        if (model.getData().getProfileInfo().getGst() != null) {
            gst_number = model.getData().getProfileInfo().getGst();
        }
        if (model.getData().getProfileInfo().getAdditionalInfo() != null) {
            additional_info = model.getData().getProfileInfo().getAdditionalInfo();
        }

        if (model.getData().getProfileInfo().getPincode() != null) {
            postalCode = String.valueOf(model.getData().getProfileInfo().getPincode());
        }

        if (model.getData().getProfileInfo().getCompanyName() != null) {
            billing_name = model.getData().getProfileInfo().getCompanyName();
        }

        if (model.getData().getProfileInfo().getFindUs() != null) {
            how_did_find_us = model.getData().getProfileInfo().getFindUs();
        }

        if (model.getData().getProfileInfo().getFindUsOtherText() != null) {
            how_did_find_us_text = model.getData().getProfileInfo().getFindUsOtherText();
        }



        binding.firstName.setText(firstName);
        binding.lastName.setText(lastName);

        if (gender.equalsIgnoreCase("Male")){
            binding.genderIcon.setImageResource(R.drawable.male);
        } else if (gender.equalsIgnoreCase("Female")) {
            binding.genderIcon.setImageResource(R.drawable.female);
        }
        else{
            binding.genderIcon.setImageResource(R.drawable.others);
        }

        binding.gender.setText(gender);
        binding.email.setText(email);
        binding.dobTitle.setText(dob);
        binding.address.setText(address);
        binding.number.setText(number);
        binding.additionalInfo.setText(additional_info);
        binding.gstNo.setText(gst_number);
        binding.companyName.setText(billing_name);
        binding.howFindUsText.setText(how_did_find_us);

        if (how_did_find_us.equalsIgnoreCase("Others")) {
            if (how_did_find_us_text != null && !how_did_find_us_text.isEmpty()) {
                binding.howFindUsOther.setVisibility(View.VISIBLE);
                binding.howFindUsOtherText.setText(how_did_find_us_text);
            } else {
                binding.howFindUsOther.setVisibility(View.GONE);
            }
        }
        else{
            binding.howFindUsOther.setVisibility(View.GONE);
        }


        Log.d("Country_ID",  model.toString());
        countryCode = model.getData().getProfileInfo().getCountryList().getCountryID();
        stateCode = model.getData().getProfileInfo().getStateList().getStateID();
        cityCode = model.getData().getProfileInfo().getCityList().getCityID();

//        binding.address.setText(model.getData().getProfileInfo().getAddress());
        binding.country.setText(model.getData().getProfileInfo().getCountryList().getName());
        binding.state.setText(model.getData().getProfileInfo().getStateList().getName());
        binding.city.setText(model.getData().getProfileInfo().getCityList().getName());

        binding.pincode.setText(postalCode);
        binding.postalCode.setCountryForPhoneCode(Integer.parseInt(model.getData().getProfileInfo().getPhoneCode()));

    }

    private void validationCheck()
    {
            firstName = binding.firstName.getText().toString().trim();
            lastName = binding.lastName.getText().toString().trim();
            email = binding.email.getText().toString().trim();
            number = binding.number.getText().toString().trim();
            postalCode =  binding.pincode.getText().toString().trim();

            additional_info = binding.additionalInfo.getText().toString().trim();
            gst_number = binding.gstNo.getText().toString().trim();
            billing_name = binding.companyName.getText().toString().trim();


            if (firstName.isEmpty()){
                Utils.showSnackMessage(this,binding.firstName,"Enter valid first name!");
            } else if (lastName.isEmpty()) {
                Utils.showSnackMessage(this,binding.firstName,"Enter valid last name!");
            } else if (email.isEmpty()) {
                Utils.showSnackMessage(this,binding.firstName,"Enter valid email address!");
            } else if (postalCode.isEmpty()) {
                Utils.showSnackMessage(this,binding.firstName,"Enter valid pin code!");
            } else if (number.isEmpty()) {
                Utils.showSnackMessage(this,binding.firstName,"Enter valid contact number!");
            }
            else {

                Map<String, Object> params = new HashMap<>();
                params.put("UserID", String.valueOf(PreferenceConnector.readInteger(this, PreferenceConnector.GTF_USER_ID, 0)));
                params.put("Email", email);
                params.put("Gender", gender);
                params.put("FirstName", firstName);
                params.put("LastName", lastName);
                params.put("Phone", number);
                params.put("CountryID", countryCode);
                params.put("StateID", stateCode);
                params.put("CityID", cityCode);
                params.put("Pincode", postalCode);
                params.put("DOB", dob);


                params.put("find_us", how_did_find_us);
                if (is_find_us_others) {
                    params.put("find_us_other_text", binding.howFindUsOtherText.getText().toString().trim());
                }

                requestType = UPDATE_PROFILE;
                authViewModel.updateUserProfile(PreferenceConnector.readString(this, PreferenceConnector.API_CONNECT_TOKEN, ""), "android", "test_token", "1", params);

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


    private void renderResponse(JsonObject data) {
        Gson gson = new Gson();
        Type type;
        switch (requestType) {
            case COUNTRY_FETCHER:

                type = new TypeToken<CountryResponse>() {
                }.getType();

                countryResponse = gson.fromJson(data, type);

                if (countryResponse != null && countryResponse.getData() != null && !countryResponse.getData().isEmpty()){

                    String response = new Gson().toJson(countryResponse);

                    Intent intent = new Intent(UpdateUserInfoScreen.this, LocationPickerScreen.class);
                    intent.putExtra("response",response);
                    intent.putExtra("locationType","country");

                    startForResult.launch(intent);

                }
                else{
                    Utils.showSnackMessage(this, binding.chooseCountry,"No country found!");
                }
                break;
            case STATE_FETCHER:

                type = new TypeToken<StateResponse>() {
                }.getType();

                stateResponse = gson.fromJson(data, type);

                if (stateResponse != null && stateResponse.getData() != null && !stateResponse.getData().isEmpty()){

                    String response = new Gson().toJson(stateResponse);

                    Intent intent = new Intent(UpdateUserInfoScreen.this, LocationPickerScreen.class);
                    intent.putExtra("response",response);
                    intent.putExtra("locationType","state");

                    startForResult.launch(intent);

                }
                else{
                    Utils.showSnackMessage(this, binding.chooseState,"No state found!");
                }
                break;

            case CITY_FETCHER:

                type = new TypeToken<CityResponse>() {
                }.getType();

                cityResponse = gson.fromJson(data, type);

                if (cityResponse != null && cityResponse.getData() != null && !cityResponse.getData().isEmpty()){

                    String response = new Gson().toJson(cityResponse);

                    Intent intent = new Intent(UpdateUserInfoScreen.this, LocationPickerScreen.class);
                    intent.putExtra("response",response);
                    intent.putExtra("locationType","city");

                    startForResult.launch(intent);

                }
                else{
                    Utils.showSnackMessage(this, binding.chooseState,"No city found!");
                }
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

                if (profileResponseModel != null && profileResponseModel.getData() != null) {
                    prefillUserProfileData(profileResponseModel);
                }

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












    // -------------------------------------------------------------- Handling all update from Other Screens -----------------------------------------------------------

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Constants.GET_COUNTRY) {

                    assert result.getData() != null;
                    int id = result.getData().getIntExtra("id",0);
                    String place = result.getData().getStringExtra("place");

                    countryCode = id;
                    binding.country.setText(place);


                    binding.state.setText("");
                    binding.city.setText("");

                    stateCode = 0;
                    cityCode = 0;

                    stateResponse = new StateResponse();
                    cityResponse = new CityResponse();


                } else if (result.getResultCode() == Constants.GET_STATE) {

                    assert result.getData() != null;
                    int id = result.getData().getIntExtra("id",0);
                    String place = result.getData().getStringExtra("place");

                    stateCode = id;
                    binding.state.setText(place);

                    binding.city.setText("");

                    cityCode = 0;

                    cityResponse = new CityResponse();
                }
                else if (result.getResultCode() == Constants.GET_CITY) {

                    assert result.getData() != null;
                    int id = result.getData().getIntExtra("id",0);
                    String place = result.getData().getStringExtra("place");

                    cityCode = id;
                    binding.city.setText(place);
                }
                else if (result.getResultCode() == Constants.HOW_DID_YOU_FIND_US) {

                    if (result.getData() != null) {

                        is_find_us_others = result.getData().getBooleanExtra("isOtherSelected",false);

                        if (is_find_us_others){
                            binding.howFindUsOther.setVisibility(View.VISIBLE);
                        }
                        else{
                            binding.howFindUsOther.setVisibility(View.GONE);
                        }

                        how_did_find_us = result.getData().getStringExtra("find_text");
                        binding.howFindUsText.setText(how_did_find_us);
                    }
                }
            });



    private void separateDob(){
        if (dob != null && !dob.isEmpty()){

            String[] date_of_birth = dob.split("-");
            dob_year = date_of_birth[0];
            dob_month = date_of_birth[1];
            dob_date = date_of_birth[2];
        }
    }
}
