package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Common;
import com.example.india11.ContestsFragments.SelectTeam;
import com.example.india11.ContestsFragments.SubContestsDetails;
import com.example.india11.CreateTeam.CreatePlayerTeam;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.SubContestsModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class SubContestsAdapter extends RecyclerView.Adapter<SubContestsAdapter.ViewHolder> {
    public Context context;
    private List<SubContestsModel> subContestsModelList;
    DatabaseReference databaseReferenceWallet,databaseReferenceTeamCheck,databaseReferenceJoin,databaseReference,databaseReferenceTeam;
    DatabaseReference databaseReference5,databaseReference4,databaseReference6,databaseReference7;
    FirebaseAuth firebaseAuth;
    String uid,tBalance;
    Dialog joiningDialog,toastDialog;
    TextView walletBal, joinFee, bonusFee, payAmount,toastMessage;
    LinearLayout confirm;
    ImageView cancel;
    long balance;
    double Credits = 100.0;
    int wk = 0;
    int bt = 0;
    int ar = 0;
    int br = 0;
    int teamOneCount = 0;
    int teamTwoCount = 0;
    Integer WK,BT,AR,BR;
    String teamOne, teamTwo, captain, viceCaptain;
    String scid, tag, winningPercent, leagueType,totalSpots, leftSpots,totalWinners,multipleLeague;
    Long entryFee, prizePool;
    Integer joined;
    String matchId,id;
    Integer played, won;
    Double invested, earned;
    int teamName = 1;


    public SubContestsAdapter(Context context, List<SubContestsModel> subContestsModelList) {
        this.context = context;
        this.subContestsModelList = subContestsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_contests_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubContestsModel subContestsModel = subContestsModelList.get(position);
        databaseReferenceWallet = FirebaseDatabase.getInstance().getReference();
        databaseReferenceTeamCheck = FirebaseDatabase.getInstance().getReference();
        databaseReferenceJoin = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceTeam = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceJoin.child("INDIA11Users").child(uid).child("UserStats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int played = Integer.parseInt(snapshot.child("played").getValue().toString());
                    if (played == 0 && subContestsModelList.get(position).getEntryFee() == 10){
                        holder.entryFee.setText("FREE");
                    }else {
                        holder.entryFee.setText(convertValueInIndianCurrency(subContestsModel.getEntryFee()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceWallet.child("INDIA11Users").child(uid).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("totalBalance").getValue().toString();
                balance = Long.parseLong(tBalance);
                Common.walletBalance = balance;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("INDIA11Users").child(uid).child("BonusBalance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String bBalance = snapshot.child("bonusBalance").getValue().toString();
                        long bBal = Long.parseLong(bBalance);
                        Common.walletBonus = bBal;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference7.child("INDIA11Users").child(uid).child("UserStats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        played = Integer.valueOf(snapshot.child("played").getValue().toString());
                        invested = Double.valueOf((snapshot.child("invested").getValue().toString()));
                        won = Integer.valueOf(snapshot.child("won").getValue().toString());
                        earned = Double.valueOf((snapshot.child("earned").getValue().toString()));

                        Common.playedLeague = played;
                        Common.investedAmount = invested;
                        Common.earnedAmount = earned;
                        Common.wonLeague = won;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        joiningDialog = new Dialog(context);
        joiningDialog.setContentView(R.layout.joining_confirmation_layout);
        joiningDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        joiningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        joiningDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        walletBal = joiningDialog.findViewById(R.id.wallet_balance);
        joinFee = joiningDialog.findViewById(R.id.joining_fee);
        bonusFee = joiningDialog.findViewById(R.id.bonus_fee);
        payAmount = joiningDialog.findViewById(R.id.total_pay);
        confirm = joiningDialog.findViewById(R.id.submit);
        cancel = joiningDialog.findViewById(R.id.cancel);
        toastDialog = new Dialog(context);
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);

        holder.prizePool.setText(convertValueInIndianCurrency(subContestsModel.getPrizePool()));
        holder.totalSpots.setText(subContestsModel.getTotalSpots());
        holder.leftSpots.setText(String.valueOf(subContestsModel.getJoined()));
        holder.firstPrize.setText(convertValueInIndianCurrency(subContestsModel.getPrizePool()));
        holder.winningPercent.setText(subContestsModel.getWinningPercent());
        holder.progressBar.setMax(Integer.parseInt(subContestsModel.getTotalSpots()));
        holder.progressBar.setProgress(subContestsModel.getJoined());
        holder.leagueType.setText(subContestsModel.getLeagueType());
        holder.winners.setText(subContestsModel.getTotalWinners());

        if (Integer.parseInt(subContestsModel.getTotalSpots()) <= 10){
            holder.typeIdentifier.setText("S");
            holder.tags.setText("Single");
            holder.ub.setText("Usable Bonus "+convertValueInIndianCurrency(0));
        }else if (Integer.parseInt(subContestsModel.getTotalSpots()) > 10 && Integer.parseInt(subContestsModel.getTotalSpots()) <= 40){
            holder.typeIdentifier.setText("M");
            holder.tags.setText("Upto 6");
            holder.ub.setText("Usable Bonus "+convertValueInIndianCurrency(0));
        }else if (Integer.parseInt(subContestsModel.getTotalSpots()) > 40 && Integer.parseInt(subContestsModel.getTotalSpots()) <= 150){
            holder.typeIdentifier.setText("M");
            holder.tags.setText("Upto 10");
            holder.ub.setText("Usable Bonus "+convertValueInIndianCurrency(0));
        }else if (Integer.parseInt(subContestsModel.getTotalSpots()) > 150 && Integer.parseInt(subContestsModel.getTotalSpots()) <= 1000){
            holder.typeIdentifier.setText("M");
            holder.tags.setText("Upto 15");
            holder.ub.setText("Usable Bonus "+convertValueInIndianCurrency(0));
        }else if (Integer.parseInt(subContestsModel.getTotalSpots()) > 1000){
            holder.typeIdentifier.setText("G");
            holder.tags.setText("Upto 20");
            double bonus = subContestsModel.getEntryFee()*7;
            long uBonus = (long) (bonus/100);
            //Toast.makeText(context, String.valueOf(uBonus), Toast.LENGTH_SHORT).show();
            //long bonus = subContestsModel.getEntryFee()/100;
            holder.ub.setText("Usable Bonus "+convertValueInIndianCurrency(uBonus));
        }

        holder.joinText.setText("Join");
        if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) == subContestsModelList.get(position).getJoined() && subContestsModelList.get(position).getMultipleLeague().equals("NO")){
            holder.join.setVisibility(View.GONE);
            holder.joinText.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            matchId=formatter.format(date);
            matchId=matchId.replace("/","").replace(":","").replace(" ","");
            id="CIDI11"+matchId;
            scid = id;
            tag = subContestsModelList.get(position).getTag();
            winningPercent = subContestsModelList.get(position).getWinningPercent();
            leagueType = subContestsModelList.get(position).getLeagueType();
            totalSpots = subContestsModelList.get(position).getTotalSpots();
            leftSpots = subContestsModelList.get(position).getLeftSpots();
            totalWinners = subContestsModelList.get(position).getTotalWinners();
            entryFee = subContestsModelList.get(position).getEntryFee();
            prizePool = subContestsModelList.get(position).getPrizePool();
            joined = 0;
            multipleLeague = "NO";
            SubContestsModel contestsModel = new SubContestsModel(scid, tag, winningPercent, leagueType,totalSpots, leftSpots,totalWinners,
                    multipleLeague,entryFee, prizePool,joined);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(scid).setValue(contestsModel);
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
            multipleLeague = "YES";
            databaseReference1.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(subContestsModelList.get(position).getScid())
                    .child("multipleLeague").setValue("YES");
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (subContestsModelList.get(position).getJoined() == Integer.valueOf(subContestsModelList.get(position).getTotalSpots())){
            holder.joinText.setText("Full");
        }
        holder.join.setOnClickListener(view -> {
            if (subContestsModelList.get(position).getJoined() == Integer.valueOf(subContestsModelList.get(position).getTotalSpots())){
                toastMessage.setText("League is already full, please join other one.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else {
                Common.buttonType = "Create";
                Common.totalSpots = Integer.parseInt(subContestsModelList.get(position).getTotalSpots());

                if (holder.entryFee.getText().toString().equals("FREE")){
                    Common.entryFee = 0;
                }else {
                    Common.entryFee = subContestsModelList.get(position).getEntryFee();
                }
                Common.subContestId = subContestsModelList.get(position).getScid();
                if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) <= 10){
                    databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                            .child(subContestsModelList.get(position).getScid()).child("SingleTeam").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Common.userNameID)){
                                //Toast.makeText(context, "You have already joined the league.", Toast.LENGTH_SHORT).show();
                                if(!((Activity) context).isFinishing())
                                {
                                    toastMessage.setText("You have already joined this league.");
                                    toastDialog.show();
                                    final Timer timer2 = new Timer();
                                    timer2.schedule(new TimerTask() {
                                        public void run() {
                                            toastDialog.dismiss();
                                            timer2.cancel(); //this will cancel the timer of the system
                                        }
                                    }, 1000);
                                }

                            }else {
                                Common.contestType = "Small";
                                Common.usableBonus = 0;
                                teamCheckToJoin(view);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) > 10 && Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) <= 40){
                    databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                            .child(subContestsModelList.get(position).getScid()).child("MultipleTeam").child(Common.userNameID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    if (count >= 6){
                                        //Toast.makeText(context, "You have joined with maximum number of teams.", Toast.LENGTH_SHORT).show();
                                        toastMessage.setText("You have joined with maximum number of teams.");
                                        toastDialog.show();
                                        final Timer timer2 = new Timer();
                                        timer2.schedule(new TimerTask() {
                                            public void run() {
                                                toastDialog.dismiss();
                                                timer2.cancel(); //this will cancel the timer of the system
                                            }
                                        }, 1000);
                                    }else {
                                        Common.contestType = "Medium";
                                        Common.usableBonus = 0;
                                        teamCheckToJoin(view);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }else if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) > 40 && Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) <= 150){
                    databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                            .child(subContestsModelList.get(position).getScid()).child("MultipleTeam").child(Common.userNameID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    if (count >= 10){
                                        //Toast.makeText(context, "You have joined with maximum number of teams.", Toast.LENGTH_SHORT).show();
                                        toastMessage.setText("You have joined with maximum number of teams.");
                                        toastDialog.show();
                                        final Timer timer2 = new Timer();
                                        timer2.schedule(new TimerTask() {
                                            public void run() {
                                                toastDialog.dismiss();
                                                timer2.cancel(); //this will cancel the timer of the system
                                            }
                                        }, 1000);
                                    }else {
                                        Common.contestType = "Medium";
                                        Common.usableBonus = 0;
                                        teamCheckToJoin(view);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }else if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) > 150 && Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) <= 1000){
                    databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                            .child(subContestsModelList.get(position).getScid()).child("MultipleTeam").child(Common.userNameID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    if (count >= 15){
                                        //Toast.makeText(context, "You have joined with maximum number of teams.", Toast.LENGTH_SHORT).show();
                                        toastMessage.setText("You have joined with maximum number of teams.");
                                        toastDialog.show();
                                        final Timer timer2 = new Timer();
                                        timer2.schedule(new TimerTask() {
                                            public void run() {
                                                toastDialog.dismiss();
                                                timer2.cancel(); //this will cancel the timer of the system
                                            }
                                        }, 1000);
                                    }else {
                                        Common.contestType = "Medium";
                                        Common.usableBonus = 0;
                                        teamCheckToJoin(view);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }else if (Integer.parseInt(subContestsModelList.get(position).getTotalSpots()) > 1000){
                    databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                            .child(subContestsModelList.get(position).getScid()).child("MultipleTeam").child(Common.userNameID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    if (count >= 20){
                                        //Toast.makeText(context, "You have joined with maximum number of teams.", Toast.LENGTH_SHORT).show();
                                        toastMessage.setText("You have joined with maximum number of teams.");
                                        toastDialog.show();
                                        final Timer timer2 = new Timer();
                                        timer2.schedule(new TimerTask() {
                                            public void run() {
                                                toastDialog.dismiss();
                                                timer2.cancel(); //this will cancel the timer of the system
                                            }
                                        }, 1000);
                                    }else {
                                        Common.contestType = "Grand";
                                        double bonus = subContestsModel.getEntryFee()*7;
                                        long uBonus = (long) (bonus/100);
                                        Common.usableBonus = uBonus;
                                        teamCheckToJoin(view);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            });

        holder.contDetails.setOnClickListener(view -> {
            String subContestsId = subContestsModel.getScid();
            Common.subContestId = subContestsId;
            Common.totalSpot = Integer.parseInt(subContestsModelList.get(position).getTotalSpots());
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment subContestsDetails = new SubContestsDetails();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,subContestsDetails)
                    .addToBackStack(null).commit();
        });
        cancel.setOnClickListener(view -> {
            joiningDialog.dismiss();
        });

    }

    private void teamCheckToJoin(View view) {
        databaseReferenceTeamCheck.child("AvailableContests").child(Common.avlContestId).child("IsUserCreatedTeam")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(Common.profileMobile)){
                            joiningDialog.show();
                            walletBal.setText("Wallet balance: "+convertValueInIndianCurrency(Double.parseDouble(tBalance)));
                            joinFee.setText(convertValueInIndianCurrency(Common.entryFee));
                            bonusFee.setText(convertValueInIndianCurrency(Common.usableBonus));

                            payAmount.setText(convertValueInIndianCurrency(Common.entryFee));
                            confirm.setOnClickListener(view1 -> {
                                joiningDialog.dismiss();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Fragment teams = new SelectTeam();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,teams)
                                        .addToBackStack(null).commit();

                            });
                        }else {
                            DatabaseReference databaseReference8 = FirebaseDatabase.getInstance().getReference();
                            databaseReference8.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(String.valueOf(Common.teamName))){
                                        DatabaseReference databaseReference9 = FirebaseDatabase.getInstance().getReference();
                                        databaseReference9.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            String teamCode = String.valueOf(Common.teamName);
                            Double leftCredit = Credits;
                            WK = wk;
                            BT = bt;
                            AR = ar;
                            BR = br;
                            Common.teamCode = teamCode;
                            //InitialCreateTeamModel initialCreateTeamModel = new InitialCreateTeamModel(teamCode,leftCredit,WK,BT,AR,BR);
                            teamOne = Common.teamOneName;
                            teamTwo = Common.teamTwoName;
                            captain = "";
                            viceCaptain = "";
                            PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount,
                                    teamOne, teamTwo, captain, viceCaptain);
                            databaseReference5.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(teamCode).setValue(playerValuesModel);
                            databaseReference4.child("INDIA11Users").child(uid).child("CreatedTeams").child(teamCode).setValue(playerValuesModel).addOnSuccessListener(aVoid -> {
                            });
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment createTeam = new CreatePlayerTeam();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,createTeam)
                                    .addToBackStack(null).commit();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return subContestsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ub,prizePool, entryFee, totalSpots, leftSpots, firstPrize, winningPercent, tags,joinText,leagueType,winners,typeIdentifier;
        LinearLayout contDetails;
        LinearLayout join;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prizePool = itemView.findViewById(R.id.prize_pool);
            entryFee = itemView.findViewById(R.id.entry_fee);
            totalSpots = itemView.findViewById(R.id.total_spots);
            leftSpots = itemView.findViewById(R.id.left_spot);
            firstPrize = itemView.findViewById(R.id.first_prize);
            winningPercent = itemView.findViewById(R.id.winning_percent);
            tags = itemView.findViewById(R.id.tags);
            contDetails = itemView.findViewById(R.id.cont_detail);
            join = itemView.findViewById(R.id.join_btn);
            progressBar = itemView.findViewById(R.id.joinedProgress);
            joinText = itemView.findViewById(R.id.join_txt);
            leagueType = itemView.findViewById(R.id.league_type);
            winners = itemView.findViewById(R.id.winners_number);
            typeIdentifier = itemView.findViewById(R.id.typeIdentifier);
            ub = itemView.findViewById(R.id.ub);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
