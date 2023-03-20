package com.gtfconnect.ui.adapters.mentorModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gtfconnect.databinding.FragmentMentorForwardedMessageRecievedBinding;
import com.gtfconnect.databinding.FragmentMentorForwardedMessageSentBinding;
import com.gtfconnect.databinding.FragmentMentorMessageRecievedBinding;
import com.gtfconnect.databinding.FragmentMentorMessageSentBinding;
import com.gtfconnect.databinding.FragmentMentorSingleImageReceivedBinding;

import java.util.List;

public class MentorChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MESSAGE_SENT = 0;
    private static final int MESSAGE_RECEIVED = 1;
    private static final int QUOTE = 2;
    private static final int FORWARDED_MESSAGE = 3;
    private static final int IMAGE_RECEIVED = 4;
    private static final int IMAGE_SENT = 5;
    private static final int DOCUMENT_SENT = 6;
    private static final int DOCUMENT_RECEIVED = 7;

    private int tempItemCount;
    private Context context;
    private List<Integer> message_type;
    private List<Integer> message_status;

    public  MentorChatAdapter(Context context,int tempItemCount,List<Integer> message_type,List<Integer> message_status){

        this.tempItemCount = tempItemCount;
        this.context= context;
        this.message_type=message_type;
        this.message_status = message_status;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MESSAGE_RECEIVED)
            return new MessageReceivedViewHolder(FragmentMentorMessageRecievedBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        else if (viewType == MESSAGE_SENT)
            return new MessageSentViewHolder(FragmentMentorMessageSentBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        else if (viewType == FORWARDED_MESSAGE)
            return new ForwardedViewHolder(FragmentMentorForwardedMessageRecievedBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        else if (viewType == QUOTE)
            return new QuoteViewHolder(FragmentMentorForwardedMessageSentBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        else if (viewType == IMAGE_RECEIVED)
            return new ImageReceivedViewHolder(FragmentMentorSingleImageReceivedBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));

        return new MessageReceivedViewHolder(FragmentMentorMessageRecievedBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempItemCount;
    }

    static class MessageReceivedViewHolder extends RecyclerView.ViewHolder {
        FragmentMentorMessageRecievedBinding binding;
        MessageReceivedViewHolder(@NonNull FragmentMentorMessageRecievedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class MessageSentViewHolder extends RecyclerView.ViewHolder {
        FragmentMentorMessageSentBinding binding;
        MessageSentViewHolder(@NonNull FragmentMentorMessageSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ForwardedViewHolder extends RecyclerView.ViewHolder {
        FragmentMentorForwardedMessageRecievedBinding binding;
        ForwardedViewHolder(@NonNull FragmentMentorForwardedMessageRecievedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        FragmentMentorForwardedMessageSentBinding binding;
        QuoteViewHolder(@NonNull FragmentMentorForwardedMessageSentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    static class ImageReceivedViewHolder extends RecyclerView.ViewHolder {
        FragmentMentorSingleImageReceivedBinding binding;
        ImageReceivedViewHolder(@NonNull FragmentMentorSingleImageReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
/*
    static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentCommentSectionBinding binding;
        ViewHolder(@NonNull FragmentCommentSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentCommentSectionBinding binding;
        ViewHolder(@NonNull FragmentCommentSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        FragmentCommentSectionBinding binding;
        ViewHolder(@NonNull FragmentCommentSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }*/

    @Override
    public int getItemViewType(int position) {

        // For message received condition
        if(message_status.get(position) == 0)
        {
            switch (message_type.get(position))
            {

                // For message type
                case 0:
                    return MESSAGE_RECEIVED;

                // For forwarded type
                case 1:
                    return FORWARDED_MESSAGE;
                // For Image type
                case 2:
                    return IMAGE_RECEIVED;
                // For Document type
                case 3:
                    return DOCUMENT_RECEIVED;
            }
        }
        else {
            switch (message_type.get(position))
            {

                // For message type
                case 0:
                    return MESSAGE_SENT;

                // For forwarded type
                case 1:
                    return QUOTE;
                // For Image type
                case 2:
                    return IMAGE_SENT;
                // For Document type
                case 3:
                    return DOCUMENT_SENT;
            }
        }
        return super.getItemViewType(position);
    }
}

