package com.gtfconnect.ui.screenUI.groupModule;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.gtfconnect.services.SocketService.socketInstance;
import static com.gtfconnect.utilities.Constants.CAPTURE_IMAGE_REQUEST_CODE;
import static com.gtfconnect.utilities.Constants.RECORD_AUDIO_REQUEST_CODE;
import static com.gtfconnect.utilities.Constants.SELECT_DOCUMENT_REQUEST_CODE;
import static com.gtfconnect.utilities.Constants.SELECT_PICTURE_REQUEST_CODE;
import static com.gtfconnect.utilities.Constants.SELECT_VIDEO_REQUEST_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiorecorder.OnBasketAnimationEnd;
import com.example.audiorecorder.OnRecordClickListener;
import com.example.audiorecorder.OnRecordListener;
import com.example.flyingreactionanim.Directions;
import com.example.flyingreactionanim.ZeroGravityAnimation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;
import com.gtfconnect.R;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.controller.Rest;
import com.gtfconnect.databinding.ActivityGroupChatBinding;
import com.gtfconnect.interfaces.ApiResponseListener;
import com.gtfconnect.interfaces.AttachmentUploadListener;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.interfaces.ImagePreviewListener;
import com.gtfconnect.interfaces.SelectEmoteReaction;
import com.gtfconnect.interfaces.UpdateGroupDummyUserListener;
import com.gtfconnect.models.EmojiListModel;
import com.gtfconnect.models.ImagePreviewModel;
import com.gtfconnect.models.PinnedMessagesModel;
import com.gtfconnect.models.SendAttachmentResponseModel;
import com.gtfconnect.models.channelResponseModel.ChannelCommentResponseModel;
import com.gtfconnect.models.channelResponseModel.ChannelManageReactionModel;
import com.gtfconnect.models.channelResponseModel.ChannelMessageReceivedModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelChatResponseModel;
import com.gtfconnect.models.channelResponseModel.channelChatDataModels.ChannelRowListDataModel;
import com.gtfconnect.models.groupChannelModels.GroupChannelInfoResponseModel;
import com.gtfconnect.models.groupResponseModel.GetDummyUserModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.models.groupResponseModel.GroupCommentResponseModel;
import com.gtfconnect.models.groupResponseModel.GroupMessageReceivedModel;
import com.gtfconnect.models.groupResponseModel.PostDeleteModel;
import com.gtfconnect.roomDB.DatabaseViewModel;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatHeaderDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.adapters.ExclusiveOfferAdapter;
import com.gtfconnect.ui.adapters.ImageMiniPreviewAdapter;
import com.gtfconnect.ui.adapters.groupChatAdapter.DummyUserListAdapter;
import com.gtfconnect.ui.adapters.groupChatAdapter.GroupChatAdapter;
import com.gtfconnect.ui.screenUI.channelModule.ChannelChatsScreen;
import com.gtfconnect.ui.screenUI.channelModule.ChannelProfileScreen;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.GifPreviewScreen;
import com.gtfconnect.utilities.AttachmentUploadUtils;
import com.gtfconnect.utilities.Constants;
import com.gtfconnect.utilities.CustomEditText;
import com.gtfconnect.utilities.FetchPath;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;
import com.gtfconnect.viewModels.ChatViewModel;
import com.gtfconnect.viewModels.ConnectViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GroupChatScreen extends AppCompatActivity implements ApiResponseListener, GroupChatListener, ImagePreviewListener, UpdateGroupDummyUserListener {

    ActivityGroupChatBinding binding;

    private JSONObject jsonRawObject;
    Integer gcMemberID, userID;
    int channelID;

    private final int REQUEST_PIN_MESSAGE = 1;

    private final int REQUEST_EMOJI_LIST = 2;

    private final int REQUEST_UPLOAD_FILE = 3;

    private final int PINNED_MESSAGE_COUNT = 4;

    private final int GET_DUMMY_USER = 5;

    private final int UPDATE_DUMMY_USER = 6;

    private final int GET_GROUP_CHANNEL_INFO = 7;


    private int requestType;

    private ArrayList<ImagePreviewModel> multipleImageUri;

    private Uri singleImageUri;

    private long mLastClickTime;

    private File selectedMedia;

    private boolean isMessageQuoted = false;


    private boolean isUserTyping = false;

    private int localSavedChatCount = 0;

    private ChannelMessageReceivedModel receivedMessage;

    private boolean isScrollDownHighlighted = false;

    private boolean enableSearchScroll = false;
    private int typeCount = 0;

    private ChannelChatResponseModel responseModel;

    private ChannelChatResponseModel localResponseModel;

    private boolean isMessageNotFound = true;

    Parcelable recyclerViewState;

    private int totalItem;

    LinearLayoutManager mLayoutManager;

    private int currentPage = 1;

    ArrayList<GroupMessageReceivedModel> receivedMessageList;
    private ArrayList<ChannelRowListDataModel> list;

    private boolean isScrolling = false;

    private int subscribers = 0;
    GroupChatAdapter groupViewAdapter;

    private boolean isListLoadedOnce = false;

    private String groupChatIDRef;

    private int likeGroupChatID;

    private boolean isSelfMessageReceived = false;

    // Params to search quoted data :
    private boolean loadSearchData, isDataAvailable;
    private int searchedTillPosition;

    private Rest rest;

    private ApiResponseListener listener;

    private ChatViewModel chatViewModel;

    private EmojiListModel emojiListModel;

    private ArrayList<File> attachmentFileList;

    private String mediaIDs;
    private boolean isAnyFileAttached = false;

    private int attachment_request_code = 0;

    private boolean isAttachmentSend = false;

    private String messageText = "";

    private String postBaseUrl = "";

    private boolean searchPinnedMessageEnabled = false;

    private int pinMessageCount = 0;

    private ChannelCommentResponseModel commentResponseModel;

    // creating a variable for media recorder object class.
    private MediaRecorder mRecorder;

    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;

    // string variable is created for storing a file name
    private static String mFileName = null;


    private boolean isUserTypingMessage = false;

    private int selectedImageUriIndex;


    private GetDummyUserModel getDummyUserModel;

    private String default_initials = "";

    private ChannelManageReactionModel reactionModel;

    ConnectViewModel connectViewModel;

    private boolean showPostSelectionCheckBox = false;


    private DatabaseViewModel databaseViewModel;

    private InfoDbEntity infoDbEntity;


    private MediaRecorder mMediaRecorder;

    private String audioFilePath = "";

    private GroupChannelChatDbEntity databaseEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        channelID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_CHANNEL_ID, ""));

        init();


        // Setting GIF | Image Insertion in Search EditText
        binding.type.setKeyBoardInputCallbackListener(new CustomEditText.KeyBoardInputCallbackListener() {
            @Override
            public void onCommitContent(InputContentInfoCompat inputContentInfo,
                                        int flags, Bundle opts) {

                //you will get your gif/png/jpg here in inputContentInfo
                // You can use a webView or ImageView to load the gif

                Uri linkUri = inputContentInfo.getLinkUri();

                Intent intent = new Intent(GroupChatScreen.this, GifPreviewScreen.class);
                intent.putExtra("gif",linkUri.toString());
                startForActivityResultLauncher.launch(intent);

                //mWebView.loadUrl(linkUri != null ? linkUri.toString() : "null");

            }
        });

        //destroyListeners();

        searchPinnedMessageEnabled = getIntent().getBooleanExtra("searchPinMessage", false);

        binding.quoteContainer.setVisibility(View.GONE);
        binding.attachmentContainer.setVisibility(View.GONE);


        rest = new Rest(this, false, false);

        binding.iconContainer.setVisibility(View.GONE);

        list = new ArrayList<>();
        receivedMessageList = new ArrayList<>();

        gcMemberID = Integer.parseInt(PreferenceConnector.readString(this, PreferenceConnector.GC_MEMBER_ID, ""));
        userID = PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0);

        loadDataToAdapter();

        Log.d("data_channel_id",""+channelID);

        String userName = PreferenceConnector.readString(this, PreferenceConnector.GC_NAME, "");
        binding.userName.setText(userName);

        //updateGroupChatSocket(false);



        //------------------------------------------------------------------------- Socket Listening Events -------------------------------------------------------------
        userTypingListener();
        messageReceivedListener();
        updateLikeListener();
        deletePostListener();
        commentReceiver();

        //deleteCommentListener();
        //------------------------------------------------------------------------- ------------------------ -------------------------------------------------------------



        binding.quoteContainer.setVisibility(View.GONE);
        binding.attachmentContainer.setVisibility(View.GONE);
        binding.forwardContainer.setVisibility(View.GONE);

        binding.sendMessage.setVisibility(View.GONE);
        binding.recordButton.setVisibility(View.VISIBLE);
        binding.pinAttachment.setVisibility(View.VISIBLE);




        // Navigate for Member Chat
        binding.memberTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupChatScreen.this, ChannelProfileScreen.class));

            }
        });

        binding.pin.setOnClickListener(view -> {


            if (pinMessageCount == 0) {
                Utils.showSnackMessage(GroupChatScreen.this, binding.pin, "No Pinned Message Found!");
            } else {
                Intent i = new Intent(GroupChatScreen.this, GroupPinnedMessageScreen.class);
                i.putExtra("post_base_url", postBaseUrl);
                startActivity(i);
                finish();
            }
        });

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // Bottom sheet for Mute Notifications
        binding.pinAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Todo..................Uncomment below condition when video can be shared

                //if (!isAnyFileAttached)
                openAttachmentDialog();
            }
        });

        binding.iconContainer.setOnClickListener(view -> {
            //currentPage = 1;

            if (isScrollDownHighlighted) {
                isScrollDownHighlighted = false;
                binding.arrowIcon.setColorFilter(getResources().getColor(R.color.tab_grey));
            }


            scrollToPosition(0,false);

            binding.iconContainer.setVisibility(View.GONE);

        });

        binding.closeQuoteEditor.setOnClickListener(view -> {

            binding.sendMessage.setVisibility(View.GONE);
            binding.recordButton.setVisibility(View.VISIBLE);
            binding.pinAttachment.setVisibility(View.VISIBLE);

            binding.quoteContainer.setVisibility(View.GONE);
            isMessageQuoted = false;
        });





        binding.chats.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = mLayoutManager.findFirstVisibleItemPosition();

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;

                    binding.chipDateContainer.setVisibility(View.VISIBLE);

                    if (position != -1) {
                        if (!groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));
                        } else {
                            binding.chipDateContainer.setVisibility(View.GONE);
                        }
                    }
                }
                else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    if (position != -1) {
                        if (!groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));
                        }
                    }
                    binding.chipDateContainer.setVisibility(View.GONE);
                }
                else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                    binding.chipDateContainer.setVisibility(View.VISIBLE);

                    if (position != -1) {
                        if (!groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()).isEmpty()) {
                            binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));
                        } else {
                            binding.chipDateContainer.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                recyclerViewState = binding.chats.getLayoutManager().onSaveInstanceState(); // save recycleView state
                totalItem = mLayoutManager.getItemCount();


                if (isScrolling && mLayoutManager.findLastCompletelyVisibleItemPosition() == totalItem - 1 && dy < 0) {

                    Log.d("ChannelGroupChat","totalItem = "+totalItem);
                    binding.chipDateContainer.setVisibility(View.GONE);

                    isScrolling = false;
                    currentPage++;

                    //binding.loader.setVisibility(View.VISIBLE);

                    updateGroupChatSocket(true);

                }
                else{
                    if (!groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()).isEmpty()) {
                        Log.d("Chip Date = ", "" + groupViewAdapter.getChipDate(mLayoutManager.findFirstVisibleItemPosition()));
                        binding.chipDate.setText(groupViewAdapter.getChipDate(mLayoutManager.findLastVisibleItemPosition()));
                    } else {
                        binding.chipDateContainer.setVisibility(View.GONE);
                    }
                }

                if (mLayoutManager.findFirstVisibleItemPosition() > 5) {
                    binding.iconContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.iconContainer.setVisibility(View.GONE);
                }


            }

        });

        binding.type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.sendMessage.setVisibility(View.VISIBLE);
                binding.recordButton.setVisibility(View.GONE);

                isUserTyping = true;
                typeCount = charSequence.length();

                if (typeCount == 0) {

                    binding.sendMessage.setVisibility(View.GONE);
                    binding.recordButton.setVisibility(View.VISIBLE);
                    isUserTypingMessage = false;
                    endTypingListener();
                }
                else {
                    isUserTypingMessage = true;
                    startTypingListener();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.sendMessage.setOnClickListener(view -> {


            if (isUserTypingMessage) {

                binding.sendMessage.setVisibility(View.GONE);
                binding.recordButton.setVisibility(View.VISIBLE);

                endTypingListener();
                messageText = binding.type.getText().toString().trim();

                if (isAnyFileAttached) {
                    if (messageText != null && !messageText.equalsIgnoreCase("")) {
                        callAttachmentApi();
                    } else {
                        Utils.showSnackMessage(this, binding.type, "Type Message !");
                        binding.imagePreviewLayout.setVisibility(View.GONE);
                        isAnyFileAttached = false;
                        isAttachmentSend = false;
                    }
                } else {
                    validateSendMessage(messageText, binding.type);
                }
            }
            else{

            }
        });


        binding.closeAttachmentContainer.setOnClickListener(view -> {
            isAnyFileAttached = false;
            binding.attachmentContainer.setVisibility(View.GONE);
        });


        if (searchPinnedMessageEnabled) {
            String groupChatId = getIntent().getStringExtra("groupChatId");
            searchedTillPosition = 0;
            isMessageNotFound = true;
            loadSearchData = false;
            isDataAvailable = true;

            //rest.ShowDialogue();
            searchMessageInList(groupChatId, false, false, false);

        }

        if (pinMessageCount == 0) {
            binding.pinnedMessageCountContainer.setVisibility(View.GONE);
        } else {
            binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);
        }


        binding.dummyUsers.setOnClickListener(view -> {
            BottomSheetDialog chat_options_dialog = new BottomSheetDialog(GroupChatScreen.this);
            chat_options_dialog.setContentView(R.layout.bottomsheet_dummy_user_list);

            RecyclerView dummy_user_recycler = chat_options_dialog.findViewById(R.id.dummy_user_list);

            DummyUserListAdapter dummyUserListAdapter= new DummyUserListAdapter(GroupChatScreen.this,getDummyUserModel,this);
            dummy_user_recycler.setHasFixedSize(true);
            dummy_user_recycler.setLayoutManager(new LinearLayoutManager(this));
            dummy_user_recycler.setAdapter(dummyUserListAdapter);

            chat_options_dialog.show();
        });


        //binding.emoji.setOnClickListener(view -> Utils.showDialog(1,this,rootView,emojiListModel));
        sendMessageAndAudioRecorderEvents();
    }


    // Todo ---------------------------------------Need to use Save Instance State and Restore Instance State for better performance-------------------------------
    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentPage",currentPage);

    }*/







    private void sendMessageAndAudioRecorderEvents() {


        binding.recordButton.setRecordView(binding.recordView);
        binding.recordView.setOnBasketAnimationEndListener(() -> {
            Log.d("RecordView", "Basket Animation Finished");
            binding.footerSearchContainer.setVisibility(View.VISIBLE);
            binding.recordView.setVisibility(View.GONE);
        });

        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..

                audioFilePath = "";
                binding.footerSearchContainer.setVisibility(View.GONE);
                binding.recordView.setVisibility(View.VISIBLE);

                recordAudio();
                Log.d("RecordView", "onStart");
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");
                stopRecording();
                Utils.deleteAudioFile(audioFilePath);
                /*binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);*/
            }

            @Override
            public void onFinish(long recordTime, boolean limitReached) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                //String time = getHumanTimeText(recordTime);
                Log.d("RecordView", "onFinish");
                stopRecording();

                File audioFile = new File(audioFilePath);

                attachmentFileList = new ArrayList<>();
                attachmentFileList.add(audioFile);
                isAnyFileAttached = true;

                attachment_request_code = RECORD_AUDIO_REQUEST_CODE;
                callAttachmentApi();

                binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);

            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");
                binding.footerSearchContainer.setVisibility(View.VISIBLE);
                binding.recordView.setVisibility(View.GONE);

                stopRecording();
                Utils.deleteAudioFile(audioFilePath);
            }

            @Override
            public void onLock() {
                //When Lock gets activated
                Log.d("RecordView", "onLock");
            }

        });


        // binding.recordButton.setListenForRecord(false);

        //ListenForRecord must be false ,otherwise onClick will not be called
        /*binding.recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Log.d("RecordButton", "RECORD BUTTON CLICKED");
            }
        });

        binding.recordView.setLockEnabled(true);
        binding.recordView.setRecordLockImageView(findViewById(R.id.record_lock));*/
    }


    private void recordAudio() {
        //check the permission for the record audio and for save audio write external storage

        if (CheckPermission()) {
            audioFilePath = Utils.getAudioFilePath();

            //RecordReady
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(audioFilePath);


            try {
                mMediaRecorder.prepare();
                //start the recording
                mMediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //if permission is not given then request permission
            RequestPermission();
        }
    }

    private void stopRecording() {
        try {
            mMediaRecorder.stop();
        } catch (Exception e) {
            Log.d("Recorder_Exception", e.toString());
        }
    }


    public boolean CheckPermission() {
        int first = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int first1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return first == PackageManager.PERMISSION_GRANTED && first1 == PackageManager.PERMISSION_GRANTED;
    }


    //give below permission for audio capture
    private void RequestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
    }


    private void loadLocalData() {

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        databaseViewModel.getGroupChannelInfo(channelID).observe(this, infoDbEntity -> {
            if (infoDbEntity != null) {
                this.infoDbEntity = infoDbEntity;
            }
        });

        databaseViewModel.getGroupChannelChatData(String.valueOf(channelID), 1).observe(this, groupChannelChatDbEntities -> {
            databaseEntity = groupChannelChatDbEntities;
            list = new ArrayList<>();

            if (groupChannelChatDbEntities != null) {
                if (groupChannelChatDbEntities.getChatBodyDbEntitiesLists() != null) {
                    Log.d("run", " times3");

                    Log.d("ChannelChatDataChatDB", "last entered message id =" + groupChannelChatDbEntities.getChatBodyDbEntitiesLists().get(groupChannelChatDbEntities.getChatBodyDbEntitiesLists().size() - 1).getGroupChatID());

                    for (int i = groupChannelChatDbEntities.getChatBodyDbEntitiesLists().size() - 1; i >= 0; i--) {

                        list.add(groupChannelChatDbEntities.getChatBodyDbEntitiesLists().get(i).getRows());
                        groupViewAdapter.updateList(list);
                    }
                    if (groupChannelChatDbEntities.getChatHeaderDbEntity() != null) {
                        if (groupChannelChatDbEntities.getChatHeaderDbEntity().getBaseUrl() != null) {
                            postBaseUrl = groupChannelChatDbEntities.getChatHeaderDbEntity().getBaseUrl();
                        }
                        if (groupChannelChatDbEntities.getChatHeaderDbEntity().getSubscriptionCount() != null) {
                            subscribers = Integer.parseInt(groupChannelChatDbEntities.getChatHeaderDbEntity().getSubscriptionCount());
                            binding.userSubscribers.setText(String.valueOf(subscribers));
                        }
                    }
                    //channelViewAdapter.updatePostBaseUrl(postBaseUrl);
                }
            }
        });
        refreshGroupChatSocket();
    }


    public void init() {

        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        // Getting API data fetch
        rest = new Rest(this, false, true);
        listener = this;

        //=============================================================== ConnectViewModel APIs ===========================================================
        connectViewModel = new ViewModelProvider(this).get(ConnectViewModel.class);
        connectViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });


        //=============================================================== ChatViewModel APIs ===========================================================
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getResponseLiveData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {

                Log.d("API Call Listener ----", "onChanged: " + new Gson().toJson(apiResponse));
                if (apiResponse != null) {

                    //listener.putResponse(apiResponse, auth_rest);
                    listener.putResponse(apiResponse, rest);
                }

            }
        });


        String api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");
        Log.d("api_token",api_token);

        requestType = GET_DUMMY_USER;
        connectViewModel.get_dummy_user_list(channelID,api_token,"android","test");
    }



    private void scrollToPosition(int position,boolean isMessageQuoted) {

        mLayoutManager.scrollToPosition(position);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMessageQuoted){

                    View getRoot = mLayoutManager.findViewByPosition(position);

                    Animation anim = new AlphaAnimation(0.1f, 1.0f);
                    anim.setDuration(1500); //You can manage the blinking time with this parameter
                    //anim.setStartOffset(20);
                    //anim.setRepeatMode(Animation.REVERSE);
                    //anim.setRepeatCount(Animation.INFINITE);
                    getRoot.startAnimation(anim);

                }
            }
        },1000);
    }

    private void loadDataToAdapter() {

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);


        // Load Comments List Data -----
        groupViewAdapter = new GroupChatAdapter(this, list, String.valueOf(userID), postBaseUrl, this);
        binding.chats.setHasFixedSize(true);
        binding.chats.setLayoutManager(mLayoutManager);
        binding.chats.setAdapter(groupViewAdapter);


        binding.chats.getLayoutManager().onRestoreInstanceState(recyclerViewState);

    }


//    private void

    // -------------------------------------------------------------------Socket  Implementation --------------------------------------------------------------------


    private void refreshGroupChatSocket() {
        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelChatResponseModel>() {
        }.getType();

        responseModel = new ChannelChatResponseModel();

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("page", currentPage);

            Log.d("Chat list params --", jsonRawObject.toString());

            socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Chat Data ----", responseData.toString());

                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                responseModel = gson.fromJson(gsonObject, type);

                runOnUiThread(() -> {

                    if(responseModel != null && responseModel.getData() != null && responseModel.getData().getChatData() != null && responseModel.getData().getChatData().getRows() != null) {
                        insertDataInDB(responseModel);
                    }
                });


            });
        } catch (Exception e) {
            Log.d("JsonException ---", e.toString());
        }
    }

    private void updateGroupChatSocket(boolean isScrolling) {
        isListLoadedOnce = true;

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelChatResponseModel>() {
        }.getType();

        responseModel = new ChannelChatResponseModel();

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("page", currentPage);

            Log.d("Chat list params --", jsonRawObject.toString());

            socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                if (!isScrolling)
                    runOnUiThread(() -> rest.dismissProgressdialog());

                JSONObject responseData = (JSONObject) args[0];
                Log.d("Group Chat Data ----", responseData.toString());

                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                responseModel = gson.fromJson(gsonObject, type);


                if (responseData == null) {
                    Utils.showSnackMessage(this, binding.getRoot(), "No Data Found");
                    Log.d("authenticateUserAndFetchData -- ", "Error");
                } else {

                    runOnUiThread(() -> {
                        insertDataInDB(responseModel);
                    });
                }

            });
        } catch (Exception e) {
            Log.d("JsonException ---", e.toString());
        }
    }



    public void insertDataInDB(ChannelChatResponseModel responseModel) {
        GroupChannelChatHeaderDbEntity headerDb = new GroupChannelChatHeaderDbEntity();

        if (responseModel.getData().getChatData().getCount() != null) {
            headerDb.setCount(responseModel.getData().getChatData().getCount().toString());
        }

        headerDb.setGroupChannelID(responseModel.getData().getChatData().getRows().get(0).getGroupChannelID().toString());
        headerDb.setBaseUrl(responseModel.getData().getBaseUrl());
        headerDb.setSubscriptionCount(responseModel.getData().getSubscriptionCount().toString());

        List<GroupChannelChatBodyDbEntity> chatBodyDbEntityList = new ArrayList<>();

        for (int i = 0; i < responseModel.getData().getChatData().getRows().size(); i++) {

            GroupChannelChatBodyDbEntity bodyDbEntity = new GroupChannelChatBodyDbEntity();
            bodyDbEntity.setGroupChatID(Integer.parseInt(responseModel.getData().getChatData().getRows().get(i).getGroupChatID()));
            bodyDbEntity.setRows(responseModel.getData().getChatData().getRows().get(i));
            bodyDbEntity.setGroupChannelID(responseModel.getData().getChatData().getRows().get(i).getGroupChannelID().toString());
            bodyDbEntity.setPage(currentPage);
            chatBodyDbEntityList.add(bodyDbEntity);

        }


        GroupChannelChatDbEntity channelChatDb = new GroupChannelChatDbEntity();
        channelChatDb.setChatHeaderDbEntity(headerDb);
        channelChatDb.setChatBodyDbEntitiesLists(chatBodyDbEntityList);
        /*Log.d("DB_chat_data", channelChatDb.chatBodyDbEntitiesLists.toString());
        Log.d("DB_chat_data", channelChatDb.chatHeaderDbEntity.toString());*/
        databaseViewModel.insertChannelChatData(channelChatDb);
    }



    private void userTypingListener() {
        socketInstance.on("userTyping", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.d("User Typing Data :", data.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (Integer.parseInt(data.getString("userId")) != userID) {
                                if (data.getString("type").equalsIgnoreCase("start")) {
                                    binding.userSubscribers.setText(data.getString("Firstname"));
                                    binding.subscriberSubtitle.setText("is typing");
                                }
                                if (data.getString("type").equalsIgnoreCase("end")) {
                                    binding.userSubscribers.setText(String.valueOf(subscribers));
                                    binding.subscriberSubtitle.setText("subscribers");
                                }
                            }
                        } catch (Exception e) {
                            Log.d("Typing header exception --", e.toString());
                        }
                    }
                });
            }
        });
    }

    private void startTypingListener() {
        try {

            jsonRawObject = new JSONObject();
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("type", "start");

            socketInstance.emit("typing", jsonRawObject);

        } catch (Exception e) {
            Log.d("startTyping  Exception ----", e.toString());
        }
    }

    private void endTypingListener() {
        try {

            jsonRawObject = new JSONObject();
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("userId", userID);
            jsonRawObject.put("type", "end");

            socketInstance.emit("typing", jsonRawObject);

        } catch (Exception e) {
            Log.d("startTyping  Exception ----", e.toString());
        }
    }

    private void validateSendMessage(String message, View view) {

        binding.quoteContainer.setVisibility(View.GONE);
        binding.sendMessage.setVisibility(View.GONE);
        binding.recordButton.setVisibility(View.VISIBLE);
        binding.pinAttachment.setVisibility(View.VISIBLE);

        binding.type.setText("");
        Utils.softKeyboard(this, false, binding.type);

        binding.type.setText("");

        String userID = this.userID.toString();

        if (message != null) {
            if (!message.equalsIgnoreCase("")) {

                try {
                    jsonRawObject = new JSONObject();
                    jsonRawObject.put("GCMemberID", gcMemberID);
                    jsonRawObject.put("GroupChannelID", channelID);
                    jsonRawObject.put("UserID", userID);

                    if (isAttachmentSend) {
                        jsonRawObject.put("ChatType", "file");
                        jsonRawObject.put("MediaIds", mediaIDs);

                        jsonRawObject.put("Message", message);
                        sendMessage(jsonRawObject);
                        Log.v("Send Message Params --", jsonRawObject.toString());
                    } else {
                        jsonRawObject.put("ChatType", "msg");


                        jsonRawObject.put("Message", message);

                        if (isMessageQuoted) {
                            jsonRawObject.put("GroupChatRefID", groupChatIDRef);
                        }

                        sendMessage(jsonRawObject);
                        Log.v("Send Message Params --", jsonRawObject.toString());
                    }

                } catch (Exception e) {
                    Log.d("sendMessage Exception ----", e.toString());
                }
            } else {
                isMessageQuoted = false;
                isAttachmentSend = false;
                Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
            }
        } else {
            isMessageQuoted = false;
            isAttachmentSend = false;
            Toast.makeText(this, "Type message!", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMessage(JSONObject jsonRawObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.emit("sendMessage", jsonRawObject, (Ack) args -> {
                    //boolean isMsgSent = (boolean) args[0];

                    Log.d("Message Send Log", args[0].toString());

                    isMessageQuoted = false;
                    isAttachmentSend = false;

                    currentPage = 1;
                    refreshGroupChatSocket();
                            /*if (isMsgSent) {
                                Log.d("Msg sent ", "successfully");
                                //scrollToFirstPosition();
                                currentPage = 1;
                                refreshGroupChatSocket();

                            } else {
                                Log.d("Msg sent ", "failed");
                            }*/

                });
            }
        });
    }



/*    private ArrayList<GroupChatResponseModel.Row> getLastTenPost(){

        ArrayList<GroupChatResponseModel.Row> data = new ArrayList<>();

        if (list != null){
            for (int i=0;i<list.size();i++){
                if (i >= 10){
                    break;
                }
                else{
                    data.add(list.get(i));
                }

            }
        }
        return data;
    }*/



    private void messageReceivedListener() {

        Log.d("socket_connection>>", "" + socketInstance.connected());
        Log.d("Message_Received_Listener","Channel Message Listener Method");

        socketInstance.on("messageReceived", args -> {

            Log.d("Message_Received_Listener", "Message Received");

            receivedMessage = new ChannelMessageReceivedModel();

            Gson gson = new Gson();
            Type type;

            type = new TypeToken<ChannelMessageReceivedModel>() {
            }.getType();

            isScrollDownHighlighted = true;


            JSONObject data = (JSONObject) args[0];
            Log.d("Message Received Data :", data.toString());
            Log.d("Message Received", "Listener");

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(data.toString());

            receivedMessage = gson.fromJson(gsonObject, type);

            if (!receivedMessage.getSaveMsg().getGetData().getUser().getUserID().equalsIgnoreCase(String.valueOf(PreferenceConnector.readInteger(this, PreferenceConnector.CONNECT_USER_ID, 0)))) {
                binding.arrowIcon.setColorFilter(getResources().getColor(R.color.theme_green));
            }

            //ChannelRowListDataModel rowData = receivedMessage.getSaveMsg().getGetData();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    refreshGroupChatSocket();

                    /*try {
                        if (rowData.getUserID() != null) {
                            if (!Objects.equals(userID, rowData.getUserID())){

                                GroupChannelChatHeaderDbEntity headerDb = new GroupChannelChatHeaderDbEntity();

                                if(databaseEntity != null && databaseEntity.getChatHeaderDbEntity() != null){

                                    if (databaseEntity.getChatHeaderDbEntity().getCount() != null) {
                                        headerDb.setCount(databaseEntity.getChatHeaderDbEntity().getCount());
                                    }

                                    headerDb.setGroupChannelID(channelID+"");

                                    if (databaseEntity.getChatHeaderDbEntity().getBaseUrl() != null) {
                                        headerDb.setBaseUrl(databaseEntity.getChatHeaderDbEntity().getBaseUrl());
                                    }

                                    if (databaseEntity.getChatHeaderDbEntity().getSubscriptionCount() != null) {
                                        headerDb.setSubscriptionCount(databaseEntity.getChatHeaderDbEntity().getSubscriptionCount());
                                    }
                                }
                                else{
                                    headerDb.setCount("");
                                    headerDb.setGroupChannelID(channelID+"");
                                    headerDb.setBaseUrl("");
                                    headerDb.setSubscriptionCount("");
                                }

                                List<GroupChannelChatBodyDbEntity> chatBodyDbEntityList = new ArrayList<>();

                                for (int i = 0; i < responseModel.getData().getChatData().getRows().size(); i++) {

                                    GroupChannelChatBodyDbEntity bodyDbEntity = new GroupChannelChatBodyDbEntity();
                                    bodyDbEntity.setGroupChatID(Integer.parseInt(responseModel.getData().getChatData().getRows().get(i).getGroupChatID()));
                                    bodyDbEntity.setRows(responseModel.getData().getChatData().getRows().get(i));
                                    bodyDbEntity.setGroupChannelID(responseModel.getData().getChatData().getRows().get(i).getGroupChannelID().toString());
                                    bodyDbEntity.setPage(currentPage);

                                    chatBodyDbEntityList.add(bodyDbEntity);

                                }


                                GroupChannelChatDbEntity channelChatDb = new GroupChannelChatDbEntity();
                                channelChatDb.setChatHeaderDbEntity(headerDb);
                                channelChatDb.setChatBodyDbEntitiesLists(chatBodyDbEntityList);
                                Log.d("DB_chat_data", channelChatDb.getChatBodyDbEntitiesLists().get(0).getGroupChatID()+"");
                                Log.d("DB_chat_data", channelChatDb.chatHeaderDbEntity.toString());
                                databaseViewModel.insertChannelChatData(channelChatDb);
                            }
                        }

                    } catch (Exception e) {
                        Log.d("Typing header exception --", e.toString());
                    }*/
                }
            });
        });
    }

    private void updateLikeListener() {

        Gson gson = new Gson();
        Type type;

        Log.d("Like Listener :::: ", "Called");
        type = new TypeToken<ChannelRowListDataModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("likeUpdate", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Like Data :", data.toString());

                        JSONObject response = new JSONObject();
                        try {
                            response = data.getJSONObject("data").getJSONObject("chatDetail");
                        } catch (Exception e) {

                        }


                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(response.toString());

                        ChannelRowListDataModel like_response = gson.fromJson(gsonObject, type);
                        Log.d("Like response from model", String.valueOf(like_response.getLike().get(0).getIsLike()));


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int messageID = data.getInt("messageId");

                                    if (messageID == likeGroupChatID) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).getGroupChatID().equalsIgnoreCase(String.valueOf(likeGroupChatID))) {
                                                if (list.get(i).getLike() != null && !list.get(i).getLike().isEmpty()) {

                                                    Log.d("Is liked User ---", "Not Null case : " + list.get(i).getLike().get(0).getIsLike().toString());

                                                    if (list.get(i).getLike().get(0).getIsLike() == 1) {
                                                        list.get(i).getLike().get(0).setIsLike(0);
                                                        groupViewAdapter.updateList(list);
                                                        break;
                                                    } else {
                                                        list.get(i).getLike().get(0).setIsLike(1);
                                                        groupViewAdapter.updateList(list);
                                                        break;
                                                    }
                                                } else {
                                                    list.get(i).getLike().add(0, like_response.getLike().get(0));

                                                    Log.d("Is liked User ---", "Null case : " + list.get(i).getLike().get(0).getIsLike().toString());
                                                    groupViewAdapter.updateList(list);
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    Log.d("Getting like message id exception :", e.toString());
                                }
                            }
                        });


                    }
                });
            }
        });
    }


    private void commentReceiver() {
        commentResponseModel = new ChannelCommentResponseModel();

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<ChannelCommentResponseModel>() {
        }.getType();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                socketInstance.on("commentted", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("Comment Data :", data.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(data.toString());

                        commentResponseModel = gson.fromJson(gsonObject, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchedTillPosition = 0;
                                isMessageNotFound = true;
                                loadSearchData = false;
                                isDataAvailable = true;
                                searchMessageInList(commentResponseModel.getGroupChatID().toString(), false, true, false);
                            }
                        });


                        try {
                            Log.d("Latest Comment :", data.getJSONObject("data").getJSONObject("comment").toString());
                        } catch (Exception e) {
                            Log.d("Error fetching comment ", e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Todo: Remove when live user ID has been used

                                    GroupCommentResponseModel comment = new GroupCommentResponseModel();


                            /*if (detail.getGroupChatID() == )
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
                                } catch (Exception e) {
                                    Log.d("Typing header exception --", e.toString());
                                }
                            }
                        });
                    }
                });
            }
        });
    }



    /*private void deleteCommentListener()
    {

        Gson gson = new Gson();
        Type type;

        type = new TypeToken<List<GroupChatResponseModel.CommentResponseModel>>() {
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

                        try {
                            response = data.getJSONObject("data").getJSONArray("commentList");
                        }
                        catch (Exception e)
                        {
                            Log.d("Edit Comment Listener error :",e.toString());
                        }

                        commentDeleteResponseModel.setCommentData(gson.fromJson(response.toString(), type));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchMessageInList(commentDeleteResponseModel.getGroupChatID().toString(),false,false,true);
                            }
                        });

                    }
                });
            }
        });
    }*/


    private void searchMessageInList(String groupChatId, boolean hasPostDeleted, boolean commentSearch, boolean commentDeleteSearch) {

        if (isMessageNotFound) {
            if (loadSearchData) {
                Gson gson = new Gson();
                Type type;
                type = new TypeToken<ChannelChatResponseModel>() {
                }.getType();

                responseModel = new ChannelChatResponseModel();


                try {
                    jsonRawObject = new JSONObject();
                    jsonRawObject.put("GCMemberID", gcMemberID);
                    jsonRawObject.put("GroupChannelID", channelID);
                    jsonRawObject.put("userId", userID);
                    jsonRawObject.put("page", currentPage);

                    Log.d("Chat list params --", jsonRawObject.toString());

                    socketInstance.emit("chatList", jsonRawObject, (Ack) args -> {

                        JSONObject responseData = (JSONObject) args[0];
                        Log.d("Group Chat Data ----", responseData.toString());

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(responseData.toString());

                        responseModel = gson.fromJson(gsonObject, type);

                        if (responseData == null) {
                            Utils.showSnackMessage(GroupChatScreen.this, binding.getRoot(), "No Data Found");
                            Log.d("authenticateUserAndFetchData -- ", "Error");
                            isDataAvailable = false;
                        } else if (responseModel.getData().getChatData().getRows() == null || responseModel.getData().getChatData().getRows().isEmpty()) {
                            isDataAvailable = false;
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    insertDataInDB(responseModel);
                                    loadSearchData = false;
                                    currentPage++;

                                    searchMessageInList(groupChatId, hasPostDeleted, commentSearch, commentDeleteSearch);
                                }
                            });

                        }
                    });

                } catch (Exception e) {
                    Log.d("JsonException ---", e.toString());
                }
            } else {
                for (int i = searchedTillPosition; i < list.size(); i++) {
                    if (groupChatId.equalsIgnoreCase(list.get(i).getGroupChatID())) {

                        int position = i;
                        if (hasPostDeleted) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.remove(searchedTillPosition);
                                    groupViewAdapter.updateList(list);
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        } else if (commentSearch) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.get(position).setCommentData(commentResponseModel.getData().getChatDetail().getCommentData());
                                    groupViewAdapter.updateList(list);
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }
                        /*else if (commentDeleteSearch) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (commentDeleteResponseModel.getCommentData() != null) {
                                        list.get(searchedTillPosition).setCommentData(commentDeleteResponseModel.getCommentData());
                                        channelViewAdapter.updateList(list);
                                    }
                                    else {
                                        list.get(searchedTillPosition).setCommentCount(0);
                                        channelViewAdapter.updateList(list);
                                    }
                                }
                            });
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }*/
                        else {
                            scrollToPosition(i,true);
                            rest.dismissProgressdialog();
                            isMessageNotFound = false;
                            break;
                        }
                    }
                    searchedTillPosition = i;
                }
                loadSearchData = true;
                searchMessageInList(groupChatId, hasPostDeleted, commentSearch, commentDeleteSearch);
            }
        }
    }


    private void deletePostListener() {
        Gson gson = new Gson();
        Type type;
        type = new TypeToken<PostDeleteModel>() {
        }.getType();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                socketInstance.on("postDeleteData", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject response = (JSONObject) args[0];

                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(response.toString());

                        PostDeleteModel postDeleteModel = gson.fromJson(gsonObject, type);
                        searchMessageInList(postDeleteModel.getGroupChatID().toString(), true, false, false);

                    }
                });
            }
        });

    }


    // -------------------------------------------------------------- Api Calls -----------------------------------------------------------------------------

    private void callAttachmentApi()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("GCMemberID",gcMemberID);
        params.put("GroupChannelID",channelID);
        params.put("UserID",this.userID.toString());



        List<MultipartBody.Part> files = new ArrayList<>();

        if (attachment_request_code == CAPTURE_IMAGE_REQUEST_CODE){

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        }
        else if(attachment_request_code == SELECT_VIDEO_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("video/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        }
        else if (attachment_request_code == SELECT_DOCUMENT_REQUEST_CODE) {

            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("document/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);

        } else if (attachment_request_code == SELECT_PICTURE_REQUEST_CODE) {
            for (int i=0;i<attachmentFileList.size();i++)
            {
                RequestBody part =
                        RequestBody.create(
                                MediaType.parse("image/*"),
                                attachmentFileList.get(i)
                        );

                MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(i).getName(), part);
                files.add(attachment);
            }
        }
        else if (attachment_request_code == RECORD_AUDIO_REQUEST_CODE) {
            RequestBody part =
                    RequestBody.create(
                            MediaType.parse("audio/*"),
                            attachmentFileList.get(0)
                    );

            MultipartBody.Part attachment = MultipartBody.Part.createFormData("files", attachmentFileList.get(0).getName(), part);
            files.add(attachment);
        }


        attachment_request_code = 0;
        requestType = REQUEST_UPLOAD_FILE;
        chatViewModel.attachFile(params,files);

    }


    // -------------------------------------------------------------------Access Storage Implementation ----------------------------------------------------------------



    private void openAttachmentDialog()
    {
        if (AttachmentUploadUtils.checkPermission(this)){
            requestPermissions(new String[] {Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        else {
            AttachmentUploadUtils.showPictureDialog(
                    this,
                    new AttachmentUploadListener() {
                        @Override
                        public void onClickGallery() {
                            accessMedia();
                        }

                        @Override
                        public void onClickCamera(Intent intent) {
                            accessCamera();
                        }

                        @Override
                        public void onClickDocument() {
                            accessDocument();
                        }
                    }
            );
        }
    }

    private void accessMedia()
    {
        BottomSheetDialog media_type = new BottomSheetDialog(this);
        media_type.setContentView(R.layout.bottomsheet_select_media_type);

        TextView cancel = media_type.findViewById(R.id.cancel);
        TextView image = media_type.findViewById(R.id.image);
        TextView video = media_type.findViewById(R.id.video);


        cancel.setOnClickListener(view -> media_type.dismiss());

        image.setOnClickListener(view -> {
            accessMediaImage();
            media_type.dismiss();
        });

        video.setOnClickListener(view -> {
            accessMediaVideo();
            media_type.dismiss();
        });

        media_type.show();
    }

    private void accessMediaImage()
    {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        //i.setAction(Intent.ACTION_PICK);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"),  SELECT_PICTURE_REQUEST_CODE);
    }

    private void accessMediaVideo()
    {
        // intent of the type image
        Intent i = new Intent();
        i.setType("video/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i,  SELECT_VIDEO_REQUEST_CODE);
    }

    private void accessCamera()
    {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        singleImageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent cameraIntent = AttachmentUploadUtils.takePhotoFromCamera(this);
        selectedMedia = new File(Objects.requireNonNull(cameraIntent.getStringExtra("image_path")));
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);

    }


    private void accessDocument()
    {

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        resultLauncher.launch(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                openAttachmentDialog();
            }
        } else if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            isAnyFileAttached = true;
            attachment_request_code = SELECT_PICTURE_REQUEST_CODE;

            multipleImageUri = new ArrayList<>();
            attachmentFileList = new ArrayList<>();

            if (data.getClipData() != null) {
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();

                    ImagePreviewModel model = new ImagePreviewModel();
                    model.setUri(imageUri);
                    multipleImageUri.add(i,model);
                    attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri))));
                    Log.d("Uri Data",imageUri.toString());
                }


            }
            else {
                isAnyFileAttached = true;
                Uri imageUri = data.getData();

                ImagePreviewModel model = new ImagePreviewModel();
                model.setUri(imageUri);
                model.setActive(false);
                multipleImageUri.add(model);

                try{
                    attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, imageUri))));
                }
                catch (Exception e)
                {
                    Log.d("Image error",e.toString());
                }
            }

            previewImage();
        }

        else if(requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK )
        {
            isAnyFileAttached = true;
            attachment_request_code = CAPTURE_IMAGE_REQUEST_CODE;

            multipleImageUri = new ArrayList<>();
            attachmentFileList = new ArrayList<>();

            Bitmap bitmap = Utils.decodeFile(selectedMedia);
            Uri selected_camera_uri = Utils.getImageUri(this, bitmap, 1024.0f, 10240.0f);


            attachmentFileList.add(new File(Objects.requireNonNull(FetchPath.getPath(this, selected_camera_uri))));

            ImagePreviewModel model = new ImagePreviewModel();
            model.setUri(selected_camera_uri);
            model.setActive(false);
            multipleImageUri.add(model);

            previewImage();

        }
        else if(requestCode == SELECT_VIDEO_REQUEST_CODE && resultCode == RESULT_OK )
        {
            isAnyFileAttached = true;



            Uri imageUri = data.getData();
            if (imageUri != null ) {
                try {


                    TrimVideo.activity(String.valueOf(imageUri))
                            //.setCompressOption(new CompressOption()) //empty constructor for default compress option
                            .setTrimType(TrimType.DEFAULT)
                            .setHideSeekBar(true)
                            .start(this, startForResult);
                    /*startActivity(new Intent(this, VideoActivity.class)
                            .putExtra("videourl",attachmentFileList.get(0).getAbsolutePath())
                            .putExtra("start_time","0")
                            .putExtra("end_time","0"));*/
                } catch (Exception e) {
                    Log.v("Video Fetch Error",e.toString());
                }

                //Log.d("Video Attachment ",attachmentFileList.get(0).getAbsolutePath());
            }
        }
        else {
            if(requestCode == SELECT_PICTURE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == SELECT_DOCUMENT_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any document", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == SELECT_VIDEO_REQUEST_CODE) {
                Toast.makeText(this, "You haven't picked any video", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
                Toast.makeText(this, "You haven't capture image", Toast.LENGTH_LONG).show();
            }

            attachment_request_code = 0;
        }
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @SuppressLint("WrongConstant")
                @Override
                public void onActivityResult(ActivityResult result) {

                    isAnyFileAttached = true;
                    attachment_request_code = SELECT_DOCUMENT_REQUEST_CODE;
                    attachmentFileList = new ArrayList<>();

                    Intent data = result.getData();

                    if (data != null) {
                        Uri sUri = data.getData();
                        attachmentFileList.add(new File(copyFileToInternalStorage(sUri, "GTF_Document")));


                        binding.attachmentTypeImage.setBackground(getResources().getDrawable(R.drawable.document));
                        binding.attachmentTypeTitle.setText(attachmentFileList.get(0).getName());
                        binding.attachmentContainer.setVisibility(View.VISIBLE);
                    }
                }
            });


    private String copyFileToInternalStorage(Uri uri, String newDirName) {
        Uri returnUri = uri;

        Cursor returnCursor = getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        }, null, null, null);


        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if (!newDirName.equals("")) {
            File dir = new File(getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(getFilesDir() + "/" + newDirName + "/" + name);
        } else {
            output = new File(getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }




    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                attachment_request_code = SELECT_VIDEO_REQUEST_CODE;

                multipleImageUri = new ArrayList<>();
                attachmentFileList = new ArrayList<>();

                try {
                    if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData() != null) {
                        Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                        Log.v("DATAVIDEO", "uri: " + uri);
                        String videoPath = uri.getPath();
                        Log.v("DATAVIDEO", "Path: " + videoPath);

                        Uri videoUri = Uri.fromFile(new File(videoPath));
                        Log.v("DATAVIDEO", "videoUri: " + videoUri);

                        File video = new File(videoPath);

                        long fileSizeInBytes = video.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        Log.v("VIDEOSIZE", "video size in KB:  " + fileSizeInKB);
                        Log.v("VIDEOSIZE", "video size in MB:  " + fileSizeInMB);

                        attachmentFileList.add(video);

                        binding.attachmentTypeImage.setBackground(getResources().getDrawable(R.drawable.autoplay_video));
                        binding.attachmentTypeTitle.setText(attachmentFileList.get(0).getName());
                        binding.attachmentContainer.setVisibility(View.VISIBLE);



                        //responseType = "addVideo";
                        //campaignViewModel.AddVideo(accessToken, videoName, String.valueOf(videoSectionId), video, "1", isMobileRecorded);
                   /* Map<String,Object> header = new HashMap<>();
                    header.put("Content-Type","application/octet-stream;");
                    header.put("accessToken",accessToken);
                    campaignViewModel.DummyAddVideo(header,videoName, String.valueOf(videoSectionId),video,"1");*/
                    }else if (result.getResultCode() == 201){
                        //getVideo();
                    }/*else if (result.getResultCode() == 201){
                        String videoPath = result.getData().getStringExtra("uri");
                        File video = new File(videoPath);
                        responseType = "addVideo";
                        campaignViewModel.AddVideo(accessToken, videoName, String.valueOf(videoSectionId), video, "1", isMobileRecorded);
                    }*/
                } catch (Exception e) {

                }
            });



    // ---------------------------------------------------------------Interface Listeners ----------------------------------------------------------------

    @Override
    public void sendQuotedMessage(View view,String groupChatId,String oldMessage,String username,String time) {

        Log.d("Quote Listener Data ---",groupChatId+"     "+oldMessage+"     "+username+"     "+time);


        isMessageQuoted = true;

        binding.quoteContainer.setVisibility(View.VISIBLE);

        binding.sendMessage.setVisibility(View.VISIBLE);
        binding.recordButton.setVisibility(View.GONE);
        binding.pinAttachment.setVisibility(View.GONE);


        binding.oldMessage.setText(oldMessage);
        binding.oldMsgTime.setText(time);
        binding.oldMsgUser.setText(username);

        groupChatIDRef = groupChatId;

        //scrollToPosition(0);

        binding.type.requestFocus();
        //Utils.softKeyboard(GroupChatsScreen.this,true,binding.type);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void likePost(int userID, int groupChannelId, int gcMemberID, int groupChatId, int like) {
        jsonRawObject = new JSONObject();
        likeGroupChatID = groupChatId;




        try {
            jsonRawObject.put("UserID", userID);
            jsonRawObject.put("GroupChannelID", groupChannelId);
            jsonRawObject.put("GCMemberID", gcMemberID);
            jsonRawObject.put("GroupChatID", groupChatId);
            jsonRawObject.put("isLike", like);

            Log.v("Like Message Params --", jsonRawObject.toString());

            socketInstance.emit("like", jsonRawObject);
        } catch (Exception e) {
            Log.d("Like Exception ------", e.toString());
        }
    }

    @Override
    public void likeAsEmote(int position, ImageView rootView)
    {


        Utils.showDialog(position, this, rootView, reactionModel, new SelectEmoteReaction() {
            @Override
            public void selectEmoteReaction(int id, String emoji_code, String emoji_name) {
                for (int i=0;i<15;i++) {
                    playAnimation(Utils.textAsBitmap(GroupChatScreen.this,emoji_code));
                }
            }
        });
    }



    private void playAnimation(Bitmap resID)
    {
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
        animation.play(this,container);
    }

    @Override
    public void deletePost(int userID, int gcMemberId, int groupChatId, int groupChannelId) {

        try {
            jsonRawObject = new JSONObject();
            jsonRawObject.put("GCMemberID", gcMemberId);
            jsonRawObject.put("GroupChannelID", channelID);
            jsonRawObject.put("GroupChatID", groupChatId);
            jsonRawObject.put("UserID", userID);


            socketInstance.emit("postDelete", jsonRawObject);
        }
        catch (Exception e)
        {
            Log.d("delete post exception",e.toString());
        }


// Todo.......................................................... Implement Delete Listener
/*
        isMessageNotFound = true;
        loadSearchData = false;
        isDataAvailable = true;

        searchedTillPosition = 0;


        rest.ShowDialogue();

        searchMessageInList(String.valueOf(groupChatId));*/
    }

    @Override
    public void forwardMultiplePost(int selectedPostCount,boolean showPostSelection) {

        showPostSelectionCheckBox = showPostSelection;

        if (selectedPostCount <= 0){
            binding.forwardContainer.setVisibility(View.GONE);
        }
        else{
            binding.forwardContainer.setVisibility(View.VISIBLE);
            binding.forwardCount.setText(String.valueOf(selectedPostCount));
        }
    }

    @Override
    public void updateChatList(ArrayList<ChannelRowListDataModel> list) {
        this.list = list;
    }

    @Override
    public void imagePreviewListener(int index,ArrayList<ImagePreviewModel> uriList) {
        selectedImageUriIndex = index;
        binding.imagePreview.setImageURI(uriList.get(index).getUri());
    }

    @Override
    public void searchQuoteMessage(int index,String groupChatId) {

        isMessageNotFound = true;
        loadSearchData = false;
        isDataAvailable = true;

        searchedTillPosition = index;


        //rest.ShowDialogue();
        searchMessageInList(groupChatId,false,false,false);

    }

    @Override
    public void pinMessage(int gcMemberId, int GroupChannelId, int userId, int groupChatId) {
        Map<String,Object> param = new HashMap<>();
        param.put("GCMemberID", gcMemberId);
        param.put("GroupChannelID",GroupChannelId);
        param.put("UserID",String.valueOf(PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0)));
        param.put("GroupChatID",groupChatId);

        Log.d("Pin Message"," Params ----"+param.toString());

        requestType = REQUEST_PIN_MESSAGE;
        chatViewModel.pinMessage(param);
    }


    @Override
    public void updateDummyUser(int dummyUserId, int isAdmin) {

        Log.d("update_dummy_user","true");

        // Getting API data fetch
        rest = new Rest(this, false, true);
        listener = this;


        Map<String,Object> param = new HashMap<>();
        param.put("DummyUserID", dummyUserId);
        param.put("IsAdmin",isAdmin);

        String api_token = PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, "");
        Log.d("api_token",api_token);

        requestType = UPDATE_DUMMY_USER;
        connectViewModel.update_dummy_user_list(channelID,api_token,param);

        Log.d("request_type","2 = "+requestType);
    }



    @Override
    public void onLoading() {
        //rest.ShowDialogue();
    }

    @Override
    public void onDataRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseRender(JsonObject jsonObject) {
        renderResponse(jsonObject);
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
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


    // ------------------------------------------------------------------Required Methods --------------------------------------------------------------------

    private void previewImage()
    {
        binding.pinAttachment.setVisibility(View.GONE);
        binding.sendMessage.setVisibility(View.VISIBLE);
        binding.recordButton.setVisibility(View.GONE);

        ImageMiniPreviewAdapter imageMiniPreviewAdapter= new ImageMiniPreviewAdapter(this,multipleImageUri,this);
        binding.miniImagePreviewRecycler.setHasFixedSize(true);
        binding.miniImagePreviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.miniImagePreviewRecycler.setAdapter(imageMiniPreviewAdapter);

        binding.close.setOnClickListener(view -> {
            binding.imagePreviewLayout.setVisibility(View.GONE);
            isAnyFileAttached = false;
        });

        binding.deleteSelectedImage.setOnClickListener(view -> {

            Log.d("Entered"," 2 true........................");

            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (multipleImageUri.size() == 1){
                multipleImageUri.clear();
                Toast.makeText(this, "You haven't picked any image", Toast.LENGTH_LONG).show();
                imageMiniPreviewAdapter.updateList(multipleImageUri,0);

                binding.pinAttachment.setVisibility(View.VISIBLE);
                binding.sendMessage.setVisibility(View.GONE);
                binding.recordButton.setVisibility(View.VISIBLE);

                binding.imagePreviewLayout.setVisibility(View.GONE);
                isAnyFileAttached = false;
            }
            else {
                if (selectedImageUriIndex == 0) {
                    binding.imagePreview.setImageURI(multipleImageUri.get(selectedImageUriIndex + 1).getUri());
                    multipleImageUri.get(selectedImageUriIndex + 1).setActive(true);

                    multipleImageUri.remove(selectedImageUriIndex);
                    imageMiniPreviewAdapter.updateList(multipleImageUri, selectedImageUriIndex);
                } else {
                    binding.imagePreview.setImageURI(multipleImageUri.get(selectedImageUriIndex - 1).getUri());
                    multipleImageUri.get(selectedImageUriIndex - 1).setActive(true);

                    multipleImageUri.remove(selectedImageUriIndex);
                    imageMiniPreviewAdapter.updateList(multipleImageUri, selectedImageUriIndex - 1);
                }
            }
        });

        binding.imagePreviewLayout.setVisibility(View.VISIBLE);

    }


    private void showBottomSheetPopUp(String message)
    {
        Dialog pin_dialog = new Dialog(this);
        pin_dialog.setContentView(R.layout.dialog_pinned_message);
        pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pin_dialog.setCancelable(false);
        pin_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView text = pin_dialog.findViewById(R.id.title);
        text.setText(message);

        pin_dialog.show();
        new Handler().postDelayed(pin_dialog::dismiss,2000);
    }


    private void renderResponse(JsonObject jsonObject)
    {
        Gson gson = new Gson();

        Log.d("request_type"," = "+requestType);

        if (requestType == UPDATE_DUMMY_USER){

            rest = new Rest(this, false, false);
            listener = this;


        }
        else if (requestType == GET_DUMMY_USER){


            getDummyUserModel = new GetDummyUserModel();
            Type type = new TypeToken<GetDummyUserModel>() {
            }.getType();

            getDummyUserModel = gson.fromJson(jsonObject,type);

            Log.d("dummy_user",jsonObject.toString());

            setDummyUserData();

            rest = new Rest(this, false, false);
            listener = this;
            //appDao = AppDatabase.getInstance(getApplication()).appDao();


            requestType = GET_GROUP_CHANNEL_INFO;
            connectViewModel.get_group_channel_info(channelID,PreferenceConnector.readString(this, PreferenceConnector.API_GTF_TOKEN_, ""));
        }
        else if (requestType == GET_GROUP_CHANNEL_INFO) {
            gson = new Gson();
            Type type = new TypeToken<GroupChannelInfoResponseModel>() {
            }.getType();


            GroupChannelInfoResponseModel groupChannelInfoResponseModel = gson.fromJson(jsonObject, type);

            if (groupChannelInfoResponseModel != null && groupChannelInfoResponseModel.getData() != null) {
                InfoDbEntity data;
                data = groupChannelInfoResponseModel.getData();
                data.setGroupChannelID(channelID);

                databaseViewModel.insertGroupChannelInfo(data);
            }


            requestType = REQUEST_EMOJI_LIST;
            chatViewModel.getEmojiList();
        }
        else if (requestType == REQUEST_PIN_MESSAGE)
        {
            pinMessageCount +=1;
            binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);
            binding.pinnedMessageCount.setText(String.valueOf(pinMessageCount));
            showBottomSheetPopUp(jsonObject.get("message").toString());

        } else if (requestType == REQUEST_EMOJI_LIST) {

            emojiListModel = new EmojiListModel();
            Type type = new TypeToken<EmojiListModel>() {
            }.getType();

            emojiListModel = gson.fromJson(jsonObject, type);


            requestType = PINNED_MESSAGE_COUNT;

            Map<String,Object> param = new HashMap<>();
            param.put("GroupChannelID", PreferenceConnector.readString(this,PreferenceConnector.GC_CHANNEL_ID,"0"));
            param.put("UserID",PreferenceConnector.readInteger(this,PreferenceConnector.CONNECT_USER_ID,0));

            chatViewModel.getPinnedMessages(param);
        }
        else if (requestType == REQUEST_UPLOAD_FILE) {
            Log.d("UPLOAD IMAGE",jsonObject.toString());

            binding.pinAttachment.setVisibility(View.VISIBLE);
            binding.sendMessage.setVisibility(View.GONE);
            binding.recordButton.setVisibility(View.VISIBLE);

            SendAttachmentResponseModel sendAttachmentResponseModel = new SendAttachmentResponseModel();


            Type type = new TypeToken<SendAttachmentResponseModel>() {
            }.getType();

            sendAttachmentResponseModel = gson.fromJson(jsonObject, type);

            mediaIDs = "";
            for (int i=0;i< sendAttachmentResponseModel.getData().size();i++) {
                if (i == sendAttachmentResponseModel.getData().size()-1)
                {
                    mediaIDs += sendAttachmentResponseModel.getData().get(i).getGroupChatMediaID().toString();
                    Log.d("Media IDs","Single loop -----"+mediaIDs);
                }
                else {
                    mediaIDs += sendAttachmentResponseModel.getData().get(i).getGroupChatMediaID().toString() + ",";
                    Log.d("Media IDs","Double loop -----"+mediaIDs);
                }
            }

            Log.d("Media IDs",mediaIDs);

            binding.imagePreviewLayout.setVisibility(View.GONE);
            binding.attachmentContainer.setVisibility(View.GONE);
            isAnyFileAttached = false;
            isAttachmentSend = true;
            validateSendMessage("Audio Test",binding.type);
        }
        else if(requestType == PINNED_MESSAGE_COUNT)
        {
            PinnedMessagesModel pinnedMessagesModel = new PinnedMessagesModel();
            Type type = new TypeToken<PinnedMessagesModel>() {
            }.getType();

            pinnedMessagesModel = gson.fromJson(jsonObject, type);
            if(pinnedMessagesModel.getData()!=null && !pinnedMessagesModel.getData().isEmpty())
            {
                pinMessageCount = pinnedMessagesModel.getData().size();
                binding.pinnedMessageCount.setText(String.valueOf(pinMessageCount));
                binding.pinnedMessageCountContainer.setVisibility(View.VISIBLE);
            }
            else {
                binding.pinnedMessageCountContainer.setVisibility(View.GONE);
                pinMessageCount = 0;
            }
        }
    }




    private void setDummyUserData(){

        String userInitials = "";
        boolean checkFlag = false;

        if (getDummyUserModel != null &&
                getDummyUserModel.getData() != null &&
                getDummyUserModel.getData().getList()!= null &&
                !getDummyUserModel.getData().getList().isEmpty()){


            for (int i=0;i<getDummyUserModel.getData().getList().size();i++){
                if (getDummyUserModel.getData().getList().get(i).getIsAdmin() == 1){
                    userInitials = Utils.getUserInitials(getDummyUserModel.getData().getList().get(i).getFirstname(),getDummyUserModel.getData().getList().get(i).getLastname());
                    checkFlag = true;
                    break;
                }
            }

            if(!checkFlag)
            {
                userInitials = Utils.getUserInitials(getDummyUserModel.getData().getList().get(0).getFirstname(),getDummyUserModel.getData().getList().get(0).getLastname());
            }

            binding.dummyUserInitials.setText(userInitials);
            binding.dummyUserContainer.setVisibility(View.VISIBLE);
        }
    }



    private void destroyListeners()
    {
        // Todo : Remove all ON listeners of socket

        socketInstance.off("userTyping");
        socketInstance.off("messageReceived");
        socketInstance.off("likeUpdate");
        socketInstance.off("postDeleteData");

    }


    //--------------------------------------------------------------------- Android Lifecycle Checks --------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle Check ", "In the onResume() event");


        currentPage = 1;
        loadLocalData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyListeners();
        finish();
    }

    public void onStart()
    {
        super.onStart();
        Log.d("Lifecycle Check ", "In the onStart() event");
    }
    public void onRestart()
    {
        super.onRestart();
        Log.d("Lifecycle Check ", "In the onRestart() event");
    }

    public void onPause()
    {
        super.onPause();
        Log.d("Lifecycle Check ", "In the onPause() event");
    }
    public void onStop()
    {
        super.onStop();
        Log.d("Lifecycle Check ", "In the onStop() event");

        //recyclerViewState = binding.chats.getLayoutManager().onSaveInstanceState();
    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Lifecycle Check ", "In the onDestroy() event");
    }







    // -------------------------------------------------------------- Handling all update from Other Screens -----------------------------------------------------------

    ActivityResultLauncher<Intent> startForActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Constants.SHARE_GIF) {

                    // Todo : Share gif from here
                } else if (result.getResultCode() == Constants.NO_GIF_FOUND) {
                    Utils.showSnackMessage(this,binding.getRoot(),"GIF not available");
                }
            });
}
