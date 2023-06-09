package com.gtfconnect.ui.adapters.channelModuleAdapter.profileAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentUserSettingBinding;
import com.gtfconnect.interfaces.ChannelSettingListener;
import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;
import com.gtfconnect.ui.screenUI.commonGroupChannelModule.BlocklistScreen;
import com.gtfconnect.utilities.Constants;
import com.shawnlin.numberpicker.NumberPicker;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    Context context;

    InfoDbEntity profileDetailModel;

    private boolean isChannelDescriptionClicked = false;

    private int accessType = 0, discussion = 0, signMessage = 0,manipulateView = 0,manipulateViewPercent=10, viewChatHistory = 0, manageReaction = 0;

    private String viewType;

    private ChannelSettingListener listener;

    private boolean isAdmin;

    private boolean enable_disable_discussion;

    public  SettingAdapter(Context context,InfoDbEntity profileDetailModel, boolean isAdmin,boolean enable_disable_discussion,ChannelSettingListener listener,String viewType){
        this.context = context;
        this.profileDetailModel = profileDetailModel;

        this.isAdmin = isAdmin;
        this.enable_disable_discussion = enable_disable_discussion;

        this.listener = listener;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new SettingAdapter.ViewHolder(FragmentUserSettingBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SettingAdapter.ViewHolder holder, int position) {


        checkUserAdminPermission(holder,isAdmin);


        if (viewType.equalsIgnoreCase(Constants.Group_Channel_TYPE_1)){
            holder.binding.discussionContainer.setVisibility(View.GONE);
            holder.binding.signMessageContainer.setVisibility(View.GONE);
            holder.binding.viewChatHistoryContainer.setVisibility(View.GONE);
            holder.binding.manipulateContainer.setVisibility(View.GONE);
        }


        holder.binding.channelDescription.setOnClickListener(view -> {
            if(isChannelDescriptionClicked){
                //This will shrink textview to 2 lines if it is expanded.
                holder.binding.channelDescription.setMaxLines(6);
                isChannelDescriptionClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                holder.binding.channelDescription.setMaxLines(Integer.MAX_VALUE);
                isChannelDescriptionClicked = true;
            }
        });



        if (profileDetailModel != null && profileDetailModel != null) {
            if (profileDetailModel.getGcInfo() != null) {

                if (profileDetailModel.getGcInfo().getDescription() != null) {
                    holder.binding.channelDescription.setText(profileDetailModel.getGcInfo().getDescription());
                }

                if(profileDetailModel.getGcInfo().getAccessType() != null && !profileDetailModel.getGcInfo().getAccessType().isEmpty()){
                    if (profileDetailModel.getGcInfo().getAccessType().equalsIgnoreCase("public")){
                        holder.binding.privateChannelSwitch.setChecked(true);
                        holder.binding.privateChannelSwitchText.setText("On");
                        accessType = 1;
                    }
                    else{
                        holder.binding.privateChannelSwitch.setChecked(false);
                        holder.binding.privateChannelSwitchText.setText("Off");
                        accessType = 0;
                    }
                }

            }


            if (profileDetailModel.getGcSetting() != null) {
                if (profileDetailModel.getGcSetting().getAllowDiscussion() != null) {
                    if (profileDetailModel.getGcSetting().getAllowDiscussion() == 1) {
                        holder.binding.discussionSwitch.setChecked(true);
                        holder.binding.discussionSwitchText.setText("On");
                        discussion = 1;
                    } else {
                        holder.binding.discussionSwitch.setChecked(false);
                        holder.binding.discussionSwitchText.setText("Off");
                        discussion = 0;
                    }
                }


                if(profileDetailModel.getGcSetting().getEnableReactions() != null){
                    if (profileDetailModel.getGcSetting().getEnableReactions() == 0){
                        holder.binding.manageReactionSwitch.setChecked(false);
                        holder.binding.manageReactionSwitchText.setText("Off");
                        Log.d("Reaction_Status",profileDetailModel.getGcSetting().getEnableReactions().toString());
                        manageReaction = 0;
                    }
                    else{
                        holder.binding.manageReactionSwitch.setChecked(true);
                        holder.binding.manageReactionSwitchText.setText("On");
                        Log.d("Reaction_Status",profileDetailModel.getGcSetting().getEnableReactions().toString());
                        manageReaction = 1;
                    }
                }


                if (profileDetailModel.getGcSetting().getEnableManipulateViews() != null){
                    if(profileDetailModel.getGcSetting().getEnableManipulateViews() == 1) {

                        manipulateView = 1;

                        if (profileDetailModel.getGcSetting().getManipulateViewsPercent() != null) {
                            if (profileDetailModel.getGcSetting().getManipulateViewsPercent() == 0) {
                                holder.binding.manipulateViewSwitch.setChecked(false);
                                holder.binding.manipulateViewSwitchText.setText("Off");

                                manipulateViewPercent = 0;
                            } else {
                                holder.binding.manipulateViewSwitch.setChecked(true);
                                holder.binding.manipulateViewSwitchText.setText("On");

                                manipulateView = profileDetailModel.getGcSetting().getManipulateViewsPercent();
                            }
                        }
                    }
                    else{
                        manipulateView = 0;
                        manipulateViewPercent = 10;
                    }
                }
            }
        }






        holder.binding.manageReaction.setOnClickListener(view -> {
            if ((profileDetailModel.getGcSetting().getEnableReactions() != null && profileDetailModel.getGcSetting().getEnableReactions() == 1) || manageReaction == 1){
                listener.callManageReactionsClass(1);
            }
        });

        holder.binding.manageReactionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    manageReaction = 1;
                    holder.binding.manageReactionSwitchText.setText("On");


                   listener.callManageReactionsClass(profileDetailModel.getGcSetting().getEnableReactions());
                }
                else{
                    manageReaction = 0;
                    holder.binding.manageReactionSwitchText.setText("Off");
                }
            }
        });





        holder.binding.managePermission.setOnClickListener(view -> listener.callPermissionClass());


        holder.binding.editChannelDescription.setOnClickListener(view -> {
            holder.binding.editChannelDescription.setVisibility(View.GONE);
            holder.binding.updateChannelDescription.setVisibility(View.VISIBLE);
            holder.binding.channelDescription.setEnabled(true);
        });

        holder.binding.updateChannelDescription.setOnClickListener(view -> {
            holder.binding.updateChannelDescription.setVisibility(View.GONE);
            holder.binding.editChannelDescription.setVisibility(View.VISIBLE);
            holder.binding.channelDescription.setEnabled(false);
        });

        holder.binding.blocklist.setOnClickListener(view -> context.startActivity(new Intent(context, BlocklistScreen.class)));


        holder.binding.privateChannelSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                accessType = 1;
                listener.updateAccessTypeStatus(accessType);
                holder.binding.privateChannelSwitchText.setText("On");
            }
            else {
                accessType = 0;
                listener.updateAccessTypeStatus(accessType);
                holder.binding.privateChannelSwitchText.setText("Off");
            }
        });

        holder.binding.discussionSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                discussion = 1;
                listener.updateDiscussionStatus(discussion);
                holder.binding.discussionSwitchText.setText("On");
            }
            else {
                discussion = 0;
                listener.updateDiscussionStatus(discussion);
                holder.binding.discussionSwitchText.setText("Off");
            }
        });

        holder.binding.signMessageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                signMessage = 1;
                listener.updateSignMessageStatus(signMessage);
                holder.binding.signMessageSwitchText.setText("On");
            }
            else {
                signMessage = 0;
                listener.updateSignMessageStatus(signMessage);
                holder.binding.signMessageSwitchText.setText("Off");
            }
        });

        holder.binding.viewChatHistorySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                viewChatHistory = 1;
                listener.updateViewChatHistoryStatus(viewChatHistory);
                holder.binding.viewChatHistorySwitchText.setText("On");
            }
            else {
                viewChatHistory = 0;
                listener.updateViewChatHistoryStatus(viewChatHistory);
                holder.binding.viewChatHistorySwitchText.setText("Off");
            }
        });


        holder.binding.manipulateViewContainer.setOnClickListener(view -> {
            if (profileDetailModel.getGcSetting().getEnableManipulateViews() == 1){
                showManipulateViewDialog();
            }
        });


        holder.binding.manipulateViewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.binding.manipulateViewSwitchText.setText("On");
                    showManipulateViewDialog();
                }
                else{
                    holder.binding.manipulateViewSwitchText.setText("Off");
                    listener.updateManipulateViewsStatus(0,10);
                }
            }
        });

        holder.binding.deleteChannel.setOnClickListener(view -> {

            Dialog delete_dialog = new Dialog(context);

            delete_dialog.setContentView(R.layout.dialog_delete_channel);
            delete_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            delete_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView delete = (TextView) delete_dialog.findViewById(R.id.delete);
            TextView cancel = (TextView) delete_dialog.findViewById(R.id.cancel);

            delete.setOnClickListener(view1 -> delete_dialog.dismiss());
            cancel.setOnClickListener(view1 -> delete_dialog.dismiss());

            delete_dialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentUserSettingBinding binding;

        ViewHolder(@NonNull FragmentUserSettingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public void updateData(InfoDbEntity profileDetailModel){
        this.profileDetailModel = profileDetailModel;
        notifyDataSetChanged();
    }



    private void showManipulateViewDialog(){
        Dialog manipulate_view_dialog = new Dialog(context);

        manipulate_view_dialog.setContentView(R.layout.dialog_manipulate_view);
        manipulate_view_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        manipulate_view_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        SwitchCompat viewSwitch = manipulate_view_dialog.findViewById(R.id.manipulate_view_switch);
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b)
                {
                    listener.updateManipulateViewsStatus(0,10);
                    manipulate_view_dialog.dismiss();
                }
            }
        });



        NumberPicker numberPicker = (NumberPicker) manipulate_view_dialog.findViewById(R.id.manipulate_view_percent);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9);
        numberPicker.setDisplayedValues(new String[]{"10","20","30","40","50","60","70","80","90","100"});
        numberPicker.setValue((manipulateViewPercent/10)-1);


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal){
                    case 0:
                        manipulateViewPercent = 10;
                        break;
                    case 1:
                        manipulateViewPercent = 20;
                        break;
                    case 2:
                        manipulateViewPercent = 30;
                        break;
                    case 3:
                        manipulateViewPercent = 40;
                        break;
                    case 4:
                        manipulateViewPercent = 50;
                        break;
                    case 5:
                        manipulateViewPercent = 60;
                        break;
                    case 6:
                        manipulateViewPercent = 70;
                        break;
                    case 7:
                        manipulateViewPercent = 80;
                        break;
                    case 8:
                        manipulateViewPercent = 90;
                        break;
                    case 9:
                    default:
                        manipulateViewPercent = 100;
                        break;
                }
            }
        });

        manipulate_view_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                if (manipulateViewPercent != profileDetailModel.getGcSetting().getManipulateViewsPercent()){
                    listener.updateManipulateViewsStatus(1,manipulateViewPercent);
                }
            }
        });

        manipulate_view_dialog.show();
    }





    // -------------------------------------------------------------- Check User & Admin Permissions -----------------------------------------------------------

    private void checkUserAdminPermission(ViewHolder holder,boolean isAdmin){

        if (!isAdmin) {

            holder.binding.container1.setVisibility(View.GONE);
            holder.binding.container2.setVisibility(View.GONE);
            holder.binding.blocklist.setVisibility(View.GONE);
            holder.binding.signMessageContainer.setVisibility(View.GONE);
            holder.binding.container3.setVisibility(View.GONE);
            holder.binding.viewChatHistoryContainer.setVisibility(View.GONE);
            holder.binding.deleteChannel.setVisibility(View.GONE);

            if (enable_disable_discussion){

                holder.binding.container1.setVisibility(View.VISIBLE);
                holder.binding.privateChannelContainer.setVisibility(View.GONE);
                holder.binding.managePermission.setVisibility(View.GONE);

                holder.binding.discussionContainer.setVisibility(View.VISIBLE);
            }
            else{
                holder.binding.container1.setVisibility(View.GONE);
            }

        }
        else {

            holder.binding.container1.setVisibility(View.VISIBLE);
            holder.binding.container2.setVisibility(View.VISIBLE);
            holder.binding.blocklist.setVisibility(View.VISIBLE);
            holder.binding.signMessageContainer.setVisibility(View.VISIBLE);
            holder.binding.container3.setVisibility(View.VISIBLE);
            holder.binding.viewChatHistoryContainer.setVisibility(View.VISIBLE);
            holder.binding.deleteChannel.setVisibility(View.VISIBLE);


            if (enable_disable_discussion){
                holder.binding.discussionContainer.setVisibility(View.VISIBLE);
            }
            else{
                holder.binding.discussionContainer.setVisibility(View.GONE);
            }
        }
    }



    public void updateUserType(boolean isAdmin){
        this.isAdmin = isAdmin;
        notifyItemChanged(0);
    }

    public void updateEnableDisableDiscussion(boolean enable_disable_discussion){
        this.enable_disable_discussion = enable_disable_discussion;
        notifyItemChanged(0);
    }
}

