package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

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
import com.gtfconnect.databinding.FragmentChannelViewBinding;
import com.gtfconnect.roomDB.AppDao;
import com.gtfconnect.roomDB.AppDatabase;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardResponseModel;
import com.gtfconnect.ui.adapters.ChannelViewAdapter;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChannelViewBinding.inflate(inflater, container, false);

        init();

        userId = PreferenceConnector.readInteger(requireContext(),PreferenceConnector.GTF_USER_ID,0);
        Log.d("USER ID ",String.valueOf(userId));

        messageReceived();

        // Todo : Unset count to real data
        //unreadMessageListener.getUnreadCount(0);

        return binding.getRoot();
    }


    private void init()
    {
        appDao = AppDatabase.getInstance(requireActivity().getApplication()).appDao();
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
    }


    private void loadLocalData()
    {

        databaseViewModel.getDashboardList("channel").observe(requireActivity(), channelResponseList -> {
            if (channelResponseList != null && !channelResponseList.isEmpty()) {


                    localDBDataSize = channelResponseList.size();

                    gcList = new ArrayList<>();
                    gcList.addAll(channelResponseList);

                    /*responseModel = new ChannelResponseModel();
                    responseModel.setData(channelResponseModel);*/

                    loadDataToAdapter();

                    Log.d("Channel_TABLE",channelResponseList.toString());

                    updateChannelDashboardSocket();
                } else {
                    updateChannelDashboardSocket();
                }
        });
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
                                if (responseModel.getData() != null && responseModel.getData().getGcData() != null) {

                                    /*if (localDBDataSize != responseModel.getData().size()) {
                                        for (int i = 0; i < responseModel.getData().size(); i++) {
                                            databaseViewModel.insertChannels(responseModel.getData().get(i));
                                        }
                                    }*/

                                    if (gcList != null && !gcList.isEmpty()) {
                                        if (responseModel.getData().getGcData().size() > gcList.size() || responseModel.getData().getGcData().size() < gcList.size()) {
                                            for (int i = 0; i < responseModel.getData().getGcData().size(); i++) {

                                                if (responseModel.getData().getGcImageBasePath() != null) {
                                                    responseModel.getData().getGcData().get(i).setBaseUrl(responseModel.getData().getGcImageBasePath());
                                                }
                                                responseModel.getData().getGcData().get(i).setDashboardType("channel");
                                            }

                                            databaseViewModel.insertDashboardData(responseModel.getData().getGcData());
                                        } else {
                                            for (int i = 0; i < gcList.size(); i++) {

                                                boolean isIDFound = false;
                                                for (int j = 0; j < responseModel.getData().getGcData().size(); j++) {
                                                    if (Objects.equals(gcList.get(i).getGroupChannelID(), responseModel.getData().getGcData().get(j).getGroupChannelID())) {
                                                        isIDFound = true;
                                                    }
                                                }
                                                if (!isIDFound) {
                                                    for (int k = 0; k < responseModel.getData().getGcData().size(); k++) {

                                                        if (responseModel.getData().getGcImageBasePath() != null) {
                                                            responseModel.getData().getGcData().get(k).setBaseUrl(responseModel.getData().getGcImageBasePath());
                                                        }
                                                        responseModel.getData().getGcData().get(k).setDashboardType("channel");
                                                    }

                                                    databaseViewModel.insertDashboardData(responseModel.getData().getGcData());
                                                }
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < responseModel.getData().getGcData().size(); i++) {

                                            if (responseModel.getData().getGcImageBasePath() != null) {
                                                responseModel.getData().getGcData().get(i).setBaseUrl(responseModel.getData().getGcImageBasePath());
                                            }
                                            responseModel.getData().getGcData().get(i).setDashboardType("channel");
                                        }

                                        databaseViewModel.insertDashboardData(responseModel.getData().getGcData());
                                    }
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
        //Log.d("Group Data ----"," reposne -----"+responseModel);

        ChannelViewAdapter channelViewAdapter= new ChannelViewAdapter(getActivity(),gcList);
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
