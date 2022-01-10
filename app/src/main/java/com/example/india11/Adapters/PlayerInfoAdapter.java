package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.R;

import java.util.List;

public class PlayerInfoAdapter extends RecyclerView.Adapter<PlayerInfoAdapter.ViewHolder> {
    public Context context;
    private List<SelectedPlayersModel> selectedPlayersModelList;

    public PlayerInfoAdapter(Context context, List<SelectedPlayersModel> selectedPlayersModelList) {
        this.context = context;
        this.selectedPlayersModelList = selectedPlayersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_info_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectedPlayersModel selectedPlayersModel = selectedPlayersModelList.get(position);
        holder.playerName.setText(selectedPlayersModel.getPlayerName());
        holder.score.setText(String.valueOf(selectedPlayersModel.getObtainedPoints()));
        holder.credit.setText(String.valueOf(selectedPlayersModel.getCreditScore()));
        Glide.with(context).load(selectedPlayersModel.getPlayerPic()).into(holder.playerPic);
        if (selectedPlayersModel.getTeamName().equals(Common.teamOneName)){
            Glide.with(context).load(Common.teamOneLogo).into(holder.flag);
        }
        if (selectedPlayersModel.getTeamName().equals(Common.teamTwoName)){
            Glide.with(context).load(Common.teamTwoLogo).into(holder.flag);
        }
        if (selectedPlayersModel.getPlayerStatus().equals("In lineup")){
            holder.status.setImageResource(R.drawable.check_mark);
        }
        if (selectedPlayersModel.getPlayerStatus().equals("Not in lineup")){
            holder.status.setImageResource(R.drawable.close_icon);
        }

    }

    @Override
    public int getItemCount() {
        return selectedPlayersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playerPic, flag, status;
        TextView playerName, credit, score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerPic = itemView.findViewById(R.id.player_img);
            flag = itemView.findViewById(R.id.flag);
            status = itemView.findViewById(R.id.status);
            playerName = itemView.findViewById(R.id.player_name);
            credit = itemView.findViewById(R.id.player_credit);
            score = itemView.findViewById(R.id.score);
        }
    }
}
