package com.gtfconnect.ui.fragments;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gtfconnect.databinding.FragmentMentorViewBinding;
import com.gtfconnect.interfaces.UnreadCountHeaderListener;
import com.gtfconnect.ui.adapters.MentorViewAdapter;
import com.gtfconnect.ui.adapters.RecentViewAdapter;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import io.socket.client.Ack;

public class MentorFragment extends Fragment {

    FragmentMentorViewBinding binding;

    private JSONObject jsonRawObject;
    private final int userId = 13720;

    private UnreadCountHeaderListener unreadMessageListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMentorViewBinding.inflate(inflater, container, false);

        updateMentorDashboardSocket();

        // Todo : Unset count to real data
        //unreadMessageListener.getUnreadCount(0);

        // Load Recent List Data -----
        /*MentorViewAdapter mentorViewAdapter= new MentorViewAdapter(requireContext(),1);
        binding.mentorViewList.setHasFixedSize(true);
        binding.mentorViewList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.mentorViewList.setAdapter(mentorViewAdapter);*/


        return binding.getRoot();
    }

    private void updateMentorDashboardSocket()
    {
        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("userId", userId);
            jsonRawObject.put("filter","mentor");

            socketInstance.emit("auth-user", jsonRawObject, (Ack) args -> {

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Mentor Data ----",responseData.toString());

                if (responseData == null) {
                    Utils.showSnackMessage(getContext(), binding.getRoot(), "No Data Found");
                    Log.d("authenticateUserAndFetchData -- ", "Error");
                } else {
                    Log.d("authenticateUserAndFetchData -- ", String.valueOf(responseData));
                }

            });
        }
        catch (Exception e){
            Log.d("JsonException ---",e.toString());
        }
    }
}
