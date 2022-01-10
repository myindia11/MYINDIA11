package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Common;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AvailableTeamsSwitchAdapter extends RecyclerView.Adapter<AvailableTeamsSwitchAdapter.ViewHolder> {
    public Context context;
    private List<PlayerValuesModel> playerValuesModelList;
    DatabaseReference databaseReference;
    SendData sendData;

    public AvailableTeamsSwitchAdapter(Context context, List<PlayerValuesModel> playerValuesModelList, SendData sendData) {
        this.context = context;
        this.playerValuesModelList = playerValuesModelList;
        this.sendData = sendData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_team_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerValuesModel playerValuesModel = playerValuesModelList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("JoinedTeamIds").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(playerValuesModelList.get(position).getTeamCode())){
                            holder.select.setVisibility(View.GONE);
                            holder.btnBack.setVisibility(View.GONE);
                        }else {
                            holder.select.setVisibility(View.VISIBLE);
                            holder.btnBack.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.teamId.setText("Team-"+playerValuesModel.getTeamCode());
        holder.capName.setText(playerValuesModel.getCaptain());
        holder.vcapName.setText(playerValuesModel.getViceCaptain());
        holder.wicket.setText("WK "+playerValuesModel.getWK());
        holder.bat.setText("BT "+playerValuesModel.getBT());
        holder.bowl.setText("BL "+playerValuesModel.getBR());
        holder.allRounder.setText("AR "+playerValuesModel.getAR());
        holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.select.setBackgroundColor(Color.parseColor("#e97005"));
                    sendData.sendTeamData(playerValuesModelList.get(position));
                    Common.swTc = playerValuesModelList.get(position).getTeamCode();
                    Common.creditLeft = playerValuesModelList.get(position).getLeftCredit();
                    Common.wk = playerValuesModelList.get(position).getWK();
                    Common.bt = playerValuesModelList.get(position).getBT();
                    Common.br = playerValuesModelList.get(position).getBR();
                    Common.ar = playerValuesModelList.get(position).getAR();
                    Common.tOneC = playerValuesModelList.get(position).getTeamOneCount();
                    Common.tTwoC = playerValuesModelList.get(position).getTeamTwoCount();
                    Common.tOne = playerValuesModelList.get(position).getTeamOne();
                    Common.tTwo = playerValuesModelList.get(position).getTeamTwo();
                    Common.c = playerValuesModelList.get(position).getCaptain();
                    Common.vc = playerValuesModelList.get(position).getViceCaptain();
                }else {
                    holder.select.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return playerValuesModelList.size();
    }
    public interface SendData {
        void sendTeamData(PlayerValuesModel playerValuesModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamId, capName,vcapName, wicket, bat, bowl, allRounder;
        ToggleButton select;
        LinearLayout layout,btnBack;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamId = itemView.findViewById(R.id.team_id);
            capName = itemView.findViewById(R.id.cap_name);
            vcapName = itemView.findViewById(R.id.vcap_name);
            wicket = itemView.findViewById(R.id.wicket_count);
            bat = itemView.findViewById(R.id.bat_count);
            bowl = itemView.findViewById(R.id.bowl_count);
            allRounder = itemView.findViewById(R.id.ar_count);
            select = itemView.findViewById(R.id.select);
            layout = itemView.findViewById(R.id.back);
            btnBack = itemView.findViewById(R.id.btnLay);
        }
    }
}
