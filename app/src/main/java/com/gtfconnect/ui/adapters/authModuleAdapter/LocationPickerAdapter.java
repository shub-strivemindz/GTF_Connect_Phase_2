package com.gtfconnect.ui.adapters.authModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.models.authResponseModels.CountryData;
import com.gtfconnect.models.authResponseModels.LocationPickerModel;

import java.util.ArrayList;
import java.util.List;

public class LocationPickerAdapter extends RecyclerView.Adapter<LocationPickerAdapter.ViewHolder> {

    private List<LocationPickerModel> locationPickerModelList;
    private Context context;

    private LocationPickerModel selectedLocation;

    private int itemSelected = -1;

    public  LocationPickerAdapter(Context context, List<LocationPickerModel> locationPickerModelList){

        this.locationPickerModelList = locationPickerModelList;
        this.context = context;

        selectedLocation = new LocationPickerModel();
    }

    @NonNull
    @Override
    public LocationPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new LocationPickerAdapter.ViewHolder(FragmentPlaceSelelctorItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(LocationPickerAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;

        holder.binding.place.setText(locationPickerModelList.get(itemPosition).getPlace());

        if (itemPosition == itemSelected){
            holder.binding.getRoot().setBackgroundColor(context.getColor(R.color.theme_green));

            selectedLocation = new LocationPickerModel();
            selectedLocation.setPlace(locationPickerModelList.get(itemPosition).getPlace());
            selectedLocation.setId(locationPickerModelList.get(itemPosition).getId());
            selectedLocation.setPhoneCode(locationPickerModelList.get(itemPosition).getPhoneCode());
        }
        else{
            holder.binding.getRoot().setBackgroundColor(context.getColor(R.color.locationPickerBg));
        }

        holder.binding.getRoot().setOnClickListener(v -> {

            itemSelected = itemPosition;
            notifyDataSetChanged();
        });

        //holder.binding.place.setOnClickListener(view -> mListener.onItemAccept(locationPickerModelList.get(itemPosition).getId(), locationPickerModelList.get(itemPosition).getPlace(),locationPickerModelList.get(itemPosition).getPhoneCode()));
    }

    @Override
    public int getItemCount() {
        return locationPickerModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPlaceSelelctorItemBinding binding;

        ViewHolder(@NonNull FragmentPlaceSelelctorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public LocationPickerModel getLocation()
    {
        return selectedLocation;
    }
}

