package com.gtfconnect.ui.adapters.authModuleAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.R;
import com.gtfconnect.databinding.FragmentPlaceSelelctorItemBinding;
import com.gtfconnect.databinding.RecyclerHowDidYouFindUsBinding;
import com.gtfconnect.models.authResponseModels.CountryData;
import com.gtfconnect.models.authResponseModels.LocationPickerModel;

import java.util.ArrayList;
import java.util.List;

public class HowDidYouFindUsAdapter extends RecyclerView.Adapter<HowDidYouFindUsAdapter.ViewHolder> {

    private int itemSelected = -1;
    private Context context;

    private String[] findUsList = {"Telegram","Facebook","Youtube","Instagram","Friends","Google Search","Quora","Others"};

    private ArrayList<Drawable> drawableArrayList;

    private String selectedText = "";


    public  HowDidYouFindUsAdapter(Context context){
        this.context = context;
        setDrawableArrayList();
    }

    @NonNull
    @Override
    public HowDidYouFindUsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new HowDidYouFindUsAdapter.ViewHolder(RecyclerHowDidYouFindUsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(HowDidYouFindUsAdapter.ViewHolder holder, int position) {

        final int itemPosition = position;

        holder.binding.icon.setImageDrawable(drawableArrayList.get(itemPosition));
        holder.binding.title.setText(findUsList[itemPosition]);


        if (itemPosition == itemSelected){
            holder.binding.getRoot().setBackgroundColor(context.getColor(R.color.theme_green));
            holder.binding.title.setTextColor(context.getColor(R.color.white));
            holder.binding.icon.setColorFilter(context.getColor(R.color.white));

            selectedText = findUsList[itemPosition];
        }
        else{

            holder.binding.getRoot().setBackgroundColor(context.getColor(R.color.how_did_you_find_us));
            holder.binding.title.setTextColor(context.getColor(R.color.authEditText));
            holder.binding.icon.setColorFilter(context.getColor(R.color.theme_green));

        }


        holder.binding.getRoot().setOnClickListener(v -> {

            if (itemSelected == itemPosition){
                itemSelected = -1;
                selectedText = "";
            }
            else{
                itemSelected = itemPosition;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerHowDidYouFindUsBinding binding;

        ViewHolder(@NonNull RecyclerHowDidYouFindUsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




    public String getSelectedValue(){
        return selectedText;
    }





    private void setDrawableArrayList(){
        drawableArrayList = new ArrayList<>();

        drawableArrayList.add(context.getDrawable(R.drawable.telegram));
        drawableArrayList.add(context.getDrawable(R.drawable.facebook));
        drawableArrayList.add(context.getDrawable(R.drawable.youtube));
        drawableArrayList.add(context.getDrawable(R.drawable.instgram));
        drawableArrayList.add(context.getDrawable(R.drawable.friends));
        drawableArrayList.add(context.getDrawable(R.drawable.google));
        drawableArrayList.add(context.getDrawable(R.drawable.quora));
        drawableArrayList.add(context.getDrawable(R.drawable.others));

    }
}

