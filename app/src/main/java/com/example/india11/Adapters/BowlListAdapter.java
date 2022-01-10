package com.example.india11.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class BowlListAdapter extends ArrayAdapter<PlayersListModel>{
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
    int totalBowlers, totalSelectedPlayers,selectedBy;
    Double creditsLeft,percent;
    public BowlListAdapter(@NonNull Context context, ArrayList<PlayersListModel> playersListModelArrayList) {
        super(context, playersListModelArrayList.size(), playersListModelArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.new_add_player_layout_india, parent, false);
        }
        PlayersListModel playersListModel = getItem(position);
        TextView team, player, credit,status,type,seriesPoint,sellPercent;
        CircleImageView identification;
        CardView layout,indicator,backCard;
        ImageView plus, minus;
        CheckBox checkBox;
        team = listitemView.findViewById(R.id.team);
        player = listitemView.findViewById(R.id.player_name);
        credit = listitemView.findViewById(R.id.credit_score);
        identification = listitemView.findViewById(R.id.identification_img);
        status = listitemView.findViewById(R.id.player_status);
        layout = listitemView.findViewById(R.id.layout);
        plus = listitemView.findViewById(R.id.plus);
        minus = listitemView.findViewById(R.id.minus);
        type = listitemView.findViewById(R.id.player_type);
        indicator = listitemView.findViewById(R.id.status_indicator);
        backCard = listitemView.findViewById(R.id.back_card);
        seriesPoint = listitemView.findViewById(R.id.seriesPoint);
        sellPercent = listitemView.findViewById(R.id.sellPercent);
        team.setText(playersListModel.getTeamName());
        player.setText(playersListModel.getPlayerName());
        credit.setText(String.valueOf(playersListModel.getCreditScore()));
        Glide.with(getContext()).load(playersListModel.getPlayerPic()).into(identification);
        type.setText("Bowler");
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
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        //loading.show();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);

        databaseReference7.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers")
                .child(playersListModel.getPid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status1 = snapshot.child("playerStatus").getValue().toString();
                    if (status1.equals("Not in lineup")){
                        indicator.setCardBackgroundColor(Color.parseColor("#FF5722"));
                        status.setTextColor(Color.parseColor("#FF5722"));
                        status.setText(status1);
                    }else if (status1.equals("In lineup")){
                        indicator.setCardBackgroundColor(Color.parseColor("#4CAF50"));
                        status.setTextColor(Color.parseColor("#4CAF50"));
                        status.setText(status1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference6.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Bowler")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(playersListModel.getPid())) {
                                layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
                                backCard.setCardBackgroundColor(Color.parseColor("#2C5E1A"));
                                plus.setVisibility(View.GONE);
                                minus.setVisibility(View.VISIBLE);
                            } else {
                                layout.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                                backCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                                minus.setVisibility(View.GONE);
                                plus.setVisibility(View.VISIBLE);
                            }
                        }else {
                            minus.setVisibility(View.GONE);
                            plus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference11.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers")
                .child(playersListModel.getPid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    select = Integer.parseInt(snapshot.child("selectedBy").getValue().toString());
                    double prevoiusP = Double.parseDouble(snapshot.child("previousPoints").getValue().toString());
                    seriesPoint.setText(String.valueOf(prevoiusP));
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
                        sellPercent.setText(String.valueOf("0%"));
                    }else {
                        percent = Double.valueOf((select/people)*100);
                        DecimalFormat decimalFormat = new DecimalFormat("##.##%");
                        String per = decimalFormat.format(percent/100);
                        sellPercent.setText(String.valueOf(per));
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
                            totalBowlers = bowl;
                            totalSelectedPlayers = wicket + bat + bowl + ar;
                            creditsLeft = Credit;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        plus.setOnClickListener(view -> {
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
            if (totalBowlers == 6){
                //Toast.makeText(context, "Maximum bowlers selected.", Toast.LENGTH_SHORT).show();
                toastMessage.setText("Maximum bowlers selected.");
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
            }else if (playersListModel.getTeamName().equals(Common.teamOneName) && tmOneCount == 7){
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
            }else if (playersListModel.getTeamName().equals(Common.teamTwoName) && tmTwoCount == 7){
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
                plus.setVisibility(View.GONE);
                minus.setVisibility(View.VISIBLE);
                layout.setCardBackgroundColor(Color.parseColor("#FFE4DB"));
                backCard.setCardBackgroundColor(Color.parseColor("#2C5E1A"));
                Common.creditUsed = playersListModel.getCreditScore();
                bowl++;
                teamCode = Common.teamCode;
                leftCredit = Credit - Common.creditUsed;
                WK = wicket;
                BT = bat;
                AR = ar;
                BR = bowl;
                teamOne = Common.teamOneName;
                teamTwo = Common.teamTwoName;
                captain = Common.oldCap;
                viceCaptain = Common.oldVcap;
                if (playersListModel.getTeamName().equals(teamOne)) {
                    tmOneCount++;
                    teamOneCount = tmOneCount;
                    teamTwoCount = tmTwoCount;
                } else if (playersListModel.getTeamName().equals(teamTwo)) {
                    tmTwoCount++;
                    teamTwoCount = tmTwoCount;
                    teamOneCount = tmOneCount;
                }
                PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode, leftCredit, WK, BT, AR, BR, teamOneCount, teamTwoCount,
                        teamOne, teamTwo, captain, viceCaptain);
                databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
                databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
                teamName = playersListModel.getTeamName();
                playerName = playersListModel.getPlayerName();
                pid = playersListModel.getPid();
                playerStatus = playersListModel.getPlayerStatus();
                playerRole = "Bowler";
                isSelected = true;
                creditScore = playersListModel.getCreditScore();
                obtainedPoints = playersListModel.getObtainedPoints();
                cap = "NO";
                vcap = "NO";
                playerPic = playersListModel.getPlayerPic();
                SelectedPlayersModel selectedPlayersModel = new SelectedPlayersModel(teamName, playerName, pid, playerStatus, playerRole,
                        playerPic,creditScore, obtainedPoints, isSelected, cap, vcap);
                databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).setValue(selectedPlayersModel);
                databaseReference8.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(teamCode).child(pid).setValue(selectedPlayersModel);
                DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
                selectedBy = select+1;
                databaseReference12.child("ContestSeries").child(Common.seriesId)
                        .child("AllPlayers").child(playersListModel.getPid())
                        .child("selectedBy").setValue(selectedBy);
            }
        });
        minus.setOnClickListener(view -> {
            minus.setVisibility(View.GONE);
            plus.setVisibility(View.VISIBLE);
            layout.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            backCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            Common.creditUsed = playersListModel.getCreditScore();
            bowl --;
            teamCode = Common.teamCode;
            leftCredit = Credit + Common.creditUsed;
            WK = wicket;
            BT = bat;
            AR = ar;
            BR = bowl;
            teamOne = Common.teamOneName;
            teamTwo = Common.teamTwoName;
            captain = Common.oldCap;
            viceCaptain = Common.oldVcap;

            if (playersListModel.getTeamName().equals(teamOne)){
                tmOneCount --;
                teamOneCount = tmOneCount;
                teamTwoCount = tmTwoCount;
            }else if (playersListModel.getTeamName().equals(teamTwo)){
                tmTwoCount --;
                teamTwoCount = tmTwoCount;
                teamOneCount = tmOneCount;
            }
            PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount,
                    teamOne, teamTwo, captain, viceCaptain);
            databaseReference.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel);
            databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
            pid = playersListModel.getPid();
            playerRole = "Bowler";
            databaseReference3.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(teamCode).child(playerRole).child(pid).removeValue();
            databaseReference9.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(teamCode).child(pid).removeValue();
            DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
            selectedBy = select-1;
            databaseReference12.child("ContestSeries").child(Common.seriesId)
                    .child("AllPlayers").child(playersListModel.getPid())
                    .child("selectedBy").setValue(selectedBy);
        });
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Item clicked is : "+playersListModel.getPlayerName(), Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}
