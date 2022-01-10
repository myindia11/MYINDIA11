package com.example.india11.BottomNavigation;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.india11.Adapters.ContestsPagerAdapter;
import com.example.india11.Adapters.JoinedMatchAdapter;
import com.example.india11.ContestsFragments.Contests;
import com.example.india11.ContestsFragments.MyContests;
import com.example.india11.ContestsFragments.MyContestsTeams;
import com.example.india11.ContestsFragments.MyToss;
import com.example.india11.MatchStatus.Completed;
import com.example.india11.MatchStatus.Live;
import com.example.india11.MatchStatus.Upcoming;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentMyMatchesBinding;
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

public class MyMatches extends Fragment {
    private FragmentMyMatchesBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;
    Dialog wallet;
    DatabaseReference databaseReference2,databaseReference3;
    String tBalance,bBalance,wBalance;
    TextView totalBalance, bonusBalance, winningBalance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyMatchesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        showBalance();
        initViews();
        return view;
    }

    private void initViews() {
        setupViewPager(binding.pager);
        binding.tabLayout.setupWithViewPager(binding.pager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ContestsPagerAdapter contestsPagerAdapter = new ContestsPagerAdapter(getChildFragmentManager());
        contestsPagerAdapter.addFragment(new Upcoming(),"Upcoming");
        contestsPagerAdapter.addFragment(new Live(),"Live");
        contestsPagerAdapter.addFragment(new Completed(),"Completed");
        viewPager.setAdapter(contestsPagerAdapter);
    }

    private void showBalance() {
        databaseReference.child("INDIA11Users").child(UID).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("totalBalance").getValue().toString();
                totalBalance.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference2.child("INDIA11Users").child(UID).child("BonusBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bBalance = snapshot.child("bonusBalance").getValue().toString();
                bonusBalance.setText(convertValueInIndianCurrency(Long.valueOf(bBalance)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference3.child("INDIA11Users").child(UID).child("WinningBalance").addValueEventListener(new ValueEventListener() {
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