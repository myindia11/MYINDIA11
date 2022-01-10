package com.example.india11.Contests;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.india11.Activities.CreateTeamActivity;
import com.example.india11.Adapters.ContestsPagerAdapter;
import com.example.india11.Common;
import com.example.india11.ContestsFragments.Contests;
import com.example.india11.ContestsFragments.MyContests;
import com.example.india11.ContestsFragments.MyContestsTeams;
import com.example.india11.ContestsFragments.MyToss;
import com.example.india11.CreateTeam.CreatePlayerTeam;
import com.example.india11.Model.InitialCreateTeamModel;
import com.example.india11.Model.IsTeamCreatedModel;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;
import com.example.india11.TossPrediction.TossPredictionDetails;
import com.example.india11.databinding.FragmentContestsDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class ContestsDetails extends Fragment {
    private FragmentContestsDetailsBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    Dialog wallet;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5,databaseReference6;
    FirebaseAuth firebaseAuth;
    String uid,tBalance,bBalance,wBalance,tCode;
    TextView totalBalance, bonusBalance, winningBalance;
    double Credits = 100.0;
    int wk = 0;
    int bt = 0;
    int ar = 0;
    int br = 0;
    int teamOneCount = 0;
    int teamTwoCount = 0;
    Integer WK,BT,AR,BR;
    String teamOne, teamTwo, captain, viceCaptain;
    long diff;
    long milliseconds;
    int teamName = 0;
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    String currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentContestsDetailsBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
       binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        currentDate = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(new Date());
       firebaseAuth = FirebaseAuth.getInstance();
       uid = firebaseAuth.getCurrentUser().getUid();
       databaseReference5 = FirebaseDatabase.getInstance().getReference();
       databaseReference4 = FirebaseDatabase.getInstance().getReference();
       databaseReference6 = FirebaseDatabase.getInstance().getReference();
       binding.contestsName.setText(Common.contestsName);
       binding.remainTime.setText(Common.contestsTime);
        Glide.with(getContext()).load(Common.teamOneLogo).into(binding.teamOneLogo);
        Glide.with(getContext()).load(Common.teamTwoLogo).into(binding.teamTwoLogo);
        binding.teamOne.setText(Common.teamOneName);
        binding.teamTwo.setText(Common.teamTwoName);
        initViews();
        binding.createTeam.setOnClickListener(view1 -> {
            Common.buttonType = "Create";
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
            /*Fragment createTeam = new CreatePlayerTeam();
            loadFragment(createTeam,"CreatePlayerTeam");*/
            Intent intent = new Intent(getActivity(), CreateTeamActivity.class);
            getActivity().startActivity(intent);
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
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
        Date endDate;
        try {
            endDate = simpleDateFormat.parse(Common.contestsTime);
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
        binding.tossJoin.setOnClickListener(view1 -> {
            Fragment fragment = new TossPredictionDetails();
            loadFragment(fragment,"TossPredictionDetails");
        });
       return view;
    }
    private void initViews() {
        setupViewPager(binding.pager);
        binding.tabLayout.setupWithViewPager(binding.pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ContestsPagerAdapter contestsPagerAdapter = new ContestsPagerAdapter(getChildFragmentManager());
        contestsPagerAdapter.addFragment(new Contests(),"Contests");
        contestsPagerAdapter.addFragment(new MyContests(),"My Contests");
        contestsPagerAdapter.addFragment(new MyContestsTeams(),"My Teams");
        viewPager.setAdapter(contestsPagerAdapter);
    }
    private void loadFragment(Fragment fragment, String tag) {
        executorService.execute(() -> {
            if (fragment != null) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(tag).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
            });
        });
    }
    private void showBalance() {
        databaseReference.child("INDIA11Users").child(uid).child("TotalBalance").addValueEventListener(new ValueEventListener() {
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
    public String convertValueInIndianCurrency(Long amount)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}