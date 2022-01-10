package com.example.india11.CandVCAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.CVCModels.AllrounderModel;
import com.example.india11.Common;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.R;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CVCAllAdapter extends RecyclerView.Adapter<CVCAllAdapter.ViewHolder> {
    public Context context;
    private List<AllrounderModel> allrounderModelList;
    int wicket, bat, bowl, ar, tmOneCount, tmTwoCount;
    Integer WK,BT,AR,BR,teamOneCount,teamTwoCount;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5,databaseReference6;
    String uid;
    FirebaseAuth firebaseAuth;
    double Credit;
    String teamCode;
    Double leftCredit;
    String teamName, playerName, pid, playerStatus,playerRole,playerPic;
    double creditScore, obtainedPoints;
    Boolean isSelected;
    String teamOne, teamTwo, captain, viceCaptain,cap, vcap, scap, svcap;
    String captan, vicecaptan;

    public CVCAllAdapter(Context context, List<AllrounderModel> allrounderModelList) {
        this.context = context;
        this.allrounderModelList = allrounderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.captain_vcaptain_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllrounderModel playersListModel = allrounderModelList.get(position);
        DatabaseReference databaseReferenceCheck = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference2.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Credit = Double.parseDouble(snapshot.child("leftCredit").getValue().toString());
                    wicket = Integer.parseInt(snapshot.child("wk").getValue().toString());
                    bat = Integer.parseInt(snapshot.child("bt").getValue().toString());
                    bowl = Integer.parseInt(snapshot.child("br").getValue().toString());
                    ar = Integer.parseInt(snapshot.child("ar").getValue().toString());
                    tmOneCount = Integer.parseInt(snapshot.child("teamOneCount").getValue().toString());
                    tmTwoCount = Integer.parseInt(snapshot.child("teamTwoCount").getValue().toString());
                    scap = snapshot.child("captain").getValue().toString();
                    svcap = snapshot.child("viceCaptain").getValue().toString();
                    if (playersListModel.getPlayerName().equals(scap)) {
                        holder.cTag.setText("2X");
                        holder.capBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
                    } else {
                        holder.cTag.setText("C");
                        holder.capBtn.setCardBackgroundColor(Color.parseColor("#E3E3E3"));
                        databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child("AllRounder").child(playersListModel.getPid()).child("cap").setValue("NO");

                    }
                    if (playersListModel.getPlayerName().equals(svcap)) {
                        holder.vcTag.setText("1.5X");
                        holder.vcapBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
                    } else {
                        holder.vcTag.setText("VC");
                        holder.vcapBtn.setCardBackgroundColor(Color.parseColor("#E3E3E3"));
                        databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child("AllRounder").child(playersListModel.getPid()).child("vcap").setValue("NO");

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.team.setText(playersListModel.getTeamName());
        holder.player.setText(playersListModel.getPlayerName());
        holder.status.setText(playersListModel.getPlayerStatus());
        holder.identification.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.allrounder_icon));

        holder.capBtn.setOnClickListener(view -> {
            holder.layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
            holder.capBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
            holder.cTag.setText("2X");
            teamCode = Common.teamCode;
            leftCredit = Credit;
            WK = wicket;
            BT = bat;
            AR = ar;
            BR = bowl;
            teamOne = Common.teamOneName;
            teamTwo = Common.teamTwoName;
            captain = allrounderModelList.get(position).getPlayerName();
            viceCaptain = svcap;
            teamOneCount = tmOneCount;
            teamTwoCount = tmTwoCount;
            PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount,
                    teamOne, teamTwo, captain, viceCaptain);
            databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
            databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
            teamName = allrounderModelList.get(position).getTeamName();
            playerName = allrounderModelList.get(position).getPlayerName();
            pid = allrounderModelList.get(position).getPid();
            playerStatus = allrounderModelList.get(position).getPlayerStatus();
            playerRole = "AllRounder";
            isSelected = true;
            creditScore = allrounderModelList.get(position).getCreditScore();
            obtainedPoints = allrounderModelList.get(position).getObtainedPoints();
            cap = "YES";
            vcap = "NO";
            playerPic = allrounderModelList.get(position).getPlayerPic();
            SelectedPlayersModel selectedPlayersModel = new SelectedPlayersModel(teamName, playerName, pid, playerStatus,playerRole,
                    playerPic,creditScore, obtainedPoints,isSelected,cap, vcap);
            databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).setValue(selectedPlayersModel);

        });

        holder.vcapBtn.setOnClickListener(view -> {
            holder.layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
            holder.vcapBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
            holder.vcTag.setText("1.5X");
            teamCode = Common.teamCode;
            leftCredit = Credit;
            WK = wicket;
            BT = bat;
            AR = ar;
            BR = bowl;
            teamOne = Common.teamOneName;
            teamTwo = Common.teamTwoName;
            captain = scap;
            viceCaptain = allrounderModelList.get(position).getPlayerName();
            teamOneCount = tmOneCount;
            teamTwoCount = tmTwoCount;
            PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount,
                    teamOne, teamTwo, captain, viceCaptain);
            databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
            databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
            teamName = allrounderModelList.get(position).getTeamName();
            playerName = allrounderModelList.get(position).getPlayerName();
            pid = allrounderModelList.get(position).getPid();
            playerStatus = allrounderModelList.get(position).getPlayerStatus();
            playerRole = "AllRounder";
            isSelected = true;
            creditScore = allrounderModelList.get(position).getCreditScore();
            obtainedPoints = allrounderModelList.get(position).getObtainedPoints();
            cap = "NO";
            vcap = "YES";
            playerPic = allrounderModelList.get(position).getPlayerPic();
            SelectedPlayersModel selectedPlayersModel = new SelectedPlayersModel(teamName, playerName, pid, playerStatus,playerRole,
                    playerPic,creditScore, obtainedPoints,isSelected,cap, vcap);
            databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).setValue(selectedPlayersModel);

        });
    }

    @Override
    public int getItemCount() {
        return allrounderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView team, player,cTag,vcTag,status;
        CircleImageView identification;
        MaterialCardView layout;
        CardView capBtn, vcapBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.team);
            player = itemView.findViewById(R.id.player_name);
            identification = itemView.findViewById(R.id.identification_img);
            layout = itemView.findViewById(R.id.layout);
            cTag = itemView.findViewById(R.id.c_tag);
            vcTag = itemView.findViewById(R.id.vc_tag);
            status = itemView.findViewById(R.id.player_status);
            capBtn = itemView.findViewById(R.id.c_btn);
            vcapBtn = itemView.findViewById(R.id.vc_btn);
        }
    }
}
