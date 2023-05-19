package com.gtfconnect.ui.adapters.commonGroupChannelAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.databinding.RecyclerReportReasonListItemBinding;
import com.gtfconnect.models.authResponseModels.CityData;
import com.gtfconnect.models.commonGroupChannelResponseModels.MemberReportReasonResponseModel;
import com.gtfconnect.ui.adapters.authModuleAdapter.CityListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MemberReportReasonListAdapter extends RecyclerView.Adapter<MemberReportReasonListAdapter.ViewHolder> {

    private List<MemberReportReasonResponseModel.ReasonList> reasonLists;
    private Context context;

    private MemberReportReasonListAdapter.OnRecyclerViewItemClickListener mListener;

    private int selectedIndex = -1;

    public  MemberReportReasonListAdapter(Context context, List<MemberReportReasonResponseModel.ReasonList> reasonLists){

        this.reasonLists = reasonLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberReportReasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MemberReportReasonListAdapter.ViewHolder(RecyclerReportReasonListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MemberReportReasonListAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;


        holder.binding.reason.setChecked(reasonLists.get(itemPosition).isReasonSelected());
        selectedIndex = itemPosition;



        holder.binding.reason.setText(reasonLists.get(itemPosition).getReasonText());


        holder.binding.reason.setOnClickListener(view -> {

            for (int i=0;i<reasonLists.size();i++){
                if (i != itemPosition){
                    reasonLists.get(i).setReasonSelected(false);
                }
                else{
                    reasonLists.get(i).setReasonSelected(true);
                }

                mListener.onItemAccept(reasonLists.get(itemPosition).getReportReasonID(),reasonLists.get(itemPosition).getReasonText(),reasonLists.get(itemPosition).getReasonCode());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasonLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerReportReasonListItemBinding binding;

        ViewHolder(@NonNull RecyclerReportReasonListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setOnRecyclerViewItemClickListener(MemberReportReasonListAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemAccept(int id,String reason,String reasonCode);
    }

    public void updateList(List<MemberReportReasonResponseModel.ReasonList> reasonLists){
        reasonLists = reasonLists;
        notifyDataSetChanged();
    }


    public boolean isReasonSelected(){

        boolean isAnyChecked = false;
        for (int i=0; i< reasonLists.size();i++){
            if (reasonLists.get(i).isReasonSelected()){
                isAnyChecked = true;
                break;
            }
            else{
                isAnyChecked = false;
            }
        }

        return isAnyChecked;
    }
}

