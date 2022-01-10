package com.example.india11.MoneyRelatedLayouts;

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

import com.example.india11.Adapters.WalletHistoryAdapter;
import com.example.india11.Model.WalletModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentWalletHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class WalletHistory extends Fragment {

    private FragmentWalletHistoryBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    private List<WalletModel> walletModelList = new ArrayList<>();
    Dialog wallet,loading;
    DatabaseReference databaseReference2,databaseReference3;
    String tBalance,bBalance,wBalance;
    TextView totalBalance, bonusBalance, winningBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletHistoryBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        binding.noTagLayout.setVisibility(View.VISIBLE);
        loading.show();
        loadHistory();
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

    private void loadHistory() {
        binding.walletRecycler.setHasFixedSize(true);
        binding.walletRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("INDIA11Users").child(uid).child("WalletHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.noTagLayout.setVisibility(View.GONE);
                    walletModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        WalletModel walletModel = dataSnapshot.getValue(WalletModel.class);
                        walletModelList.add(walletModel);
                    }
                    WalletHistoryAdapter walletHistoryAdapter = new WalletHistoryAdapter(walletModelList, getContext());
                    binding.walletRecycler.setAdapter(walletHistoryAdapter);
                    Collections.sort(walletModelList, new Comparator<WalletModel>() {
                        @Override
                        public int compare(WalletModel t1, WalletModel t2) {
                            return t2.getReferenceId().compareTo(t1.getReferenceId());
                        }
                    });
                    walletHistoryAdapter.notifyDataSetChanged();
                    loading.dismiss();
                }else {
                    binding.noTagLayout.setVisibility(View.VISIBLE);
                    loading.dismiss();
                }
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