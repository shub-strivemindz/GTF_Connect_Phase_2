package com.gtfconnect.ui.adapters.authModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.models.CountryData;
import com.gtfconnect.models.StateData;

import java.util.ArrayList;
import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.ViewHolder> {

    private List<StateData> stateDataList;
    private Context context;

    private StateListAdapter.OnRecyclerViewItemClickListener mListener;

    public  StateListAdapter(Context context, List<StateData> stateDataList){

        this.stateDataList = new ArrayList<>();

        this.stateDataList = stateDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public StateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new StateListAdapter.ViewHolder(FragmentPlaceSelelctorItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(StateListAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;

        holder.binding.place.setText(stateDataList.get(itemPosition).getStateName());
        holder.binding.place.setOnClickListener(view -> mListener.onItemAccept(stateDataList.get(itemPosition).getStateID(),stateDataList.get(itemPosition).getStateName()));
    }

    @Override
    public int getItemCount() {
        return stateDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPlaceSelelctorItemBinding binding;

        ViewHolder(@NonNull FragmentPlaceSelelctorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setOnRecyclerViewItemClickListener(StateListAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemAccept(int id,String item_name);

    }

    public void updateList(List<StateData> list){
        stateDataList = list;
        notifyDataSetChanged();
    }
}

