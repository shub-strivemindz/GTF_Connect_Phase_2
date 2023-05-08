package com.gtfconnect.ui.screenUI.authModule.registerModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityLocationSelectorBinding;
import com.gtfconnect.models.authResponseModels.CityResponse;
import com.gtfconnect.models.authResponseModels.CountryData;
import com.gtfconnect.models.authResponseModels.CountryResponse;
import com.gtfconnect.models.authResponseModels.LocationPickerModel;
import com.gtfconnect.models.authResponseModels.StateResponse;
import com.gtfconnect.ui.adapters.authModuleAdapter.LocationPickerAdapter;
import com.gtfconnect.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class LocationPickerScreen extends AppCompatActivity {

    ActivityLocationSelectorBinding binding;

    List<LocationPickerModel> locationPickerModelList;

    private boolean isSearchFocused = false;

    private  LocationPickerAdapter locationPickerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationSelectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String locationType = getIntent().getStringExtra("locationType");
        getLocationData(locationType);





        binding.title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    binding.closeIcon.setImageResource(R.drawable.close);
                    isSearchFocused = true;
                }
                else {
                    binding.closeIcon.setImageResource(R.drawable.search);
                    isSearchFocused = false;
                }

            }
        });




        binding.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<LocationPickerModel> temp = new ArrayList();
                for (LocationPickerModel locationPicker : locationPickerModelList) {
                    if (locationPicker.getPlace().toLowerCase().contains(charSequence.toString())) {
                        temp.add(locationPicker);
                    }
                }
                if (temp.size() == 0) {
                    //text_no_data_found.setVisibility(View.VISIBLE);
                } else {
                    //text_no_data_found.setVisibility(View.GONE);
                }
                locationPickerAdapter.updateList(temp);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });








        binding.close.setOnClickListener(v -> {
            LocationPickerModel pickerModel = locationPickerAdapter.getLocation();

            if (pickerModel != null){

                Intent intent = new Intent();
                intent.putExtra("id",pickerModel.getId());
                intent.putExtra("place",pickerModel.getPlace());

                if (locationType.equalsIgnoreCase("country")) {
                    setResult(Constants.GET_COUNTRY, intent);
                    finish();
                } else if (locationType.equalsIgnoreCase("state")) {
                    setResult(Constants.GET_STATE, intent);
                    finish();
                } else if (locationType.equalsIgnoreCase("city")) {
                    setResult(Constants.GET_CITY, intent);
                    finish();
                }
            }
        });
    }



    private void getLocationData(String locationType){

        locationPickerModelList = new ArrayList<>();
        String response = getIntent().getStringExtra("response");

        if (locationType.equalsIgnoreCase("country")){

            CountryResponse countryResponse = new Gson().fromJson(response, CountryResponse.class);

            for (int i=0;i<countryResponse.getData().size();i++){

                LocationPickerModel data= new LocationPickerModel();
                data.setId(countryResponse.getData().get(i).getCountryID());
                data.setPlace(countryResponse.getData().get(i).getCountryName());
                data.setPhoneCode(countryResponse.getData().get(i).getPhoneCode());

                locationPickerModelList.add(data);
            }

            loadDataToAdapter();
        }
        else if (locationType.equalsIgnoreCase("state")) {

            StateResponse stateResponse = new Gson().fromJson(response, StateResponse.class);

            for (int i=0;i<stateResponse.getData().size();i++){

                LocationPickerModel data= new LocationPickerModel();
                data.setId(stateResponse.getData().get(i).getStateID());
                data.setPlace(stateResponse.getData().get(i).getStateName());
                data.setPhoneCode(0);

                locationPickerModelList.add(data);
            }

            loadDataToAdapter();

        }
        else if (locationType.equalsIgnoreCase("city")){


            CityResponse cityResponse = new Gson().fromJson(response, CityResponse.class);

            for (int i=0;i<cityResponse.getData().size();i++){

                LocationPickerModel data= new LocationPickerModel();
                data.setId(cityResponse.getData().get(i).getCityID());
                data.setPlace(cityResponse.getData().get(i).getCityName());
                data.setPhoneCode(0);

                locationPickerModelList.add(data);
            }

            loadDataToAdapter();
        }
    }



    private void loadDataToAdapter(){

        locationPickerAdapter = new LocationPickerAdapter(this,locationPickerModelList);
        binding.placeRecycler.setHasFixedSize(true);
        binding.placeRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.placeRecycler.setAdapter(locationPickerAdapter);
    }



}
