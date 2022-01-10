package com.example.india11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.NotificationModel;
import com.example.india11.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationModel notificationModel = notificationModelList.get(position);
        holder.notificationHeading.setText(notificationModel.getHeading());
        holder.notificationBody.setText(notificationModel.getBody());
        holder.notificationDate.setText(notificationModel.getDate());

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationBody,notificationDate,notificationHeading;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDate = itemView.findViewById(R.id.notiDate);
            notificationBody = itemView.findViewById(R.id.notiContent);
            notificationHeading = itemView.findViewById(R.id.notiTitle);
        }
    }
}
