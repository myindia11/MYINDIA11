package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.GroupChatModel;
import com.example.india11.R;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    public Context context;
    private List<GroupChatModel> groupChatModelList;

    public GroupChatAdapter(Context context, List<GroupChatModel> groupChatModelList) {
        this.context = context;
        this.groupChatModelList = groupChatModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_message_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupChatModel groupChatModel = groupChatModelList.get(position);
        holder.sender.setText(groupChatModel.getSender());
        holder.time.setText(groupChatModel.getTime());
        holder.message.setText(groupChatModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return groupChatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sender, time, message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.senderName);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.message);
        }
    }
}
