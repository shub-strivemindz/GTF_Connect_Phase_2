package com.gtfconnect.ui.adapters.authModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.models.CountryData;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private List<CountryData> countryList;
    private Context context;

    private OnRecyclerViewItemClickListener mListener;

    public  CountryListAdapter(Context context, List<CountryData> countryList){

        this.countryList = new ArrayList<>();

        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CountryListAdapter.ViewHolder(FragmentPlaceSelelctorItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CountryListAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;

        holder.binding.place.setText(countryList.get(itemPosition).getCountryName());
        holder.binding.place.setOnClickListener(view -> mListener.onItemAccept(countryList.get(itemPosition).getCountryID(), countryList.get(itemPosition).getCountryName(),countryList.get(itemPosition).getPhoneCode()));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        FragmentPlaceSelelctorItemBinding binding;

        ViewHolder(@NonNull FragmentPlaceSelelctorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemAccept(int position,String item_name,int phoneCode);

    }

    public void updateList(List<CountryData> list){
        countryList = list;
        notifyDataSetChanged();
    }
}

