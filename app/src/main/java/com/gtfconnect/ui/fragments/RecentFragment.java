package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.FragmentGroupViewBinding;
import com.gtfconnect.databinding.FragmentRecentViewBinding;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferResponseModel;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardResponseModel;
import com.gtfconnect.ui.adapters.ExclusiveOfferAdapter;
import com.gtfconnect.ui.adapters.GroupViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.Ack;

public class RecentFragment extends Fragment {

    private FragmentRecentViewBinding binding;

    private DashboardResponseModel responseModel;

    private List<DashboardListEntity> gcList;

    private JSONObject jsonRawObject;
    private int userId;

    Rest rest;

    private boolean isSearchClicked = false;
    private AppDao appDao;

    private DatabaseViewModel databaseViewModel;

    private int localDBDataSize = 0;


    private List<ExclusiveOfferDataModel> exclusiveOfferDataModels;


    private ExclusiveOfferAdapter exclusiveOfferAdapter;

    private GroupViewAdapter recentViewAdapter;

    private boolean isExclusiveDataUpdated;

    private IntentFilter filter1;


    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (action.equals("send_exclusive")) {

                Log.i("chat","I am in BroadCastReceiver");
                boolean msg = intent.getBooleanExtra("value",false);


            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecentViewBinding.inflate(inflater, container, false);
        rest = new Rest(getContext(),false,false);

        filter1 = new IntentFilter();
        filter1.addAction("send_exclusive");
        getContext().registerReceiver(mReceiver, filter1);

        init();

        binding.searchView.setVisibility(View.GONE);


        binding.searchIcon.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.VISIBLE);
            binding.container.setVisibility(View.GONE);
            isSearchClicked = true;
        });

        binding.searchClose.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.GONE);
            binding.container.setVisibility(View.VISIBLE);
            isSearchClicked = false;
        });


        gcList = new ArrayList<>();
        loadDataToAdapter();


        // Todo : un comment when implemented all 4 tabs socket
        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));


        messageReceived();

        return binding.getRoot();
    }



    private void init()
    {
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
    }


    private void loadLocalData()
    {
        exclusiveOfferDataModels = new ArrayList<>();

        String response = PreferenceConnector.readString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/exclusive","");
        Type type = new TypeToken<ExclusiveOfferResponseModel>(){}.getType();

        ExclusiveOfferResponseModel exclusiveOfferResponseModel = new Gson().fromJson(response,type);

        /*exclusiveOfferDataModels.addAll(exclusiveOfferResponseModel.getData().getList());*/

        exclusiveOfferAdapter = new ExclusiveOfferAdapter(getActivity(),exclusiveOfferDataModels);
        binding.exclusiveViewList.setHasFixedSize(true);
        binding.exclusiveViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.exclusiveViewList.setAdapter(exclusiveOfferAdapter);








        String response2 = PreferenceConnector.readString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/recent","");
        Type type2 = new TypeToken<DashboardResponseModel>(){}.getType();
        responseModel = new Gson().fromJson(response2,type2);


        if (responseModel != null && responseModel.getData() != null && responseModel.getData().getGcData() != null && !responseModel.getData().getGcData().isEmpty()){
            gcList = new ArrayList<>();
            gcList.addAll(responseModel.getData().getGcData());

            recentViewAdapter.updateList(gcList);
        }
        updateGroupDashboardSocket();
    }



    private void updateGroupDashboardSocket()
    {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<DashboardResponseModel>() {
        }.getType();

        responseModel = new DashboardResponseModel();


        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("userId", userId);
            jsonRawObject.put("filter","");

            Log.d("AUTH USER CREDENTIALS -----",jsonRawObject.toString());

            //getActivity().runOnUiThread(() -> rest.ShowDialogue());
            socketInstance.emit("auth-user", jsonRawObject, (Ack) args -> {

                //getActivity().runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Data ----", responseData.toString());

                try {
                    if (responseData.getString("success").equalsIgnoreCase("false")) {
                        getActivity().runOnUiThread(() -> {
                            // rest.dismissProgressdialog();
                            Toast.makeText(getContext(), "Socket Connection Refused", Toast.LENGTH_SHORT).show();
                        });
                    }
                    else {

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                        responseModel = gson.fromJson(gsonObject, type);


                        if (responseData == null) {
                            Utils.showSnackMessage(getContext(), binding.getRoot(), "No Data Found");
                            Log.d("authenticateUserAndFetchData -- ", "Error");
                        } else {
                            Log.d("authenticateUserAndFetchData -- ", String.valueOf(responseData));

                            getActivity().runOnUiThread(() -> {
                                if (responseModel.getData() != null && responseModel.getData().getGcData() != null) {

                                    String response = new Gson().toJson(responseModel);
                                    PreferenceConnector.writeString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/"+"recent",response);

                                    gcList = new ArrayList<>();
                                    gcList.addAll(responseModel.getData().getGcData());

                                    recentViewAdapter.updateList(gcList);
                                }
                            });
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("Socket : ","Group Chat Exception ---"+e.toString());
                }
            });
        }
        catch (Exception e){
            Log.d("JsonException ---",e.toString());
        }
    }

    private void loadDataToAdapter()
    {
        recentViewAdapter = new GroupViewAdapter(getActivity(),gcList);
        binding.recentViewList.setHasFixedSize(true);
        binding.recentViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recentViewList.setAdapter(recentViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();

    }


    private void messageReceived() {

        /*socketInstance.on("messageReceived", args -> {
            Log.d("Message_Received_on","Home Fragment");
            //updateGroupDashboardSocket();
        });*/
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(mReceiver);
        //socketInstance.off("messageReceived");
    }


  /*  public void exclusiveDataUpdate(boolean isExclusiveDataUpdated){
        this.isExclusiveDataUpdated = isExclusiveDataUpdated;
    }*/
}

