package com.example.india11.ContestsFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.india11.Adapters.ContestsPagerAdapter;
import com.example.india11.Adapters.JoinedLeaderboardAdapter;
import com.example.india11.Adapters.PersonalLeaderboardAdapter;
import com.example.india11.Common;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentSubContestsDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubContestsDetails extends Fragment {
    private FragmentSubContestsDetailsBinding binding;
    String scid, tag, winningPercent, leagueType,totalSpots, leftSpots, uid;
    long entryFee, prizePool;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Dialog wallet;
    DatabaseReference databaseReference4,databaseReference2,databaseReference3,databaseReference6,databaseReference7;
    String tBalance,bBalance,wBalance,winners;
    TextView totalBalance, bonusBalance, winningBalance;
    String matchtime;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    List<JoinedLeagueModel> joinedLeagueModelList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubContestsDetailsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        initViews();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadContestsData();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        wallet = new Dialog(getContext());
        wallet.setContentView(R.layout.top_dialog_for_wallet);
        wallet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        wallet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wallet.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        wallet.getWindow().setGravity(Gravity.TOP);
        binding.wallet.setOnClickListener(view1 -> {
            wallet.show();
        });
        totalBalance = wallet.findViewById(R.id.total);
        bonusBalance = wallet.findViewById(R.id.bonus);
        winningBalance = wallet.findViewById(R.id.winning);
        showBalance();
        loadContestTime();
        binding.personalLeaderboardRecycler.setHasFixedSize(true);
        binding.personalLeaderboardRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference7.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    joinedLeagueModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        JoinedLeagueModel joinedLeagueModel = dataSnapshot.getValue(JoinedLeagueModel.class);
                        joinedLeagueModelList.add(joinedLeagueModel);
                    }
                    PersonalLeaderboardAdapter joinedLeaderboardAdapter = new PersonalLeaderboardAdapter(getContext(),joinedLeagueModelList);
                    binding.personalLeaderboardRecycler.setAdapter(joinedLeaderboardAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void loadContestTime() {
        databaseReference2.child("AvailableContests").child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchtime = snapshot.child("matchTime").getValue().toString();
                runTimer(matchtime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void runTimer(String matchtime) {
        Date endDate;
        try {
            endDate = simpleDateFormat.parse(matchtime);
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final long[] startTime = {System.currentTimeMillis()};
        diff = milliseconds - startTime[0];
        mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime[0] = startTime[0] -1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime[0]) / 1000;
                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //binding.remainTime.setText(daysLeft+"D "+hoursLeft+"H:"+minutesLeft+"M:"+secondsLeft+"S");
                if (!daysLeft.equals("0")){
                    binding.remainTime.setText(daysLeft+" Day");
                }else if (daysLeft.equals("0") && !hoursLeft.equals("0")){
                    binding.remainTime.setText(hoursLeft+" Hours");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && !minutesLeft.equals("0")){
                    binding.remainTime.setText(minutesLeft+" Minutes");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && minutesLeft.equals("0") && !secondsLeft.equals("0")){
                    binding.remainTime.setText(secondsLeft+" Seconds");
                }else {
                    binding.remainTime.setText("Live");
                }
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void loadContestsData() {
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("SubContests").child(Common.subContestId)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        prizePool = Long.parseLong(snapshot.child("prizePool").getValue().toString());
                        entryFee = Long.parseLong(snapshot.child("entryFee").getValue().toString());
                        totalSpots = snapshot.child("totalSpots").getValue().toString();
                        leftSpots = snapshot.child("joined").getValue().toString();
                        tag = snapshot.child("tag").getValue().toString();
                        winningPercent = snapshot.child("winningPercent").getValue().toString();
                        winners = snapshot.child("totalWinners").getValue().toString();
                        Common.totalWinners = winners;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initViews() {
        setupViewPager(binding.pager);
        binding.tabLayout.setupWithViewPager(binding.pager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ContestsPagerAdapter contestsPagerAdapter = new ContestsPagerAdapter(getChildFragmentManager());
        contestsPagerAdapter.addFragment(new ContestsWinningInfo(),"Winnings");
        contestsPagerAdapter.addFragment(new JoinedLeaderboard(),"Leaderboard");
        viewPager.setAdapter(contestsPagerAdapter);
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
    private void showBalance() {
        databaseReference4.child("INDIA11Users").child(uid).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("totalBalance").getValue().toString();
                totalBalance.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference2.child("INDIA11Users").child(uid).child("BonusBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bBalance = snapshot.child("bonusBalance").getValue().toString();
                bonusBalance.setText(convertValueInIndianCurrency(Long.valueOf(bBalance)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference3.child("INDIA11Users").child(uid).child("WinningBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wBalance = snapshot.child("winningBalance").getValue().toString();
                winningBalance.setText(convertValueInIndianCurrency(Long.valueOf(wBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}