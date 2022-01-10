package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Model.InitialCreateTeamModel;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class BatsmanAdapter extends RecyclerView.Adapter<BatsmanAdapter.ViewHolder> {
    public Context context;
    private List<PlayersListModel> playersListModelList;
    int count = 0,tOne = 0, tTwo = 0;
    int wicket, bat, bowl, ar, tmOneCount, tmTwoCount,select;
    Integer WK,BT,AR,BR,teamOneCount,teamTwoCount;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference5,databaseReference6,
            databaseReference7,databaseReference8,databaseReference9,databaseReference10,databaseReference11,databaseReference12;
    String uid;
    Dialog loading;
    Dialog toastDialog;
    TextView toastMessage;
    FirebaseAuth firebaseAuth;
    double Credit;
    String teamCode;
    Double leftCredit;
    Integer totalPlayers;
    String teamName, playerName, pid, playerStatus,playerRole,playerPic;
    double creditScore, obtainedPoints;
    Boolean isSelected;
    String teamOne, teamTwo, captain, viceCaptain,cap, vcap;
    int totalBatsMan, totalSelectedPlayers,selectedBy;
    Double creditsLeft,percent;

    public BatsmanAdapter(Context context, List<PlayersListModel> playersListModelList) {
        this.context = context;
        this.playersListModelList = playersListModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_player_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayersListModel playersListModel = playersListModelList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReference9 = FirebaseDatabase.getInstance().getReference();
        databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference11 = FirebaseDatabase.getInstance().getReference();
        databaseReference12 = FirebaseDatabase.getInstance().getReference();
        loading = new Dialog(context);
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        //loading.show();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        toastDialog = new Dialog(context);
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        databaseReference7.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers")
                .child(playersListModelList.get(position).getPid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("playerStatus").getValue().toString();
                    if (status.equals("Not in lineup")){
                        holder.indicator.setCardBackgroundColor(Color.parseColor("#FF5722"));
                        holder.status.setTextColor(Color.parseColor("#FF5722"));
                        holder.status.setText(status);
                    }else if (status.equals("In lineup")){
                        holder.indicator.setCardBackgroundColor(Color.parseColor("#4CAF50"));
                        holder.status.setTextColor(Color.parseColor("#4CAF50"));
                        holder.status.setText(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference6.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Batsman")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(playersListModelList.get(position).getPid())) {
                                holder.layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
                                holder.backCard.setCardBackgroundColor(Color.parseColor("#2C5E1A"));
                                holder.plus.setVisibility(View.GONE);
                                holder.minus.setVisibility(View.VISIBLE);
                            } else {
                                holder.layout.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                                holder.backCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                                holder.minus.setVisibility(View.GONE);
                                holder.plus.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.minus.setVisibility(View.GONE);
                            holder.plus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference11.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers")
                .child(playersListModelList.get(position).getPid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    select = Integer.parseInt(snapshot.child("selectedBy").getValue().toString());
                    double prevoiusP = Double.parseDouble(snapshot.child("previousPoints").getValue().toString());
                    holder.seriesPoint.setText(String.valueOf(prevoiusP));
                    //loading.dismiss();
                    String playerType = snapshot.child("playerType").getValue().toString();
                    String updated = snapshot.child("updated").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference13 = FirebaseDatabase.getInstance().getReference();
        databaseReference13.child("AvailableContests").child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int people = Integer.parseInt(snapshot.child("people").getValue().toString());
                    if (people == 0){
                        holder.sellPercent.setText(String.valueOf("0%"));
                    }else {
                        percent = Double.valueOf((select/people)*100);
                        DecimalFormat decimalFormat = new DecimalFormat("##.##%");
                        String per = decimalFormat.format(percent/100);
                        holder.sellPercent.setText(String.valueOf(per));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference10.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Credit = Double.parseDouble(snapshot.child("leftCredit").getValue().toString());
                            wicket = Integer.parseInt(snapshot.child("wk").getValue().toString());
                            bat = Integer.parseInt(snapshot.child("bt").getValue().toString());
                            bowl = Integer.parseInt(snapshot.child("br").getValue().toString());
                            ar = Integer.parseInt(snapshot.child("ar").getValue().toString());
                            tmOneCount = Integer.parseInt(snapshot.child("teamOneCount").getValue().toString());
                            tmTwoCount = Integer.parseInt(snapshot.child("teamTwoCount").getValue().toString());
                            totalBatsMan = bat;
                            totalSelectedPlayers = wicket + bat + bowl + ar;
                            creditsLeft = Credit;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.team.setText(playersListModel.getTeamName());
        holder.player.setText(playersListModel.getPlayerName());
        holder.credit.setText(String.valueOf(playersListModel.getCreditScore()));

        holder.type.setText("Batter");
        Glide.with(context).load(playersListModel.getPlayerPic()).into(holder.identification);

        holder.plus.setOnClickListener(view -> {
            databaseReference2.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Credit = Double.parseDouble(snapshot.child("leftCredit").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            if (totalBatsMan == 6){
                //Toast.makeText(context, "Maximum batter selected.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("Maximum batter selected.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (totalSelectedPlayers == 11){
                //Toast.makeText(context, "Maximum players selected.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("Maximum players selected.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (Credit == 0.0){
                //Toast.makeText(context, "You do not have sufficient credit left.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("You do not have sufficient credit left.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (Credit <= 7.0){
                //Toast.makeText(context, "You do not have sufficient credit left.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("You do not have sufficient credit left.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (playersListModelList.get(position).getTeamName().equals(Common.teamOneName) && tmOneCount == 7){
                //Toast.makeText(context, "Maximum 7 players can be selected.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("Maximum 7 players can be selected.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (playersListModelList.get(position).getTeamName().equals(Common.teamTwoName) && tmTwoCount == 7){
                //Toast.makeText(context, "Maximum 7 players can be selected.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("Maximum 7 players can be selected.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else if (playersListModel.getCreditScore() > Credit){
                toastMessage.setText("Yo don`t have sufficient credit.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else {
                holder.plus.setVisibility(View.GONE);
                holder.minus.setVisibility(View.VISIBLE);
                holder.layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
                holder.backCard.setCardBackgroundColor(Color.parseColor("#2C5E1A"));
                Common.creditUsed = playersListModelList.get(position).getCreditScore();
                bat++;
                teamCode = Common.teamCode;
                leftCredit = Credit - Common.creditUsed;
                WK = wicket;
                BT = bat;
                AR = ar;
                BR = bowl;
                teamOne = Common.teamOneName;
                teamTwo = Common.teamTwoName;
                captain = "";
                viceCaptain = "";
                if (playersListModelList.get(position).getTeamName().equals(teamOne)) {
                    tmOneCount++;
                    teamOneCount = tmOneCount;
                    teamTwoCount = tmTwoCount;
                } else if (playersListModelList.get(position).getTeamName().equals(teamTwo)) {
                    tmTwoCount++;
                    teamTwoCount = tmTwoCount;
                    teamOneCount = tmOneCount;
                }
                PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode, leftCredit, WK, BT, AR, BR, teamOneCount, teamTwoCount,
                        teamOne, teamTwo, captain, viceCaptain);
                databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
                databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
                teamName = playersListModelList.get(position).getTeamName();
                playerName = playersListModelList.get(position).getPlayerName();
                pid = playersListModelList.get(position).getPid();
                playerStatus = playersListModelList.get(position).getPlayerStatus();
                playerRole = "Batsman";
                isSelected = true;
                creditScore = playersListModelList.get(position).getCreditScore();
                obtainedPoints = playersListModelList.get(position).getObtainedPoints();
                cap = "NO";
                vcap = "NO";
                playerPic = playersListModelList.get(position).getPlayerPic();
                SelectedPlayersModel selectedPlayersModel = new SelectedPlayersModel(teamName, playerName, pid, playerStatus, playerRole,
                        playerPic,creditScore, obtainedPoints, isSelected, cap, vcap);
                databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).setValue(selectedPlayersModel);
                databaseReference8.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(teamCode).child(pid).setValue(selectedPlayersModel);
                DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
                selectedBy = select+1;
                databaseReference12.child("ContestSeries").child(Common.seriesId)
                        .child("AllPlayers").child(playersListModelList.get(position).getPid())
                        .child("selectedBy").setValue(selectedBy);
            }
        });
        holder.minus.setOnClickListener(view -> {
            holder.minus.setVisibility(View.GONE);
            holder.plus.setVisibility(View.VISIBLE);
            holder.layout.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.backCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            Common.creditUsed = playersListModelList.get(position).getCreditScore();
            bat --;
            teamCode = Common.teamCode;
            leftCredit = Credit + Common.creditUsed;
            WK = wicket;
            BT = bat;
            AR = ar;
            BR = bowl;
            teamOne = Common.teamOneName;
            teamTwo = Common.teamTwoName;
            captain = "";
            viceCaptain = "";

            if (playersListModelList.get(position).getTeamName().equals(teamOne)){
                tmOneCount --;
                teamOneCount = tmOneCount;
                teamTwoCount = tmTwoCount;
            }else if (playersListModelList.get(position).getTeamName().equals(teamTwo)){
                tmTwoCount --;
                teamTwoCount = tmTwoCount;
                teamOneCount = tmOneCount;
            }
            PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount,
                    teamOne, teamTwo, captain, viceCaptain);
            databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
            databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
            pid = playersListModelList.get(position).getPid();
            playerRole = "Batsman";
            databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).removeValue();
            databaseReference9.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(teamCode).child(pid).removeValue();
            DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
            selectedBy = select-1;
            databaseReference12.child("ContestSeries").child(Common.seriesId)
                    .child("AllPlayers").child(playersListModelList.get(position).getPid())
                    .child("selectedBy").setValue(selectedBy);
        });

    }

    @Override
    public int getItemCount() {
        return playersListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView team, player, credit,status,type,seriesPoint,sellPercent;
        CircleImageView identification;
        CardView layout,indicator,backCard;
        ImageView plus, minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.team);
            player = itemView.findViewById(R.id.player_name);
            credit = itemView.findViewById(R.id.credit_score);
            identification = itemView.findViewById(R.id.identification_img);
            status = itemView.findViewById(R.id.player_status);
            layout = itemView.findViewById(R.id.layout);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            type = itemView.findViewById(R.id.player_type);
            indicator = itemView.findViewById(R.id.status_indicator);
            backCard = itemView.findViewById(R.id.back_card);
            seriesPoint = itemView.findViewById(R.id.seriesPoint);
            sellPercent = itemView.findViewById(R.id.sellPercent);
        }
    }
}
