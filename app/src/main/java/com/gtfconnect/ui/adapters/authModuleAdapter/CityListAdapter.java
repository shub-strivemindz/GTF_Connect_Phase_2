package com.gtfconnect.ui.adapters.authModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.models.authResponseModels.CityData;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<CityData> cityDataList;
    private Context context;

    private CityListAdapter.OnRecyclerViewItemClickListener mListener;

    public  CityListAdapter(Context context, List<CityData> cityDataList){

        this.cityDataList = new ArrayList<>();

        this.cityDataList = cityDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CityListAdapter.ViewHolder(FragmentPlaceSelelctorItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CityListAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;

        holder.binding.place.setText(cityDataList.get(itemPosition).getCityName());
        holder.binding.place.setOnClickListener(view -> mListener.onItemAccept(cityDataList.get(itemPosition).getCityID(),cityDataList.get(itemPosition).getCityName()));
    }

    @Override
    public int getItemCount() {
        return cityDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPlaceSelelctorItemBinding binding;

        ViewHolder(@NonNull FragmentPlaceSelelctorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setOnRecyclerViewItemClickListener(CityListAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemAccept(int id,String item_name);

    }

    public void updateList(List<CityData> list){
        cityDataList = list;
        notifyDataSetChanged();
    }
}

