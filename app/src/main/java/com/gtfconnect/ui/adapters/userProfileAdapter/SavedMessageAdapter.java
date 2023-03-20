package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentSavedMessageBinding;

public class SavedMessageAdapter extends RecyclerView.Adapter<SavedMessageAdapter.ViewHolder> {

        private int tempItemCount;
        private Context context;

        public  SavedMessageAdapter(Context context,int tempItemCount){
                this.tempItemCount = tempItemCount;
                this.context = context;
        }

        @NonNull
        @Override
        public SavedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new SavedMessageAdapter.ViewHolder(FragmentSavedMessageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }

        @Override
        public void onBindViewHolder(SavedMessageAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
                return tempItemCount;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

                FragmentSavedMessageBinding binding;

                ViewHolder(@NonNull FragmentSavedMessageBinding binding) {
                        super(binding.getRoot());
                        this.binding = binding;

                }
        }
}

