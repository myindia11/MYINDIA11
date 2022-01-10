package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;

import java.util.List;

public class AlreadyJoinedTeamsAdapter extends RecyclerView.Adapter<AlreadyJoinedTeamsAdapter.ViewHolder> {
    public Context context;
    private List<PlayerValuesModel> playerValuesModelList;
    String tCode,tOneCount,tTwoCount;

    public AlreadyJoinedTeamsAdapter(Context context, List<PlayerValuesModel> playerValuesModelList) {
        this.context = context;
        this.playerValuesModelList = playerValuesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.already_joined_teams_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerValuesModel playerValuesModel = playerValuesModelList.get(position);
        holder.teamId.setText("Team-"+playerValuesModel.getTeamCode());
        holder.capName.setText(playerValuesModel.getCaptain());
        holder.vcapName.setText(playerValuesModel.getViceCaptain());
        holder.wicket.setText("WK "+playerValuesModel.getWK());
        holder.bat.setText("BT "+playerValuesModel.getBT());
        holder.bowl.setText("BL "+playerValuesModel.getBR());
        holder.allRounder.setText("AR "+playerValuesModel.getAR());

    }

    @Override
    public int getItemCount() {
        return playerValuesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamId, capName,vcapName, wicket, bat, bowl,allRounder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamId = itemView.findViewById(R.id.team_id);
            capName = itemView.findViewById(R.id.cap_name);
            vcapName = itemView.findViewById(R.id.vcap_name);
            wicket = itemView.findViewById(R.id.wicket_count);
            bat = itemView.findViewById(R.id.bat_count);
            bowl = itemView.findViewById(R.id.bowl_count);
            allRounder = itemView.findViewById(R.id.ar_count);
        }
    }
}
