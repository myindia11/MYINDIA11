package com.example.india11.ContestsFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Adapters.SelectTeamAdapter;
import com.example.india11.Common;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.Model.JoinedSubContestsModel;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.SubContestsModel;
import com.example.india11.Model.UserStatsModel;
import com.example.india11.databinding.FragmentSelectTeamBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectTeam extends Fragment implements SelectTeamAdapter.SendData{
    private FragmentSelectTeamBinding binding;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5;
    DatabaseReference databaseReference6,databaseReference7,databaseReference8,databaseReference9,databaseReference10,databaseReference11;
    private List<PlayerValuesModel> playerValuesModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    String uid;
    public long entryFee,prizePool;
    String totalSpots, leftSpots,teamId;
    String UID, FID, CID, SCID, TID,creationId,paid;
    String scid, winningPercent, leagueType, tId, wp, lt, cId;
    Integer rank;
    double obtainedPoints = 0.0;
    int balance,etFee,count;
    Long totalBalance,bonusBalance;
    String contestsId, contestsName, matchTime, teamOne, teamOneLogo, teamTwo, teamTwoLogo, winningAmount,seriesName,seriesId;
    String tag,totalWinners,multipleTagName,multipleLeague;
    Integer joined;
    int multipleTeamC = 0;
    String teamNode;
    String teamCode;
    Double leftCredit;
    Integer WK,BT,AR,BR,teamOneCount,teamTwoCount;
    String captain, viceCaptain;
    Integer played, won;
    Integer people;
    Double invested, earned;
    UserStatsModel userStatsModel;
    DatabaseReference databaseReferenceAmt, databaseReferenceAddAmt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectTeamBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceAmt = FirebaseDatabase.getInstance().getReference();
        databaseReferenceAddAmt = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReference9 = FirebaseDatabase.getInstance().getReference();
        databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference11 = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(Common.subContestId)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prizePool = Long.parseLong(snapshot.child("prizePool").getValue().toString());
                        etFee = Integer.parseInt(snapshot.child("entryFee").getValue().toString());
                        totalSpots = snapshot.child("totalSpots").getValue().toString();
                        leftSpots = snapshot.child("leftSpots").getValue().toString();
                        wp = snapshot.child("winningPercent").getValue().toString();
                        lt = snapshot.child("leagueType").getValue().toString();
                        tag = snapshot.child("tag").getValue().toString();
                        totalWinners = snapshot.child("totalWinners").getValue().toString();
                        joined = Integer.parseInt(snapshot.child("joined").getValue().toString());
                        multipleLeague = snapshot.child("multipleLeague").getValue().toString();
                        binding.leftSpot.setText(leftSpots+" Spots Joined");
                        binding.totalSpots.setText(totalSpots+" Spots");
                        binding.pricePool.setText(convertValueInIndianCurrency(prizePool));
                        binding.joinFee.setText(convertValueInIndianCurrency(etFee));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReferenceAmt.child("JoinedLeagueAmount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String amount1 = snapshot.child("amount").getValue().toString();
                    binding.amt.setText(amount1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadTeams();
        binding.contestsName.setText(Common.contestsName);
        binding.joinFee.setText(convertValueInIndianCurrency(Common.entryFee));
        binding.joinBtn.setOnClickListener(view1 -> {
            DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
            databaseReference12.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(Common.subContestId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                totalSpots = snapshot.child("totalSpots").getValue().toString();
                                leftSpots = snapshot.child("leftSpots").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            int joined = Integer.parseInt(leftSpots);
            int t = Integer.parseInt(totalSpots);
            if (binding.tid.getText().equals("")){
                Toast.makeText(getContext(), "Select a team to join!", Toast.LENGTH_SHORT).show();
            }else if (Common.walletBalance < Common.entryFee){
                Toast.makeText(getContext(), "You do not have sufficient balance to join.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }else if (joined == t){
                Toast.makeText(getContext(), "League already full please join other one.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }else {
                updateWallet();
            }
        });
        return view;
    }

    private void updateWallet() {
        databaseReference7.child("AvailableContests").child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    count = Integer.parseInt(snapshot.child("people").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        switch (Common.contestType){
            case "Small":
            case "Medium":
                totalBalance = Common.walletBalance - Common.entryFee;
                databaseReference3.child("INDIA11Users").child(uid).child("TotalBalance").child("totalBalance").setValue(totalBalance).addOnSuccessListener(aVoid -> {
                    joinLeague(count);
                });
                played = Common.playedLeague+1;
                won = Common.wonLeague;
                invested = Common.investedAmount + Common.entryFee;
                earned = Common.earnedAmount;
                userStatsModel = new UserStatsModel(played, won,invested,earned);
                databaseReference11.child("INDIA11Users").child(uid).child("UserStats").setValue(userStatsModel);
                break;
            case "Grand":
                long fee = Common.entryFee - Common.usableBonus;
                totalBalance = Common.walletBalance - fee;
                bonusBalance = Common.walletBonus - Common.usableBonus;
                databaseReference10.child("INDIA11Users").child(uid).child("BonusBalance").child("bonusBalance").setValue(bonusBalance).addOnSuccessListener(aVoid -> {
                });
                databaseReference3.child("INDIA11Users").child(uid).child("TotalBalance").child("totalBalance").setValue(totalBalance).addOnSuccessListener(aVoid -> {
                    joinLeague(count);
                });
                played = Common.playedLeague+1;
                won = Common.wonLeague;
                invested = Common.investedAmount + Common.entryFee;
                earned = Common.earnedAmount;
                userStatsModel = new UserStatsModel(played, won,invested,earned);
                databaseReference11.child("INDIA11Users").child(uid).child("UserStats").setValue(userStatsModel);
                break;
        }

    }

    private void joinLeague(int count) {
        CID = Common.avlContestId;
        FID = uid;
        UID = Common.userNameID;
        SCID = Common.subContestId;
        TID = teamId;
        rank = 0;
        creationId = UID+TID;
        paid = "NO";
        JoinedLeagueModel joinedLeagueModel = new JoinedLeagueModel(UID, FID, CID, SCID, TID,creationId,paid,rank,obtainedPoints);
        databaseReference3.child("JoinedLeagues").child(CID).child(SCID).child(UID+TID).setValue(joinedLeagueModel);
        contestsId = Common.avlContestId;
        contestsName = Common.matchName;
        matchTime = Common.contestsTime;
        teamOne = Common.teamOneName;
        teamTwo = Common.teamTwoName;
        teamOneLogo = Common.teamOneLogo;
        teamTwoLogo = Common.teamTwoLogo;
        seriesName = Common.seriesName;
        winningAmount = "0";
        leagueType = lt;
        people = count+1;
        seriesId = Common.seriesId;
        AvailableContestsModel availableContestsModel = new AvailableContestsModel(contestsId, contestsName, leagueType, matchTime, teamOne, teamOneLogo, teamTwo, teamTwoLogo, winningAmount,seriesName,seriesId,people);
        DatabaseReference databaseReferenceMatch = FirebaseDatabase.getInstance().getReference();
        scid = Common.subContestId;
        winningPercent = wp;
        leagueType = lt;
        tId = teamId;
        cId = Common.avlContestId;
        entryFee = etFee;
        JoinedSubContestsModel joinedSubContestsModel = new JoinedSubContestsModel(scid, winningPercent, leagueType,totalSpots, tId,cId,entryFee, prizePool);
        //databaseReference4.child("UsersJoinedLeague").child(uid).child("Contests").child(contestsId).child("SubContests").child(Common.subContestId).setValue(joinedSubContestsModel);
        databaseReference4.child("UsersJoinedSubLeagues").child(Common.userNameID).child(Common.subContestId).setValue(joinedSubContestsModel);
        databaseReferenceMatch.child("UsersJoinedLeague").child(uid).child("Contests").child(contestsId).setValue(availableContestsModel);
        joined = joined+1;
        scid = Common.subContestId;
        leagueType = lt;
        winningPercent = wp;
        entryFee = etFee;
        SubContestsModel subContestsModel = new SubContestsModel(scid, tag, winningPercent, leagueType,totalSpots, leftSpots,totalWinners,multipleLeague
        ,entryFee, prizePool,joined);
        databaseReference5.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(Common.subContestId)
                .setValue(subContestsModel);
        if (Common.totalSpots <= 10){
            databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                    .child(Common.subContestId).child("SingleTeam").child(Common.userNameID).setValue(tId);
        }else {
            databaseReference6.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                    .child(Common.subContestId).child("MultipleTeam").child(Common.userNameID).child(TID).setValue(joinedLeagueModel);
        }
        databaseReference8.child("JoinedTeamIds").child(Common.avlContestId).child(Common.subContestId)
                .child(Common.userNameID).child(tId).setValue(tId);
        PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount
        ,teamOne, teamTwo, captain, viceCaptain);
        databaseReference9.child("JoinedUsersTeams").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .child(teamCode).setValue(playerValuesModel);
        Long amountW = Long.valueOf(binding.amt.getText().toString());
        databaseReferenceAddAmt.child("JoinedLeagueAmount").child("amount").setValue(amountW+Common.entryFee);
        DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
        databaseReference12.child("AvailableContests").child(Common.avlContestId).child("people").setValue(people);
        Toast.makeText(getContext(), "League joined successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();

    }

    private void loadTeams() {
        binding.availableTeamsRecycler.setHasFixedSize(true);
        binding.availableTeamsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerValuesModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PlayerValuesModel playerValuesModel = dataSnapshot.getValue(PlayerValuesModel.class);
                    playerValuesModelList.add(playerValuesModel);
                }
                SelectTeamAdapter selectTeamAdapter = new SelectTeamAdapter(getContext(),playerValuesModelList,SelectTeam.this::sendTeamData);
                binding.availableTeamsRecycler.setAdapter(selectTeamAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void sendTeamData(PlayerValuesModel playerValuesModel) {
            teamId = playerValuesModel.getTeamCode();
            binding.tid.setText("Team-"+teamId);
            AR = playerValuesModel.getAR();
            BR = playerValuesModel.getBR();
            BT = playerValuesModel.getBT();
            WK = playerValuesModel.getWK();
            leftCredit = playerValuesModel.getLeftCredit();
            captain = playerValuesModel.getCaptain();
            viceCaptain = playerValuesModel.getViceCaptain();
            teamOneCount = playerValuesModel.getTeamOneCount();
            teamTwoCount = playerValuesModel.getTeamTwoCount();
            teamCode = playerValuesModel.getTeamCode();
    }
}