package com.gtfconnect.ui.adapters.userProfileAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentSavedMessageBinding;
import com.gtfconnect.databinding.RecyclerGroupChatBinding;
import com.gtfconnect.models.savedMessageModels.SavedMessageResponseModel;

public class SavedMessageAdapter extends RecyclerView.Adapter<SavedMessageAdapter.ViewHolder> {

        private SavedMessageResponseModel savedMessageResponseModel;
        private Context context;

        public  SavedMessageAdapter(Context context, SavedMessageResponseModel savedMessageResponseModel){
                this.savedMessageResponseModel = savedMessageResponseModel;
                this.context = context;
        }

        @NonNull
        @Override
        public SavedMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new SavedMessageAdapter.ViewHolder(RecyclerGroupChatBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }

        @Override
        public void onBindViewHolder(SavedMessageAdapter.ViewHolder holder, int position) {


        }

        @Override
        public int getItemCount() {
                return savedMessageResponseModel.getData().getList().size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

                RecyclerGroupChatBinding binding;

                ViewHolder(@NonNull RecyclerGroupChatBinding binding) {
                        super(binding.getRoot());
                        this.binding = binding;

                }
        }
}

