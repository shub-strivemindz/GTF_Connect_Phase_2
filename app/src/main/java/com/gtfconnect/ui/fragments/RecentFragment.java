package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.FragmentRecentViewBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.DashboardMessageCountListener;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferResponseModel;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardResponseModel;
import com.gtfconnect.ui.adapters.ExclusiveOfferAdapter;
import com.gtfconnect.ui.adapters.dashboardAdapters.RecentViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ConnectViewModel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;

public class RecentFragment extends Fragment implements ApiResponseListener {

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

    private RecentViewAdapter recentViewAdapter;

    private ConnectViewModel connectViewModel;

    private ApiResponseListener listener;

    private String profileImageBaseUrl = "";

    private DashboardMessageCountListener dashboardMessageCountListener;

    private boolean isExclusiveDataUpdated;

    //private IntentFilter filter1;


    /*public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (action.equals("send_exclusive")) {

                Log.d("chat","I am in BroadCastReceiver");
                //boolean msg = intent.getBooleanExtra("value",false);


            }
        }
    };*/



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dashboardMessageCountListener = (DashboardMessageCountListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecentViewBinding.inflate(inflater, container, false);
        rest = new Rest(getContext(),false,false);

        /*filter1 = new IntentFilter();
        filter1.addAction("send_exclusive");
        Objects.requireNonNull(requireActivity()).registerReceiver(mReceiver, filter1);*/


        init();

        binding.searchView.setVisibility(View.GONE);


        /*binding.searchIcon.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.VISIBLE);
            binding.container.setVisibility(View.GONE);
            isSearchClicked = true;
        });

        binding.searchClose.setOnClickListener(view -> {
            binding.searchView.setVisibility(View.GONE);
            binding.container.setVisibility(View.VISIBLE);
            isSearchClicked = false;
        });*/


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

        boolean isExclusiveRefreshed = PreferenceConnector.readBoolean(getContext(),PreferenceConnector.IS_EXCLUSIVE_REFRESHED,false);


        if (!isExclusiveRefreshed){

            rest = new Rest(getContext(),false,false);
            listener = this;

            connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
            connectViewModel.getResponseLiveData().observe(requireActivity(), new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {

                    Log.d("Profile Listener Called ---", "onChanged: " + new Gson().toJson(apiResponse));
                    if (apiResponse != null) {

                        //listener.putResponse(apiResponse, auth_rest);
                        listener.putResponse(apiResponse, rest);
                    }

                }
            });

            connectViewModel.get_exclusive_offers(PreferenceConnector.readString(getContext(),PreferenceConnector.API_GTF_TOKEN_,""),"",1);
        }
    }


    private void loadLocalData()
    {
        exclusiveOfferDataModels = new ArrayList<>();

        String response = PreferenceConnector.readString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/exclusive","");
        Type type = new TypeToken<ExclusiveOfferResponseModel>(){}.getType();

        ExclusiveOfferResponseModel exclusiveOfferResponseModel = new Gson().fromJson(response,type);

        if (exclusiveOfferResponseModel != null && exclusiveOfferResponseModel.getData() != null && exclusiveOfferResponseModel.getData().getList() != null && !exclusiveOfferResponseModel.getData().getList().isEmpty()) {
            binding.exclusiveTitle.setText("Exclusive");
            binding.recentContainer.setVisibility(View.VISIBLE);
            exclusiveOfferDataModels.addAll(exclusiveOfferResponseModel.getData().getList());
        }
        else{
            binding.recentContainer.setVisibility(View.GONE);
            binding.exclusiveTitle.setText("Recent");
        }

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

            if (responseModel.getData().getGcImageBasePath() != null) {
                profileImageBaseUrl = responseModel.getData().getGcImageBasePath();
            }



            recentViewAdapter.updateList(gcList,profileImageBaseUrl);
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

                                    if (isAdded()) {
                                        PreferenceConnector.writeString(getContext(), PreferenceConnector.DASHBOARD_DATA + "/" + "recent", response);

                                        int notificationCount = 0;
                                        for (int i=0;i<responseModel.getData().getGcData().size();i++){
                                            if (responseModel.getData().getGcData().get(i).getUnreadcount() != null){
                                                int count = Integer.parseInt(responseModel.getData().getGcData().get(i).getUnreadcount());
                                                notificationCount+=count;
                                            }
                                        }

                                        PreferenceConnector.writeInteger(getContext(),"recent/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,notificationCount);
                                        dashboardMessageCountListener.getMessageRecentCount(notificationCount);
                                    }

                                    gcList = new ArrayList<>();
                                    gcList.addAll(responseModel.getData().getGcData());


                                    // Todo


                                    if (responseModel.getData().getGcImageBasePath() != null) {
                                        profileImageBaseUrl = responseModel.getData().getGcImageBasePath();
                                    }

                                    recentViewAdapter.updateList(gcList,profileImageBaseUrl);
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
        recentViewAdapter = new RecentViewAdapter(getActivity(),gcList,profileImageBaseUrl);
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
            //Objects.requireNonNull(requireActivity()).unregisterReceiver(mReceiver);
        //socketInstance.off("messageReceived");
    }


  /*  public void exclusiveDataUpdate(boolean isExclusiveDataUpdated){
        this.isExclusiveDataUpdated = isExclusiveDataUpdated;
    }*/






    @Override
    public void onLoading() {
        // rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);

    }

    @Override
    public void onAuthFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForbidden(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLaunchFailure(JsonObject jsonObject) {
        Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtherFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



    private void renderResponse(JsonObject jsonObject){
        Log.d("exclusive_response",jsonObject.toString());


        Gson gson = new Gson();
        Type type = new TypeToken<ExclusiveOfferResponseModel>(){}.getType();

        ExclusiveOfferResponseModel exclusiveOfferResponseModel = gson.fromJson(jsonObject,type);

        if (exclusiveOfferResponseModel != null && exclusiveOfferResponseModel.getData() != null && exclusiveOfferResponseModel.getData().getList() != null && !exclusiveOfferResponseModel.getData().getList().isEmpty()) {
            String response = new Gson().toJson(exclusiveOfferResponseModel);
            PreferenceConnector.writeString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/exclusive",response);

            PreferenceConnector.writeBoolean(getContext(),PreferenceConnector.IS_EXCLUSIVE_REFRESHED,true);
            exclusiveOfferAdapter.updateOfferList(exclusiveOfferResponseModel.getData().getList());

            binding.recentContainer.setVisibility(View.VISIBLE);
            binding.exclusiveTitle.setText("Exclusive");

                   /* Bundle bundle = new Bundle();
                    bundle.putBoolean("value", true);

                    RecentFragment recentFragment = new RecentFragment();
                    recentFragment.setArguments(bundle);*/
        }
        else{
            binding.recentContainer.setVisibility(View.GONE);
            binding.exclusiveTitle.setText("Recent");
        }
    }
}

