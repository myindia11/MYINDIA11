package com.example.india11.MenuBarLayouts;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.india11.Adapters.NotificationAdapter;
import com.example.india11.Common;
import com.example.india11.Model.NotificationModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentNotificationBinding;
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

public class Notification extends Fragment {
    private FragmentNotificationBinding binding;
    Dialog wallet,loading;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4;
    FirebaseAuth firebaseAuth;
    String uid,tBalance,bBalance,wBalance;
    TextView totalBalance, bonusBalance, winningBalance;
    private List<NotificationModel> notificationModelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        wallet = new Dialog(getContext());
        wallet.setContentView(R.layout.top_dialog_for_wallet);
        wallet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        wallet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wallet.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        wallet.getWindow().setGravity(Gravity.TOP);
        binding.wallet.setOnClickListener(view1 -> {
            wallet.show();
        });
        binding.noTagLayout.setVisibility(View.VISIBLE);
        totalBalance = wallet.findViewById(R.id.total);
        bonusBalance = wallet.findViewById(R.id.bonus);
        winningBalance = wallet.findViewById(R.id.winning);
        showBalance();
        binding.notificationRecycler.setHasFixedSize(true);
        binding.notificationRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference4.child("INDIA11Users").child(uid).child("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.noTagLayout.setVisibility(View.GONE);
                    notificationModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                        notificationModelList.add(notificationModel);
                    }
                    NotificationAdapter notificationAdapter = new NotificationAdapter(notificationModelList);
                    binding.notificationRecycler.setAdapter(notificationAdapter);
                    loading.dismiss();
                }else {
                    loading.dismiss();
                    binding.noTagLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
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