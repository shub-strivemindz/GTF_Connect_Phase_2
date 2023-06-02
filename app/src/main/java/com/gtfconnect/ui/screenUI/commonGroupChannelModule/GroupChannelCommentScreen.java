package com.gtfconnect.ui.screenUI.commonGroupChannelModule;

import static com.gtfconnect.services.SocketService.socketInstance;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.audiowaveform.WaveformSeekBar;
import com.example.flyingreactionanim.Directions;
import com.example.flyingreactionanim.ZeroGravityAnimation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gtfconnect.R;
import com.gtfconnect.databinding.ActivityCommentSectionBinding;
import com.gtfconnect.interfaces.GroupCommentListener;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels.CommentDeleteResponseModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels.CommentEditResponseModel;
import com.gtfconnect.models.commonGroupChannelResponseModels.commentResponseModels.CommentReceiveResponseModel;
import com.gtfconnect.ui.adapters.ForwardPersonListAdapter;
import com.gtfconnect.ui.adapters.channelModuleAdapter.ChannelMediaAdapter;
import com.gtfconnect.ui.adapters.commonGroupChannelAdapters.GroupChannelCommentAdapter;
import com.gtfconnect.utilities.AudioPlayUtil;
import com.gtfconnect.utilities.GlideUtils;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.socket.emitter.Emitter;

public class GroupChannelCommentScreen extends AppCompatActivity implements GroupCommentListener {

    ActivityCommentSectionBinding binding;

    ChannelRowListDataModel detail;

    private boolean replyOnComment = false;

    private CommentReceiveResponseModel responseModel;

    private JSONObject jsonRawObject;

    GroupChannelCommentAdapter commentViewAdapter;

    private boolean isCommentEditable = false;

    private int commentID = 0;

    private LinearLayoutManager mLayoutManager;

    String userID;

    String userName = "";

    String postBaseUrl = "";

    String profileBaseUrl = "";

    private int commentCount = 0;

    private boolean isDiscussionAllowed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userID = String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));

        commentReceiver();

        replyOnComment = getIntent().getBooleanExtra("replyOnComment",false);
        postBaseUrl = getIntent().getStringExtra("postBaseUrl");
        profileBaseUrl = getIntent().getStringExtra("profileBaseUrl");
        isDiscussionAllowed = getIntent().getBooleanExtra("isDiscussionAllowed",false);


        if (isDiscussionAllowed){
            binding.searchSubContainer.setVisibility(View.VISIBLE);
            binding.footerStatusTag.setVisibility(View.GONE);
        }
        else{
            binding.searchSubContainer.setVisibility(View.GONE);
            binding.footerStatusTag.setVisibility(View.VISIBLE);
        }


        //userID = getIntent().getStringExtra("userID");

        Log.d("Getting user ID ---",userID);
        //detail = new GroupChatResponseModel.Row();

        Gson gson = new Gson();
        detail = gson.fromJson(getIntent().getStringExtra("userDetail"), ChannelRowListDataModel.class);

        editCommentListener();
        deleteCommentListener();


        if (detail != null) {

            mLayoutManager = new LinearLayoutManager(this);

            commentViewAdapter = new GroupChannelCommentAdapter(this, detail.getCommentData(),userID,this,profileBaseUrl);
            binding.commentRecycler.setHasFixedSize(true);
            binding.commentRecycler.setLayoutManager(mLayoutManager);
            binding.commentRecycler.setAdapter(commentViewAdapter);
        }
        else{

            detail = new ChannelRowListDataModel();

            commentViewAdapter= new GroupChannelCommentAdapter(GroupChannelCommentScreen.this,detail.getCommentData(),userID, this,profileBaseUrl);
            binding.commentRecycler.setHasFixedSize(true);
            binding.commentRecycler.setLayoutManager(new LinearLayoutManager(GroupChannelCommentScreen.this));
            binding.commentRecycler.setAdapter(commentViewAdapter);
        }


        initializeData();



        if (replyOnComment)
        {
            binding.type.requestFocus();
            Utils.softKeyboard(this,true,binding.type);

            if (detail.getCommentData() != null){
                if (detail.getCommentData().size() != 0) {

                    /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
                    //final int scrollTo = binding.commentRecycler.getChildAt(detail.getCommentData().size() - 1).getY();

                    binding.nestedScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.nestedScroll.fling(0);
                            int lastIndex = detail.getCommentData().size() - 1;
                            final RecyclerView.ViewHolder viewHolder = binding.commentRecycler.findViewHolderForAdapterPosition(lastIndex);
                            float scrollTo = viewHolder.itemView.getY();
                            binding.nestedScroll.smoothScrollTo(0, (int) scrollTo);
                        }
                    });

                    //binding.commentRecycler.getLayoutManager().scrollToPosition(detail.getCommentData().size() - 1);

                }
            }
        }

        binding.sendMessage.setOnClickListener(view -> {
            sendMessage(binding.type.getText().toString(),view);
        });


        binding.backClick.setOnClickListener(view -> onBackPressed());
    }


    private void sendMessage(String message,View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        int id = Integer.parseInt(userID);
        int groupChannelId = detail.getGroupChannelID();
        int gcMemberID = detail.getGCMemberID();
        Integer groupChatId = Integer.parseInt(detail.getGroupChatID());

        if (message != null){
            if (!(message.equalsIgnoreCase(""))) {



                try {

                    jsonRawObject = new JSONObject();

                    jsonRawObject.put("UserID", id);
                    jsonRawObject.put("GroupChannelID", groupChannelId);
                    jsonRawObject.put("GroupChatID", groupChatId);
                    jsonRawObject.put("GCMemberID",gcMemberID);
                    jsonRawObject.put("comment", message);


                    if (isCommentEditable)
                    {
                        jsonRawObject.put("CommentID",commentID);

                        Log.v("Send Message Params --", jsonRawObject.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketInstance.emit("commentEdit", jsonRawObject);
                                    isCommentEditable = false;
                            }
                        });

                    }
                    else {
                        jsonRawObject.put("GCMemberID", gcMemberID);

                        Log.v("Send Message Params --", jsonRawObject.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketInstance.emit("comment", jsonRawObject);
                                isCommentEditable = false;

                               /* new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLayoutManager.scrollToPosition(0);
                                    }
                                },1000);*/
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("sendMessage Exception ----", e.toString());
                }
            }
            else {
                isCommentEditable = false;
                Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            isCommentEditable = false;
            Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
        }

        binding.type.setText("");
    }

    private void commentReceiver()
    {
        responseModel = new CommentReceiveResponseModel();

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<CommentReceiveResponseModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentted", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :",data.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(data.toString());

                        responseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                commentCount++;
                                binding.commentContainer.setVisibility(View.VISIBLE);
                                binding.commentCount.setText(String.valueOf(commentCount));

                                detail = responseModel.getData().getChatDetail();
                                commentViewAdapter.updateList(detail.getCommentData());
                            }
                        });


                        try {
                            Log.d("Latest Comment :",data.getJSONObject("data").getJSONObject("comment").toString());
                        }
                        catch (Exception e)
                        {
                            Log.d("Error fetching comment ",e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Todo: Remove when live user ID has been used

                                    CommentReceiveResponseModel comment = new CommentReceiveResponseModel();


                           /* if (detail.getGroupChatID() == )
                            if (userID != 3) {
                                if (data.getString("type").equalsIgnoreCase("start")) {
                                    binding.userSubscribers.setText(data.getString("Firstname"));
                                    binding.subscriberSubtitle.setText("is typing");
                                }
                                if (data.getString("type").equalsIgnoreCase("end")) {
                                    binding.userSubscribers.setText(String.valueOf(subscribers));
                                    binding.subscriberSubtitle.setText("subscribers");
                                }
                            }*/
                                }
                                catch(Exception e)
                                {
                                    Log.d("Typing header exception --",e.toString());
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void oldMessage(String oldMessage,int commentID,int position) {

        binding.type.setText(oldMessage);
        binding.type.requestFocus();
        this.commentID = commentID;

        isCommentEditable = true;

        Utils.softKeyboard(this,true,binding.type);
        binding.type.setSelection(oldMessage.length());

        //scrollToPosition(position);
    }

    private void editCommentListener()
    {

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<CommentEditResponseModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentEditData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONArray response = new JSONArray();
                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :",data.toString());


                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(data.toString());

                        CommentEditResponseModel editResponseModel = new CommentEditResponseModel();
                        try {
                            editResponseModel = new Gson().fromJson(gsonObject,type);
                        }
                        catch (Exception e)
                        {
                            Log.d("Edit Comment Listener error :",e.toString());
                        }


                        /*JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(response.toString());*/

                        //detail.getCommentData()

                        Log.d("Edit Comment Response",response.toString());
                        if (editResponseModel != null && editResponseModel.getData() != null && editResponseModel.getData().getCommentList() != null) {
                            detail.setCommentData(editResponseModel.getData().getCommentList());
                        }


                        //responseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //detail = responseModel.getData().getChatDetail();
                                commentViewAdapter.updateList(detail.getCommentData());
                            }
                        });

                    }
                });
            }
        });
    }


    @Override
    public void notifyDeleteComment(int commentID,int chatID,int channelID,int userID) {

        jsonRawObject = new JSONObject();

        try {
            jsonRawObject.put("CommentID",commentID);
            jsonRawObject.put("UserID",PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));
            jsonRawObject.put("GroupChatID",chatID);
            jsonRawObject.put("GroupChannelID",channelID);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketInstance.emit("commentDelete", jsonRawObject);
                }
            });
        }
        catch (Exception e)
        {
            Log.d("Delete Comment Exception ---",e.toString());
        }

    }


    private void deleteCommentListener()
    {

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<CommentDeleteResponseModel>() {
        }.getType();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentDeleteData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONArray response = new JSONArray();
                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :","after delete --"+data.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject)jsonParser.parse(data.toString());

                        CommentDeleteResponseModel deleteResponseModel = new CommentDeleteResponseModel();
                        try {
                            deleteResponseModel = new Gson().fromJson(gsonObject,type);
                        }
                        catch (Exception e)
                        {
                            Log.d("Edit Comment Listener error :",e.toString());
                        }

                        if (deleteResponseModel != null && deleteResponseModel.getData() != null && deleteResponseModel.getData().getCommentList() != null) {
                            detail.setCommentData(deleteResponseModel.getData().getCommentList());
                        }
                        //detail.setCommentData(gson.fromJson(response.toString(), type));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //detail = responseModel.getData().getChatDetail();

                                commentCount--;

                                if (commentCount <= 0){
                                    binding.commentContainer.setVisibility(View.GONE);
                                }
                                else {
                                    binding.commentContainer.setVisibility(View.VISIBLE);
                                    binding.commentCount.setText(String.valueOf(commentCount));
                                }

                                commentViewAdapter.updateList(detail.getCommentData());
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //destroyListeners();
        finish();

    }


    private void destroyListeners()
    {
        // Todo : Remove all ON listeners of socket

            socketInstance.off("commentEditData");
            socketInstance.off("commentDeleteData");
            socketInstance.off("commentted");

    }





    private void initializeData(){


        // ----------------------------------------------------------------- Getting System Current Date and time-----------------------------------------------------

        if (detail != null){
            if (detail.getUpdatedAt()!=null && !detail.getUpdatedAt().isEmpty()){
                if (Utils.getHeaderDate(detail.getUpdatedAt()).equalsIgnoreCase("Today")){

                    /*binding.currentHeaderDate.setVisibility(View.VISIBLE);
                    binding.currentDate.setText(Utils.getHeaderDate(detail.getUpdatedAt()));*/
                }
            }
        }


        assert detail != null;
        if (detail.getUser() != null) {
            if (detail.getUser().getFirstname() == null && detail.getUser().getLastname() == null) {
                userName = "Bot";
                binding.userName.setText("Bot");
            } else {

                //Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(detail.getUser().getUserID()));

                int userId= Integer.parseInt(detail.getUser().getUserID());
                if (PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                    userName = "You";
                    binding.userName.setText("You");
                }
                else {
                    userName = detail.getUser().getFirstname() + " " + detail.getUser().getLastname();
                    binding.userName.setText(userName);
                }
            }

            if (detail.getUser().getProfileImage() != null){
                String baseUrl = profileBaseUrl + detail.getUser().getProfileImage();
                GlideUtils.loadImage(this,binding.userIcon,baseUrl);
            }
        }






        if (detail.getGroupChatRefID() != null) {
            binding.quoteContainer.setVisibility(View.VISIBLE);
            binding.headerDivider.setVisibility(View.GONE);
            binding.messageContainer.setVisibility(View.GONE);

            if (detail.getQuote() != null) {
                if (detail.getQuote().getMessage() != null) {
                    if (String.valueOf(detail.getUserID()).equalsIgnoreCase(userID)) {

                        binding.quoteContainer.setCardBackgroundColor(getColor(R.color.theme_green));

                        binding.quoteIcon.setColorFilter(getColor(R.color.white));
                        binding.newMessage.setTextColor(getColor(R.color.white));
                        binding.oldMessage.setTextColor(getColor(R.color.white));
                        binding.oldMsgUser.setTextColor(getColor(R.color.white));
                        binding.oldMsgTime.setTextColor(getColor(R.color.white));
                    }
                }
                binding.oldMessage.setTypeface(binding.oldMessage.getTypeface(), Typeface.ITALIC);
                binding.oldMessage.setText(detail.getQuote().getMessage());

                String username = detail.getQuote().getUser().getFirstname() + " " + detail.getQuote().getUser().getLastname();
                binding.oldMsgUser.setText(username);

                binding.oldMsgTime.setText(Utils.getHeaderDate(detail.getQuote().getUpdatedAt()));


                binding.newMessage.setText(detail.getMessage());

            }
        } else {
            binding.headerDivider.setVisibility(View.VISIBLE);
            binding.messageContainer.setVisibility(View.VISIBLE);
            binding.quoteContainer.setVisibility(View.GONE);
        }

        if (detail.getMedia() !=null && !detail.getMedia().isEmpty()) {

            Log.d("chatID","========= "+detail.getGroupChatID()+" channelID ="+detail.getGroupChannelID().toString());

            //List<ChannelMediaResponseModel> mediaResponseModel = detail.getMedia();

            String fileType = Utils.checkFileType(detail.getMedia().get(0).getMimeType());

            if (fileType.equalsIgnoreCase("audio")){

                binding.audioTimeContainer.setVisibility(View.VISIBLE);

                binding.mediaRecycler.setVisibility(View.GONE);
                binding.quoteContainer.setVisibility(View.GONE);
                binding.messageContainer.setVisibility(View.GONE);
                binding.headerDivider.setVisibility(View.GONE);

                binding.audioContainer.setVisibility(View.VISIBLE);


                binding.waveForm.setProgressInPercentage(100);
                binding.waveForm.setWaveform(AudioPlayUtil.createWaveform(), true);
                binding.waveForm.setProgressInPercentage(0);

                if (detail.isAudioDownloaded()){
                    binding.downloadAudio.setVisibility(View.GONE);
                    binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                    String filePath = AudioPlayUtil.getSavedAudioFilePath(detail.getGroupChannelID().toString(), detail.getGroupChatID());

                    long duration = AudioPlayUtil.getAudioDuration(filePath);
                    String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                    binding.totalAudioTime.setText("/" + totalTime);
                }
                else {
                    if (AudioPlayUtil.checkFileExistence(detail.getGroupChannelID().toString(), detail.getGroupChatID())) {

                        binding.downloadAudio.setVisibility(View.GONE);
                        binding.playPauseRecordedAudio.setVisibility(View.VISIBLE);

                        String filePath = AudioPlayUtil.getSavedAudioFilePath(detail.getGroupChannelID().toString(), detail.getGroupChatID());

                        long duration = AudioPlayUtil.getAudioDuration(filePath);
                        String totalTime = AudioPlayUtil.getAudioDurationTime(duration);

                        binding.totalAudioTime.setText("/" + totalTime);
                    } else {
                        binding.audioTimeContainer.setVisibility(View.GONE);

                        binding.downloadAudio.setVisibility(View.VISIBLE);
                        binding.playPauseRecordedAudio.setVisibility(View.GONE);
                    }
                }
            }
            else {

                binding.audioContainer.setVisibility(View.GONE);
                binding.mediaRecycler.setVisibility(View.VISIBLE);

                ChannelMediaAdapter mediaAdapter = new ChannelMediaAdapter(this, detail.getMedia(), postBaseUrl, String.valueOf(userID), userName,null,false,false);
                binding.mediaRecycler.setHasFixedSize(true);
                binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                binding.mediaRecycler.setAdapter(mediaAdapter);

            }
            //binding.postImageContainer.setVisibility(View.VISIBLE);
        }else{
            binding.audioContainer.setVisibility(View.GONE);
            binding.mediaRecycler.setVisibility(View.GONE);
        }



        binding.memberProfileContainer.setOnClickListener(view -> {

            Intent intent = new Intent(GroupChannelCommentScreen.this, MemberProfileScreen.class);
            intent.putExtra("gc_member_id",String.valueOf(detail.getGCMemberID()));
            startActivity(intent);
        });




        /*binding.playPauseRecordedAudio.setOnClickListener(view -> {
            if(AudioPlayUtil.checkFileExistence(detail.getGroupChannelID().toString(),detail.getGroupChatID())){
                String path = AudioPlayUtil.getSavedAudioFilePath(detail.getGroupChannelID().toString(),detail.getGroupChatID());

                long duration = AudioPlayUtil.getAudioDuration(path);
                channelChatListener.playAudio(path,binding.waveForm,duration);


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    //here the play recording has been stop
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    AudioPlayUtil.stopPlayback(this);
                }

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioPostPath);
                    mediaPlayer.prepare();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //play recording here
                AudioPlayUtil.playAudioAnimation(this, seekBar, duration);
                mediaPlayer.start();
            }
        });*/

        binding.downloadAudio.setOnClickListener(view -> {
            String filePath = postBaseUrl + detail.getMedia().get(0).getStoragePath()+detail.getMedia().get(0).getFileName();
            downloadAudio(filePath,detail.getGroupChannelID().toString(),detail.getGroupChatID(),binding.waveForm,binding.downloaderLoader,binding.playPauseRecordedAudio);
        });



        // Todo =============

        /*binding.message.setOnClickListener(view -> {
            if(isMessageClicked){
                //This will shrink textview to 2 lines if it is expanded.
                binding.message.setMaxLines(3);
                isMessageClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                binding.message.setMaxLines(Integer.MAX_VALUE);
                isMessageClicked = true;
            }
        });*/




        if (detail.getMessage() != null && !detail.getMessage().trim().isEmpty()) {

            binding.message.setVisibility(View.VISIBLE);
            //message = detail.getMessage();
            binding.message.setText(detail.getMessage());

        } else {
            binding.message.setVisibility(View.GONE);
        }




       /* binding.expandMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!isMessageClicked) {
                    isMessageClicked = true;
                    ObjectAnimator animation = ObjectAnimator.ofInt(binding.message, "maxLines", 40);
                    animation.setDuration(100).start();

                    binding.expandMessage.setText("See More");

                    //btnSeeMore.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_collapse));/
                } else {
                    isMessageClicked = false;
                    ObjectAnimator animation = ObjectAnimator.ofInt(binding.message, "maxLines", 4);
                    animation.setDuration(100).start();

                    binding.expandMessage.setText("See Less");

                    //btnSeeMore.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_expand));
                }

            }
        });*/
















        if (detail.getCreatedAt() != null) {
            //time = Utils.getHeaderDate(detail.getUpdatedAt());
            binding.time.setText(Utils.getHeaderDate(detail.getUpdatedAt()));
        } else {
            binding.time.setText("XX/XX/XXXX");
        }



        if (detail.getCommentData() == null) {

            binding.commentContainer.setVisibility(View.GONE);
            commentCount = 0;

        } else if (detail.getCommentData().size() == 0) {

            commentCount = 0;
            binding.commentContainer.setVisibility(View.GONE);
        }
        else {

            binding.commentContainer.setVisibility(View.VISIBLE);

            commentCount = detail.getCommentData().size();
            binding.commentCount.setText(String.valueOf(commentCount));
        }



        if (detail.getLike() != null && detail.getLike().size() != 0) {
            if (String.valueOf(detail.getLike().get(0).getUserID()).equalsIgnoreCase(userID) && detail.getLike().get(0).getIsLike() == 1) {
                binding.likeIcon.setColorFilter(getColor(R.color.theme_green));
            }
            else {
                binding.likeIcon.setColorFilter(getColor(R.color.chatIconColor));
            }
        }


        binding.like.setOnLongClickListener(view -> {
            likeAsEmote(binding.likeIcon);
            return false;
        });

        binding.like.setOnClickListener(view -> {

            if (detail.getLike() != null)
            {
                if (detail.getLike().size() != 0)
                {
                    if (detail.getLike().get(0).getIsLike() == 0)
                    {
                        likePost(Integer.parseInt(userID),
                                detail.getGroupChannelID(),
                                detail.getGCMemberID(),
                                Integer.parseInt(detail.getGroupChatID()),
                                1);
                    }
                    else{
                        likePost(Integer.parseInt(userID),
                                detail.getGroupChannelID(),
                                detail.getGCMemberID(),
                                Integer.parseInt(detail.getGroupChatID()),
                                0);
                    }
                }
                else {
                    likePost(Integer.parseInt(userID),
                            detail.getGroupChannelID(),
                            detail.getGCMemberID(),
                            Integer.parseInt(detail.getGroupChatID()),
                            1);
                }
            }
            else {
                likePost(Integer.parseInt(userID),
                        detail.getGroupChannelID(),
                        detail.getGCMemberID(),
                        Integer.parseInt(detail.getGroupChatID()),
                        1);
            }


            final ValueAnimator anim = ValueAnimator.ofFloat(1f, 1.5f);
            anim.setDuration(1000);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    binding.likeIcon.setScaleX((Float) animation.getAnimatedValue());
                    binding.likeIcon.setScaleY((Float) animation.getAnimatedValue());
                }
            });
            anim.setRepeatCount(1);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.start();
        });


        //binding.playVideo.setOnClickListener(view -> playVideo);

        binding.forward.setOnClickListener(view -> {
            Dialog forward_dialog = new Dialog(this);

            forward_dialog.setContentView(R.layout.dialog_forward_message);
            forward_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            forward_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView close = (ImageView) forward_dialog.findViewById(R.id.close);
            close.setOnClickListener(view1 -> forward_dialog.dismiss());


            RecyclerView personList = (RecyclerView) forward_dialog.findViewById(R.id.forward_person_list_recycler);

            int channelID = detail.getGroupChannelID();
            int chatID = Integer.parseInt(detail.getGroupChatID());

            ForwardPersonListAdapter forwardPersonListAdapter = new ForwardPersonListAdapter(this,channelID,chatID);
            personList.setHasFixedSize(true);
            personList.setLayoutManager(new LinearLayoutManager(this));
            personList.setAdapter(forwardPersonListAdapter);

            forwardPersonListAdapter.setOnSaveMessageClickListener(chatID1 -> {
                saveMessage(chatID1);
                forward_dialog.dismiss();
            });

            forward_dialog.show();
        });

    }




    private void downloadAudio(String audioPostUrl, String groupChannelID, String groupChatID, WaveformSeekBar seekBar, LottieAnimationView progressBar, ImageView downloadPlayPic) {

        new Thread() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isDownloading = true;
                        progressBar.setAnimation(R.raw.round_loader);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.playAnimation();

                        String downloadPath = "/" + "connect_audio_files" + "/" + groupChannelID + "/" + groupChatID + "/"
                                + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                                + ".mp3";

                        String fileDownloadPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS) + downloadPath;


                        Log.d("LocalFilePath", fileDownloadPath);
                        Log.d("WebFilePath", audioPostUrl);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioPostUrl));
                        request.setDescription("Downloading");
                        request.setMimeType("audio");
                        request.setTitle("File :");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                downloadPath);
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);


                        DownloadManager.Query query = null;
                        query = new DownloadManager.Query();
                        Cursor c = null;
                        if (query != null) {
                            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                        } else {
                            //return flag;
                        }

                        while (isDownloading) {
                            c = manager.query(query);

                        /*int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            c = false;
                        }*/

                            if (c.moveToFirst()) {
                                //Log.i("FLAG", "Downloading");
                                @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                if (status == DownloadManager.STATUS_RUNNING) {
                                    @SuppressLint("Range") long totalBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (totalBytes > 0) {
                                        @SuppressLint("Range") long downloadedBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        final int progress = (int) ((downloadedBytes * 100L) / totalBytes);
                                        Log.d("download_status", "" + progress);
                                        progressBar.setProgress(progress);
                                    }


                                    //final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                                    //progressBar.setProgress(i);
                                }
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    Log.i("FLAG", "done");
                                    isDownloading = false;


                                    progressBar.cancelAnimation();
                                    progressBar.setVisibility(View.GONE);
                              /*  long duration = AudioPlayUtil.getAudioDuration(fileDownloadPath);
                                runOnUiThread(() -> {
                                    downloadPlayPic.setImageDrawable(getResources().getDrawable(R.drawable.play));
                                    AudioPlayUtil.playAudioAnimation(ChannelChatsScreen.this, seekBar, duration);
                                });*/
                                    //channelViewAdapter.playAudio(fileDownloadPath);

                                }
                            }
                        }
                    }
                });

            }
        }.start();
    }



    private void saveMessage(int chatID) {


        Map<String,Object> params = new HashMap<>();
        params.put("GroupChatID[]",chatID);

        //requestType = Constants.SAVE_MESSAGE_REQUEST_CODE;
        ///connectViewModel.save_group_channel_message(api_token,channelID,params);
    }










    private void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like) {
        jsonRawObject = new JSONObject();
        //likeGroupChatID = groupChatId;


        try {
            jsonRawObject.put("UserID", userID);
            jsonRawObject.put("GroupChannelID", groupChannelId);
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChatID", groupChatId);
            jsonRawObject.put("isLike", like);

            Log.v("Like Message Params --", jsonRawObject.toString());
            Log.v("Like Message Params --", jsonRawObject.toString());

            socketInstance.emit("like", jsonRawObject);
        } catch (Exception e) {
            Log.d("Like Exception ------", e.toString());
        }
    }


    private void likeAsEmote(ImageView rootView) {
       /* Utils.showDialog(0, this, rootView, reactionModel, new SelectEmoteReaction() {
            @Override
            public void selectEmoteReaction(int id, String emoji_code, String emoji_name) {
                for (int i = 0; i < 15; i++) {
                    playAnimation(Utils.textAsBitmap(ChannelChatsScreen.this, emoji_code));
                }
            }
        });*/
    }


    private void playAnimation(Bitmap resID) {
        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(0.2f);
        animation.setOriginationDirection(Directions.BOTTOM);
        animation.setDestinationDirection(Directions.TOP);
        animation.setImage(resID);
        animation.setAnimationListener(new Animation.AnimationListener() {
                                           @Override
                                           public void onAnimationStart(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationEnd(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }
                                       }
        );

        ViewGroup container = findViewById(R.id.animation_holder);
        animation.play(this, container);
    }



}
