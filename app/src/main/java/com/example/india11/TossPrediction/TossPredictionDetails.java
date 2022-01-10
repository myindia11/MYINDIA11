package com.example.india11.TossPrediction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.india11.Adapters.TossSubContestAdapter;
import com.example.india11.Common;
import com.example.india11.Model.TossSubContestModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentTossPredictionDetailsBinding;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TossPredictionDetails extends Fragment {
    private FragmentTossPredictionDetailsBinding binding;
    DatabaseReference databaseReference, databaseReference2;
    String tossTime,uid,tBalance,bBalance,wBalance;
    private List<TossSubContestModel> tossSubContestModelList = new ArrayList<>();
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    long diff;
    long milliseconds;
    Dialog tc;
    ImageView cancel;
    Dialog wallet;
    TextView totalBalance, bonusBalance, winningBalance;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTossPredictionDetailsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        tc = new Dialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        tc.setContentView(R.layout.toss_tandc_layout);
        tc.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tc.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tc.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        tc.getWindow().setGravity(Gravity.CENTER);
        binding.tossTc.setOnClickListener(view1 -> {
            tc.show();
        });
        cancel = tc.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> {
            tc.dismiss();
        });
        wallet = new Dialog(getContext());
        wallet.setContentView(R.layout.top_dialog_for_wallet);
        wallet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        wallet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wallet.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        wallet.getWindow().setGravity(Gravity.TOP);
        binding.wallet.setOnClickListener(view1 -> {
            wallet.show();
        });
        showBalance();
        totalBalance = wallet.findViewById(R.id.total);
        bonusBalance = wallet.findViewById(R.id.bonus);
        winningBalance = wallet.findViewById(R.id.winning);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("AvailableContests").child(Common.avlContestId).child("TossPrediction")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            tossTime = snapshot.child("tossTime").getValue().toString();
                            Common.tossTime = tossTime;
                            Date endDate;
                            try {
                                endDate = simpleDateFormat.parse(tossTime);
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        loadContest();
        return view;
    }

    private void loadContest() {
        binding.tossRecycler.setHasFixedSize(true);
        binding.tossRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("TossPrediction")
                .child("TossSubContest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tossSubContestModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TossSubContestModel tossSubContestModel = dataSnapshot.getValue(TossSubContestModel.class);
                        tossSubContestModelList.add(tossSubContestModel);
                    }
                    TossSubContestAdapter tossSubContestAdapter = new TossSubContestAdapter(getContext(),tossSubContestModelList);
                    binding.tossRecycler.setAdapter(tossSubContestAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showBalance() {
        DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference6 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference5.child("INDIA11Users").child(uid).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("totalBalance").getValue().toString();
                totalBalance.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference6.child("INDIA11Users").child(uid).child("BonusBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bBalance = snapshot.child("bonusBalance").getValue().toString();
                bonusBalance.setText(convertValueInIndianCurrency(Long.valueOf(bBalance)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference7.child("INDIA11Users").child(uid).child("WinningBalance").addValueEventListener(new ValueEventListener() {
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