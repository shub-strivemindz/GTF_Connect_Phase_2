package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.RecyclerSavedMessageListBinding;
import com.gtfconnect.interfaces.SavedMessageListener;
import com.gtfconnect.models.savedMessageModels.SavedMessageResponseModel;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.MultiPreviewScreen;
import com.gtfconnect.utilities.PreferenceConnector;
import com.gtfconnect.utilities.Utils;

import java.util.List;

public class SavedMessageAdapter extends RecyclerView.Adapter<SavedMessageAdapter.ViewHolder> {

        private List<SavedMessageResponseModel.ListData> list;
        private Context context;


        private boolean isMessageClicked = false;

        String userName = "";
        String message = "";
        String time = "";

        String post_base_url= "";

        private String messageTime = "";

        private int messageUserID;

        private boolean isDummyUser = true;

        private String userID;

        private SavedMessageListener listener;

        public  SavedMessageAdapter(Context context, List<SavedMessageResponseModel.ListData> list, String userID, SavedMessageListener listener){
                this.list = list;
                this.context = context;
                this.userID = userID;

                this.listener = listener;
        }

        @NonNull
        @Override
        public SavedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new SavedMessageAdapter.ViewHolder(RecyclerSavedMessageListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }

        @Override
        public void onBindViewHolder(SavedMessageAdapter.ViewHolder holder, int position) {

                if (list.get(position) != null) {

                        // ---------------------------------------------------------- To Find If message date of chat. --------------------------------------------
                        if (list.get(position) != null) {
                                if (list.get(position).getUser() != null) {
                                        if (list.get(position).getUser().getUserID() != null) {

                                                messageTime = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());


                                                if (position + 1 < list.size()) {
                                                        if (list.get(position + 1) != null) {
                                                                if (list.get(position + 1).getUser() != null) {
                                                                        if (list.get(position + 1).getUser().getUserID() != null) {
                                                                                String nextMessageTime = Utils.getSavedMessageChipDate(list.get(position+1).getUpdatedAt());

                                                                                if (messageTime.equalsIgnoreCase(nextMessageTime)) {
                                                                                        holder.binding.dateChipContainer.setVisibility(View.GONE);
                                                                                } else {
                                                                                        holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                                                                                        holder.binding.currentDate.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                                                                                        messageTime = "";
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                } else if (position+1 == list.size()) {
                                                        holder.binding.dateChipContainer.setVisibility(View.VISIBLE);
                                                        holder.binding.currentDate.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                                                        messageTime = "";
                                                }

                                        }
                                }
                        }

                        if (userID.equalsIgnoreCase(String.valueOf(list.get(position).getUserID()))){
                                sentMessageView(holder,position);
                        }
                        else{
                                receivedMessageView(holder,position);
                        }
                }
        }


        private void sentMessageView(SavedMessageAdapter.ViewHolder holder, int position){

                holder.binding.sentMessageContainer.setVisibility(View.VISIBLE);
                holder.binding.receivedMessageContainer.setVisibility(View.GONE);


                if (list.get(position).getUser() != null) {
                        if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                                userName = "Bot";
                                holder.binding.userName.setText("Bot");
                        } else {

                                Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                                int userId= list.get(position).getUser().getUserID();
                                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                                        holder.binding.userName.setText("You");
                                }
                                else {
                                        userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                                        holder.binding.userName.setText(userName);
                                }
                        }
                }



                if (list.get(position).getSavedMessageMedia() !=null && !list.get(position).getSavedMessageMedia().isEmpty()) {

                        holder.binding.postImageContainer1.setVisibility(View.VISIBLE);
                    /*holder.binding.mediaRecycler1.setVisibility(View.VISIBLE);

                    GroupChannel_MediaAdapter mediaAdapter = new GroupChannel_MediaAdapter(context,holder.binding.mediaRecycler, list.get(position).getSavedMessageMedia(), post_base_url,String.valueOf(userID));
                    holder.binding.mediaRecycler1.setHasFixedSize(true);
                    holder.binding.mediaRecycler1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    holder.binding.mediaRecycler1.setAdapter(mediaAdapter);*/

                        //holder.binding.postImageContainer.setVisibility(View.VISIBLE);

                        loadSentPostMedia(holder, position, list.get(position).getSavedMessageMedia().size());
                }
                else{
                        //holder.binding.mediaRecycler1.setVisibility(View.GONE);
                        holder.binding.postImageContainer1.setVisibility(View.GONE);
                }

       /*     // Todo : Uncomment below code once get thumbnail for the video and remove below line -----------------
            loadPostMedia(holder, position, list.get(position).getSavedMessageMedia().size());
            *//*if (Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType()).equalsIgnoreCase("video")) {
                String post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();
                holder.binding.postImageContainer.setVisibility(View.GONE);
                loadVideoFile(post_path);
        }
            else {
                loadPostMedia(holder, position, list.get(position).getSavedMessageMedia().size());
            }*//*
        }
        else{
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }*/





                if (list.get(position).getContent() != null) {
                        message = list.get(position).getContent();
                        holder.binding.message1.setText(list.get(position).getContent());
                } else {
                        message = "No message found";
                        holder.binding.message1.setText("No message found");
                }

                if (list.get(position).getCreatedAt() != null) {
                        time = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                        holder.binding.time.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                } else {
                        holder.binding.time.setText("XX/XX/XXXX");
                }


                holder.binding.message1.setOnClickListener(view -> {
                        if(isMessageClicked){
                                //This will shrink textview to 2 lines if it is expanded.
                                holder.binding.message1.setMaxLines(3);
                                isMessageClicked = false;
                        } else {
                                //This will expand the textview if it is of 2 lines
                                holder.binding.message1.setMaxLines(Integer.MAX_VALUE);
                                isMessageClicked = true;
                        }
                });


                holder.binding.sentMessageContainer.setOnLongClickListener(view -> {
                        Log.d("Not going ","why2");
                        loadBottomSheet(holder,position,true);

                        return false;
                });




                holder.binding.postImageContainer1.setOnClickListener(view -> {

                        Gson gson1  = new Gson();
                        String mediaData =  gson1.toJson(list.get(position).getSavedMessageMedia());

                        Intent intent = new Intent(context, MultiPreviewScreen.class);
                        intent.putExtra("mediaList",mediaData);
                        intent.putExtra("base_url",post_base_url);

                        String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
                        intent.putExtra("title",title);

                        context.startActivity(intent);
                });


        }


        private void receivedMessageView(SavedMessageAdapter.ViewHolder holder, int position){
                holder.binding.sentMessageContainer.setVisibility(View.GONE);
                holder.binding.receivedMessageContainer.setVisibility(View.VISIBLE);


                holder.binding.receivedMessageContainer.setOnLongClickListener(view -> {
                        Log.d("Not going ","why");
                        loadBottomSheet(holder,position,false);
                        return false;
                });




                Gson gson = new Gson();
                String data = gson.toJson(list.get(position));

                if (list.get(position).getUser() != null) {
                        if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                                userName = "Bot";
                                holder.binding.userName.setText("Bot");
                        } else {

                                Log.d("USER_ID_MATCHING",String.valueOf(PreferenceConnector.readInteger(context,PreferenceConnector.GTF_USER_ID,0))+" "+String.valueOf(list.get(position).getUser().getUserID()));

                                int userId= list.get(position).getUser().getUserID();
                                if (PreferenceConnector.readInteger(context,PreferenceConnector.CONNECT_USER_ID,0) == userId){
                                        holder.binding.userName.setText("You");
                                }
                                else {
                                        userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                                        holder.binding.userName.setText(userName);
                                }
                        }
                }




                if (list.get(position) != null) {
                        if (list.get(position).getUser() != null) {
                                if (list.get(position).getUser().getUserID() != null) {
                                        messageUserID = list.get(position).getUser().getUserID();


                                        if (position + 1 < list.size()) {
                                                if (list.get(position + 1) != null) {
                                                        if (list.get(position + 1).getUser() != null) {
                                                                if (list.get(position + 1).getUser().getUserID() != null) {
                                                                        int nextUserID = list.get(position + 1).getUser().getUserID();
                                                                        if (messageUserID == nextUserID) {
                                                    /*holder.binding.highWhiteSpacer.setVisibility(View.GONE);
                                                    holder.binding.lowWhiteSpacer.setVisibility(View.VISIBLE);*/
                                                                                holder.binding.userContainer.setVisibility(View.GONE);
                                                                        } else {
                                                    /*holder.binding.highWhiteSpacer.setVisibility(View.VISIBLE);
                                                    holder.binding.lowWhiteSpacer.setVisibility(View.GONE);*/
                                                                                holder.binding.userContainer.setVisibility(View.VISIBLE);
                                                                                messageUserID = 0;
                                                                        }
                                                                }
                                                        }
                                                }
                                        }

                                }
                        }
                }






                if (list.get(position).getSavedMessageMedia() !=null && !list.get(position).getSavedMessageMedia().isEmpty()) {

                        //holder.binding.mediaRecycler.setVisibility(View.VISIBLE);
                        holder.binding.postImageContainer.setVisibility(View.VISIBLE);
                        holder.binding.greenHighlightDivider.setVisibility(View.VISIBLE);

                    /*GroupChannel_MediaAdapter mediaAdapter = new GroupChannel_MediaAdapter(context,holder.binding.mediaRecycler, list.get(position).getSavedMessageMedia(), post_base_url,String.valueOf(userID));
                    holder.binding.mediaRecycler.setHasFixedSize(true);
                    holder.binding.mediaRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    holder.binding.mediaRecycler.setAdapter(mediaAdapter);*/


                        loadReceivePostMedia(holder, position, list.get(position).getSavedMessageMedia().size());
                        //holder.binding.postImageContainer.setVisibility(View.VISIBLE);
                }
                else{
                        //holder.binding.mediaRecycler.setVisibility(View.GONE);
                        holder.binding.greenHighlightDivider.setVisibility(View.GONE);
                        holder.binding.postImageContainer.setVisibility(View.GONE);
                }

                if (list.get(position).getContent() != null) {
                        message = list.get(position).getContent();
                        holder.binding.message.setText(list.get(position).getContent());
                } else {
                        message = "No message found";
                        holder.binding.message.setText("No message found");
                }

                if (list.get(position).getCreatedAt() != null) {
                        time = Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt());
                        holder.binding.time.setText(Utils.getSavedMessageChipDate(list.get(position).getUpdatedAt()));
                } else {
                        holder.binding.time.setText("XX/XX/XXXX");
                }


                holder.binding.message.setOnClickListener(view -> {
                        if(isMessageClicked){
                                //This will shrink textview to 2 lines if it is expanded.
                                holder.binding.message.setMaxLines(3);
                                isMessageClicked = false;
                        } else {
                                //This will expand the textview if it is of 2 lines
                                holder.binding.message.setMaxLines(Integer.MAX_VALUE);
                                isMessageClicked = true;
                        }
                });


                /*holder.binding.postImageContainer.setOnClickListener(view -> {

                        Gson gson1  = new Gson();
                        String mediaData =  gson1.toJson(list.get(position).getMedia());

                        Intent intent = new Intent(context, MultiPreviewImage.class);
                        intent.putExtra("mediaList",mediaData);
                        intent.putExtra("base_url",post_base_url);

                        String title = list.get(position).getUser().getFirstname()+" "+list.get(position).getUser().getLastname();
                        intent.putExtra("title",title);

                        context.startActivity(intent);
                });*/

        }






        private void loadBottomSheet(SavedMessageAdapter.ViewHolder holder, int position, boolean isSentMessage)
        {
                BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
                chat_options_dialog.setContentView(R.layout.bottomsheet_saved_message_options);


                TextView copy = chat_options_dialog.findViewById(R.id.copy);
                TextView remove = chat_options_dialog.findViewById(R.id.remove);
                TextView cancel = chat_options_dialog.findViewById(R.id.cancel);

                copy.setOnClickListener(view -> {
                        chat_options_dialog.dismiss();

                        Dialog copy_dialog = new Dialog(context);

                        copy_dialog.setContentView(R.layout.dialog_message_copy);
                        copy_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        copy_dialog.setCancelable(false);
                        copy_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        copy_dialog.show();
                        new Handler().postDelayed(copy_dialog::dismiss,1000);
                });


                remove.setOnClickListener(view ->
                {
                        chat_options_dialog.dismiss();

                        Dialog delete_post_dialog = new Dialog(context);
                        delete_post_dialog.setContentView(R.layout.dialog_delete_post);
                        delete_post_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        delete_post_dialog.setCancelable(false);
                        delete_post_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        delete_post_dialog.show();

                        TextView delete_post = delete_post_dialog.findViewById(R.id.delete);
                        TextView cancel_post = delete_post_dialog.findViewById(R.id.cancel);

                        delete_post.setOnClickListener(view1 -> {
                                listener.deleteSavedPost(list.get(position).getSavedMessageID());
                                delete_post_dialog.dismiss();
                        });

                        cancel_post.setOnClickListener(view1 -> delete_post_dialog.dismiss());

                });

                cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

                chat_options_dialog.show();
        }



        private void loadSentPostMedia(SavedMessageAdapter.ViewHolder holder, int index, int media_count)
        {


                String fileType= "";

                String post_path = "";


                switch (media_count) {
                        case 1:

                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();
                                Log.d("Entered_POst",post_base_url);
                                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                                holder.binding.multiPostImageContainer1.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer1.setVisibility(View.VISIBLE);



                                if (fileType.equalsIgnoreCase("image"))
                                {

                                        Log.d("Post Main Url", post_path);

                                        holder.binding.playVideo01.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.postImage);
                                }
                                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                                        holder.binding.playVideo01.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.postImage);
                                } else if (fileType.equalsIgnoreCase("video")) {

                                        holder.binding.playVideo01.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.postImage);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }
                                break;

                        case 2:
                                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                                holder.binding.multiPostImageContainer1.setVisibility(View.GONE);
                                holder.binding.dualPostImageContainer1.setVisibility(View.VISIBLE);



                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo11.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.dualPost11);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo11.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.dualPost11);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo11.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.dualPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo21.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.dualPost21);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo21.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.dualPost21);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo21.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.dualPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }

                                break;
                        case 3:
                                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                                holder.binding.additionalImageCount1.setVisibility(View.GONE);

                                holder.binding.multiPostImageContainer1.setVisibility(View.VISIBLE);




                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo31.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost11);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo31.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost11);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo31.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo41.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost21);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo41.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost21);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo41.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(2).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo51.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost31);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo51.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost31);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo51.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost3);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }




                    /*post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();
                    Log.d("Post1 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost1);
                    post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();
                    Log.d("Post2 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost2);
                    post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();
                    Log.d("Post3 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost3);*/

                                break;

                        default:
                                holder.binding.dualPostImageContainer1.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer1.setVisibility(View.GONE);
                                holder.binding.additionalImageCount1.setVisibility(View.VISIBLE);

                                holder.binding.additionalImageCount1.setText("+ " + (media_count - 3));

                                holder.binding.multiPostImageContainer1.setVisibility(View.VISIBLE);


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo31.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost11);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo31.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost11);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo31.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo41.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost21);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo41.setVisibility(View.GONE);
                                        //loadDocumentFile(post_path,holder.binding.multiPost2);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo41.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(2).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo51.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost31);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo51.setVisibility(View.GONE);
                                        //loadDocumentFile(post_path,holder.binding.multiPost3);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo51.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost3);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }
                }
        }



        private void loadReceivePostMedia(SavedMessageAdapter.ViewHolder holder, int index, int media_count)
        {


                String fileType= "";

                String post_path = "";


                switch (media_count) {
                        case 1:

                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();
                                Log.d("Entered_POst",post_base_url);
                                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                                holder.binding.multiPostImageContainer.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer.setVisibility(View.VISIBLE);



                                if (fileType.equalsIgnoreCase("image"))
                                {

                                        Log.d("Post Main Url", post_path);

                                        holder.binding.playVideo.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.postImage);
                                }
                                else if (fileType.equalsIgnoreCase("document") || fileType.equalsIgnoreCase("application")) {

                                        holder.binding.playVideo.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.postImage);
                                } else if (fileType.equalsIgnoreCase("video")) {

                                        holder.binding.playVideo.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.postImage);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }
                                break;

                        case 2:
                                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                                holder.binding.multiPostImageContainer.setVisibility(View.GONE);
                                holder.binding.dualPostImageContainer.setVisibility(View.VISIBLE);



                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo1.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.dualPost1);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo1.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.dualPost1);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo1.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.dualPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo2.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.dualPost2);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo2.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.dualPost2);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo2.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.dualPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }

                                break;
                        case 3:
                                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                                holder.binding.additionalImageCount.setVisibility(View.GONE);

                                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);




                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo3.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost1);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo3.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost1);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo3.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo4.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost2);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo4.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost2);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo4.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(2).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo5.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost3);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo5.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost3);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo5.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost3);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }




                    /*post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();
                    Log.d("Post1 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost1);
                    post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();
                    Log.d("Post2 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost2);
                    post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();
                    Log.d("Post3 Main Url", post_path);
                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost3);*/

                                break;

                        default:
                                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                                holder.binding.additionalImageCount.setVisibility(View.VISIBLE);

                                holder.binding.additionalImageCount.setText("+ " + (media_count - 3));

                                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(0).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(0).getStoragePath() + list.get(index).getSavedMessageMedia().get(0).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo3.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost1);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo3.setVisibility(View.GONE);
                                        loadDocumentFile(post_path,holder.binding.multiPost1);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo3.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost1);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(1).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(1).getStoragePath() + list.get(index).getSavedMessageMedia().get(1).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo4.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost2);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo4.setVisibility(View.GONE);
                                        //loadDocumentFile(post_path,holder.binding.multiPost2);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo4.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost2);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }


                                fileType = Utils.checkFileType(list.get(index).getSavedMessageMedia().get(2).getMimeType());
                                post_path = post_base_url + list.get(index).getSavedMessageMedia().get(2).getStoragePath() + list.get(index).getSavedMessageMedia().get(2).getFileName();

                                if (fileType.equalsIgnoreCase("image"))
                                {
                                        Log.d("Post Main Url", post_path);
                                        holder.binding.playVideo5.setVisibility(View.GONE);
                                        loadImageFile(post_path,holder.binding.multiPost3);
                                }
                                else if (fileType.equalsIgnoreCase("document")) {
                                        holder.binding.playVideo5.setVisibility(View.GONE);
                                        //loadDocumentFile(post_path,holder.binding.multiPost3);
                                } else if (fileType.equalsIgnoreCase("video")) {
                                        holder.binding.playVideo5.setVisibility(View.VISIBLE);
                                        //loadVideoFile(post_path,holder.binding.multiPost3);
                                }
                                else{
                                        Log.d("File_Type_Error",fileType);
                                }
                }
        }




        private void loadImageFile(String imageFilePath, ImageView imageView)
        {
                //Setting up loader on post
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(circularProgressDrawable);
                requestOptions.error(R.drawable.image_not_found);
                requestOptions.skipMemoryCache(true);
                requestOptions.fitCenter();

                Glide.with(context).load(imageFilePath).
                        fitCenter().apply(requestOptions).
                        diskCacheStrategy(DiskCacheStrategy.ALL).
                        transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        }

        private void loadVideoFile(String videoFilePath, ImageView imageView)
        {
        /*context.startActivity(new Intent(context, VideoActivity.class)
                .putExtra("videourl",videoFilePath)
                .putExtra("start_time","0")
                .putExtra("end_time","0"));*/
        }


        private void loadDocumentFile(String docFilePath, ImageView imageView)
        {

        }









        @Override
        public int getItemCount() {
                return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

                RecyclerSavedMessageListBinding binding;

                ViewHolder(@NonNull RecyclerSavedMessageListBinding binding) {
                        super(binding.getRoot());
                        this.binding = binding;

                }
        }
}

