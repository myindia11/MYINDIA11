package com.example.india11.Adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.ChatModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<ChatModel> list;
    ChatModel chatModel;
    String current= FirebaseAuth.getInstance().getCurrentUser().getUid();



    public MessageAdapter(List<ChatModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_item,parent,false);
        return new ViewHolder(view);
    }
    private String toDate(long timestamp) {

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        return sfd.format(new Date(timestamp));

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        chatModel =list.get(position);
        String from_user= chatModel.getFrom();


        long timeStamp= Long.parseLong(chatModel.getChatTimeStamp().get("timeStamp").toString());
        String currTimeStamp=toDate(timeStamp);

        holder.textDateInCenter.setText(currTimeStamp);
        holder.linearLayout.setOrientation(LinearLayout.VERTICAL);
        if (from_user.equals(current))
        {
            holder.linearLayout.setGravity(Gravity.END);
            holder.inner_linear_layout.setBackgroundResource(R.drawable.textback);
        }
        else
        {
            holder.linearLayout.setGravity(Gravity.START);
            holder.inner_linear_layout.setBackgroundResource(R.drawable.textbackk);

        }
        holder.textView.setTextColor(Color.BLACK);
        holder.textView.setText(chatModel.getMessage());
        Date date = new Date(timeStamp);
        SimpleDateFormat sfd = new SimpleDateFormat(" HH:mm ",Locale.getDefault());
        String text = sfd.format(date);
        holder.textDate.setText(text);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView,textDate,textDateInCenter;
        public LinearLayout linearLayout,inner_linear_layout;

        public ViewHolder( View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_message_content);
            linearLayout=itemView.findViewById(R.id.linearl);
            textDate=itemView.findViewById(R.id.chat_text_date);
            inner_linear_layout=itemView.findViewById(R.id.inner_linear_layout);
            textDateInCenter=itemView.findViewById(R.id.chat_date_in_middle);

        }
    }
}