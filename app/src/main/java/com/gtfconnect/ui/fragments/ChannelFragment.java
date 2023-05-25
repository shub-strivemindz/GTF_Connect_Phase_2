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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.databinding.FragmentChannelViewBinding;
import com.gtfconnect.interfaces.DashboardMessageCountListener;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardResponseModel;
import com.gtfconnect.ui.adapters.dashboardAdapters.ChannelViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;

public class ChannelFragment extends Fragment {

    private FragmentChannelViewBinding binding;

    private DashboardResponseModel responseModel;

    private List<DashboardListEntity> gcList;

    private JSONObject jsonRawObject;
    private int userId;


    private AppDao appDao;

    private DatabaseViewModel databaseViewModel;

    private int localDBDataSize = 0;

    ChannelViewAdapter channelViewAdapter;

    private String profileImageBaseUrl = "";



    private DashboardMessageCountListener dashboardMessageCountListener;


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
        binding = FragmentChannelViewBinding.inflate(inflater, container, false);

        init();

        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));

        messageReceived();

        responseModel = new DashboardResponseModel();

        gcList = new ArrayList<>();
        loadDataToAdapter();

        return binding.getRoot();
    }


    private void init()
    {
        appDao = AppDatabase.getInstance(requireActivity().getApplication()).appDao();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
    }


    private void loadLocalData()
    {

        String response2 = PreferenceConnector.readString(getContext(),PreferenceConnector.DASHBOARD_DATA+"/channel","");
        Type type2 = new TypeToken<DashboardResponseModel>(){}.getType();
        responseModel = new Gson().fromJson(response2,type2);


        if (responseModel != null && responseModel.getData() != null && responseModel.getData().getGcData() != null && !responseModel.getData().getGcData().isEmpty()){
            gcList = new ArrayList<>();
            gcList.addAll(responseModel.getData().getGcData());


            if (responseModel.getData().getGcImageBasePath() != null) {
                profileImageBaseUrl = responseModel.getData().getGcImageBasePath();
            }


            if (PreferenceConnector.readInteger(getContext(),"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0) > 0){
                dashboardMessageCountListener.getMessageChannelCount(PreferenceConnector.readInteger(getContext(),"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));

                Log.d("notification_count","Fragment = "+PreferenceConnector.readInteger(getContext(),"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,0));
            }

            channelViewAdapter.updateList(gcList,profileImageBaseUrl);
        }
        updateChannelDashboardSocket();
    }




    private void updateChannelDashboardSocket()
    {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<DashboardResponseModel>() {
        }.getType();

        responseModel = new DashboardResponseModel();


        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("userId", userId);
            jsonRawObject.put("filter","channel");

           // getActivity().runOnUiThread(() -> rest.ShowDialogue());

            socketInstance.emit("auth-user", jsonRawObject, (Ack) args -> {

                //getActivity().runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Channel Data ----", responseData.toString());

                try {
                    if (responseData.getString("success").equalsIgnoreCase("false")) {
                        getActivity().runOnUiThread(() -> {

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
                                if (responseModel != null && responseModel.getData() != null && responseModel.getData().getGcData() != null) {

                                    String response = new Gson().toJson(responseModel);
                                    if (isAdded()){
                                        PreferenceConnector.writeString(requireActivity(),PreferenceConnector.DASHBOARD_DATA+"/"+"channel",response);

                                        int notificationCount = 0;
                                        for (int i=0;i<responseModel.getData().getGcData().size();i++){
                                            if (responseModel.getData().getGcData().get(i).getUnreadcount() != null){
                                                int count = Integer.parseInt(responseModel.getData().getGcData().get(i).getUnreadcount());
                                                notificationCount+=count;
                                            }
                                        }
                                        PreferenceConnector.writeInteger(getContext(),"channel/"+PreferenceConnector.TOTAL_UNREAD_NOTIFICATION_COUNT,notificationCount);
                                        dashboardMessageCountListener.getMessageChannelCount(notificationCount);
                                    }

                                    gcList = new ArrayList<>();
                                    gcList.addAll(responseModel.getData().getGcData());

                                    if (responseModel.getData().getGcImageBasePath() != null) {
                                        profileImageBaseUrl = responseModel.getData().getGcImageBasePath();
                                    }

                                    channelViewAdapter.updateList(gcList,profileImageBaseUrl);
                                }
                            });
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("Socket : ","Channel Chat Exception ---"+e.toString());
                }
            });
        }
        catch (Exception e){
            Log.d("JsonException ---",e.toString());
        }
    }

    private void loadDataToAdapter()
    {

        channelViewAdapter = new ChannelViewAdapter(getActivity(),gcList,profileImageBaseUrl);

        binding.channelViewList.setHasFixedSize(true);
        binding.channelViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.channelViewList.setAdapter(channelViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        //updateChannelDashboardSocket();
    }


    private void messageReceived() {

        /*socketInstance.on("messageReceived", args -> {

            Log.d("Message_Received_Listener","Channel Fragment");
            //updateChannelDashboardSocket();
        });*/
    }
    @Override
    public void onStop() {
        super.onStop();
        //socketInstance.off("messageReceived");
    }
}
