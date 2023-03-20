package com.gtfconnect.ui.adapters.channelModuleAdapter;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentCommentSectionBinding;
import com.gtfconnect.interfaces.ChannelChatListener;
import com.gtfconnect.interfaces.GroupChatListener;
import com.gtfconnect.models.channelResponseModel.ChannelChatResponseModel;
import com.gtfconnect.models.groupResponseModel.GroupChatResponseModel;
import com.gtfconnect.ui.adapters.groupChatAdapter.GroupChatAdapter;
import com.gtfconnect.ui.screenUI.groupModule.GroupCommentScreen;
import com.gtfconnect.utilities.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChannelChatAdapter extends RecyclerView.Adapter<ChannelChatAdapter.ViewHolder> {

    private ArrayList<ChannelChatResponseModel.Row> list;
    private Context context;

    private ChannelChatResponseModel.Row item;

    private JSONObject jsonRawObject;

    private String userID;

    private ChannelChatListener channelChatListener;

    String userName = "";
    String message = "";
    String time = "";

    String post_base_url= "";

    public ChannelChatAdapter(Context context, ArrayList<ChannelChatResponseModel.Row> list, String userID, String post_base_url, ChannelChatListener channelChatListener) {
        this.list = list;
        this.context = context;
        this.userID = userID;
        this.channelChatListener = channelChatListener;
        this.post_base_url = post_base_url;
    }



    public void updateList(ArrayList<ChannelChatResponseModel.Row> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChannelChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ChannelChatAdapter.ViewHolder(FragmentCommentSectionBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ChannelChatAdapter.ViewHolder holder, int index) {

        final int position = index;


        Gson gson = new Gson();
        String data = gson.toJson(list.get(position));

        if (list.get(position).getUser() != null) {
            if (list.get(position).getUser().getFirstname() == null && list.get(position).getUser().getLastname() == null) {
                userName = "Bot";
                holder.binding.firstName.setText("Bot");
            } else {

                userName = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
                holder.binding.firstName.setText(list.get(position).getUser().getFirstname());
                holder.binding.lastName.setText(list.get(position).getUser().getLastname());
            }
        }

        if (list.get(position).getGroupChatRefID() != null) {
            holder.binding.quoteContainer.setVisibility(View.VISIBLE);
            holder.binding.message.setVisibility(View.GONE);

            if (list.get(position).getQuote() != null) {
                if (list.get(position).getQuote().getMessage() != null) {
                    if (String.valueOf(list.get(position).getUserID()).equalsIgnoreCase(userID)) {

                        holder.binding.quoteContainer.setBackgroundColor(context.getColor(R.color.theme_green));

                        holder.binding.quoteDivider.setDividerColor(context.getColor(R.color.white));
                        holder.binding.title.setTextColor(context.getColor(R.color.white));
                        holder.binding.quoteIcon.setColorFilter(context.getColor(R.color.white));
                        holder.binding.newMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMessage.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgUser.setTextColor(context.getColor(R.color.white));
                        holder.binding.oldMsgTime.setTextColor(context.getColor(R.color.white));
                        holder.binding.smallQuoteDivider.setDividerColor(context.getColor(R.color.white));
                    }
                }
                holder.binding.oldMessage.setText(list.get(position).getQuote().getMessage());

                String username = list.get(position).getQuote().getUser().getFirstname() + " " + list.get(position).getQuote().getUser().getLastname();
                holder.binding.oldMsgUser.setText(username);

                holder.binding.oldMsgTime.setText(Utils.getDisplayableTime(list.get(position).getQuote().getCreatedAt()));

                holder.binding.newMessage.setText(list.get(position).getMessage());

            }
        } else {
            holder.binding.message.setVisibility(View.VISIBLE);
            holder.binding.quoteContainer.setVisibility(View.GONE);
        }

        if (list.get(position).getMedia() !=null && !list.get(position).getMedia().isEmpty())
        {
            holder.binding.postImageContainer.setVisibility(View.VISIBLE);
            loadPostMedia(holder,position,list.get(position).getMedia().size());
        }
        else{
            holder.binding.postImageContainer.setVisibility(View.GONE);
        }

        if (list.get(position).getMessage() != null) {
            message = list.get(position).getMessage();
            holder.binding.message.setText(list.get(position).getMessage());
        } else {
            message = "No message found";
            holder.binding.message.setText("No message found");
        }

        if (list.get(position).getCreatedAt() != null) {
            time = Utils.getDisplayableTime(list.get(position).getCreatedAt());
            holder.binding.time.setText(Utils.getDisplayableTime(list.get(position).getCreatedAt()));
        } else {
            holder.binding.time.setText("XX/XX/XXXX");
        }

        if (list.get(position).getCommentData() == null) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        } else if (list.get(position).getCommentData().size() == 0) {
            holder.binding.commentContainer.setVisibility(View.GONE);
        } else {
            holder.binding.commentContainer.setVisibility(View.VISIBLE);
            holder.binding.commentCount.setText("(" + list.get(position).getCommentData().size() + ")");
        }



        if (list.get(position).getLike() != null && list.get(position).getLike().size() != 0) {
            if (String.valueOf(list.get(position).getLike().get(0).getUserID()).equalsIgnoreCase(userID) && list.get(position).getLike().get(0).getIsLike() == 1) {
                holder.binding.like.setColorFilter(context.getColor(R.color.theme_green));
            }
            else {
                holder.binding.like.setColorFilter(context.getColor(R.color.tab_grey));
            }
        }

        holder.binding.viewComment.setOnClickListener(view -> {

            Intent intent = new Intent(context, GroupCommentScreen.class);
            intent.putExtra("userDetail", data);
            intent.putExtra("userID", userID);

            Log.d("Sending user ID ---", userID);
            context.startActivity(intent);
        });



        holder.binding.quoteMsgContainer.setOnClickListener(view -> {
            if (list.get(position).getQuote() != null) {
                channelChatListener.searchQuoteMessage(position,list.get(position).getQuote().getGroupChatID());
            }
        });



        // Reply into the Chat
        holder.binding.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GroupCommentScreen.class);
                intent.putExtra("replyOnComment", true);
                intent.putExtra("userDetail", data);
                intent.putExtra("userID", userID);
                context.startActivity(intent);
                    /*InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
            }
        });

        // Bottom-sheet for chat options --
        holder.binding.bottomsheetChatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadBottomsheet(holder,position);
            }
        });

        // Bottom-sheet for image options --
        holder.binding.postImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
                chat_options_dialog.setContentView(R.layout.bottomsheet_post_action_options2);
                chat_options_dialog.show();
            }
        });

        holder.binding.like.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                channelChatListener.likeAsEmote(position,holder.binding.bottomsheetChatOption);
                return false;
            }
        });

        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).getLike() != null)
                {
                    if (list.get(position).getLike().size() != 0)
                    {
                        if (list.get(position).getLike().get(0).getIsLike() == 0)
                        {
                            channelChatListener.likePost(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    1);
                        }
                        else{
                            channelChatListener.likePost(Integer.parseInt(userID),
                                    list.get(position).getGroupChannelID(),
                                    list.get(position).getGCMemberID(),
                                    Integer.parseInt(list.get(position).getGroupChatID()),
                                    0);
                        }
                    }
                    else {
                        channelChatListener.likePost(Integer.parseInt(userID),
                                list.get(position).getGroupChannelID(),
                                list.get(position).getGCMemberID(),
                                Integer.parseInt(list.get(position).getGroupChatID()),
                                1);
                    }
                }
                else {
                    channelChatListener.likePost(Integer.parseInt(userID),
                            list.get(position).getGroupChannelID(),
                            list.get(position).getGCMemberID(),
                            Integer.parseInt(list.get(position).getGroupChatID()),
                            1);
                }
            }
        });
    }


    private void loadBottomsheet(ChannelChatAdapter.ViewHolder holder, int position)
    {
        BottomSheetDialog chat_options_dialog = new BottomSheetDialog(context);
        chat_options_dialog.setContentView(R.layout.bottomsheet_group_chat_actions);

        TextView pin = chat_options_dialog.findViewById(R.id.pin);
        TextView quote = chat_options_dialog.findViewById(R.id.quote);
        TextView copy = chat_options_dialog.findViewById(R.id.copy);
        TextView cancel = chat_options_dialog.findViewById(R.id.cancel);

        pin.setOnClickListener(view -> {
            chat_options_dialog.dismiss();


            Integer groupChatId = Integer.parseInt(list.get(position).getGroupChatID());
            channelChatListener.pinMessage(list.get(position).getGCMemberID(),list.get(position).getGroupChannelID(),list.get(position).getUserID(),groupChatId);
        });

        quote.setOnClickListener(view -> {
            String name = list.get(position).getUser().getFirstname() + " " + list.get(position).getUser().getLastname();
            channelChatListener.sendQuotedMessage(holder.binding.getRoot(), list.get(position).getGroupChatID(), list.get(position).getMessage(), name, time);
            chat_options_dialog.dismiss();
        });


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

        cancel.setOnClickListener(view -> chat_options_dialog.dismiss());

        chat_options_dialog.show();
    }




    private void loadPostMedia(ChannelChatAdapter.ViewHolder holder, int index, int image_count)
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


        String post_path = "";


        switch (image_count) {
            case 1:

                String fileType = Utils.checkFileType(list.get(index).getMedia().get(0).getMimeType());

                if (fileType.equalsIgnoreCase("image"))
                {
                    holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                    holder.binding.multiPostImageContainer.setVisibility(View.GONE);

                    holder.binding.singlePostImageContainer.setVisibility(View.VISIBLE);


                    post_path += post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                    Log.d("Post Main Url", post_path);

                    Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.postImage);
                }
                else if (fileType.equalsIgnoreCase("video")) {
                    // Todo ======= Remove
                    holder.binding.postImageContainer.setVisibility(View.GONE);
                    loadVideoFile();
                }
                else if (fileType.equalsIgnoreCase("pdf")) {
                    // Todo ======= Remove
                    holder.binding.postImageContainer.setVisibility(View.GONE);
                    loadDocumentFile();
                }
                break;

            case 2:
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.multiPostImageContainer.setVisibility(View.GONE);

                holder.binding.dualPostImageContainer.setVisibility(View.VISIBLE);


                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                Log.d("Post1 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.dualPost1);

                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();
                Log.d("Post2 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.dualPost2);

                break;
            case 3:
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.additionalImageCount.setVisibility(View.GONE);

                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);


                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                Log.d("Post1 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost1);

                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();
                Log.d("Post2 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost2);

                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();
                Log.d("Post3 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost3);

                break;

            default:
                holder.binding.dualPostImageContainer.setVisibility(View.GONE);
                holder.binding.singlePostImageContainer.setVisibility(View.GONE);
                holder.binding.additionalImageCount.setVisibility(View.VISIBLE);

                holder.binding.additionalImageCount.setText("+ " + (image_count - 3));

                holder.binding.multiPostImageContainer.setVisibility(View.VISIBLE);


                post_path = post_base_url + list.get(index).getMedia().get(0).getStoragePath() + list.get(index).getMedia().get(0).getFileName();
                Log.d("Post1 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost1);

                post_path = post_base_url + list.get(index).getMedia().get(1).getStoragePath() + list.get(index).getMedia().get(1).getFileName();
                Log.d("Post2 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost2);

                post_path = post_base_url + list.get(index).getMedia().get(2).getStoragePath() + list.get(index).getMedia().get(2).getFileName();
                Log.d("Post3 Main Url", post_path);
                Glide.with(context).load(post_path).fitCenter().apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.multiPost3);
        }
    }


    public void loadVideoFile()
    {

    }


    public void loadDocumentFile()
    {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentCommentSectionBinding binding;

        ViewHolder(@NonNull FragmentCommentSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

