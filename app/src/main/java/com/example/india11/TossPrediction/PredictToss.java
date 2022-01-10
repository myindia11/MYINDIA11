package com.example.india11.TossPrediction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Activities.HomeActivity;
import com.example.india11.Common;
import com.example.india11.Model.JoinedTossModel;
import com.example.india11.Model.TossJoinModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentPredictTossBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class PredictToss extends Fragment {
    private FragmentPredictTossBinding binding;
    String userId, contestId, subContestId, winningStatus, selectedTeam, actualTeam,paidStatus,userFid;
    Long entryFee;
    Long totalBalance;
    String uid;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference2,databaseReference3;
    String tossId;
    Long entry, prize;
    Integer spot,joined,total;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPredictTossBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        Common.selectedTeam = "None";
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        binding.selectedTeam.setText(Common.selectedTeam);
        binding.entry.setText(convertValueInIndianCurrency(Common.entryFee));
        binding.teamOne.setText(Common.teamOneName);
        binding.teamTwo.setText(Common.teamTwoName);
        Glide.with(getContext()).load(Common.teamOneLogo).into(binding.teamOneLogo);
        Glide.with(getContext()).load(Common.teamTwoLogo).into(binding.teamTwoLogo);
        DatabaseReference databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference6.child("AvailableContests").child(Common.avlContestId).child("TossPrediction")
                .child("TossSubContest").child(Common.tossCId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                joined = Integer.valueOf(snapshot.child("joined").getValue().toString());
                total = Integer.valueOf(snapshot.child("totalSpots").getValue().toString());
                Common.joined = joined;
                Common.total = total;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.teamOneLogo.setOnClickListener(view1 -> {
            Common.selectedTeam = Common.teamOneName;
            binding.teamOneTick.setVisibility(View.VISIBLE);
            binding.teamTwoTick.setVisibility(View.GONE);
            binding.selectedTeam.setText(Common.selectedTeam);
        });
        binding.teamTwoLogo.setOnClickListener(view1 -> {
            Common.selectedTeam = Common.teamTwoName;
            binding.teamOneTick.setVisibility(View.GONE);
            binding.teamTwoTick.setVisibility(View.VISIBLE);
            binding.selectedTeam.setText(Common.selectedTeam);
        });
        binding.confirm.setOnClickListener(view1 -> {

            if (Common.selectedTeam.equals("None")){
                Toast.makeText(getContext(), "Please select a team!", Toast.LENGTH_SHORT).show();
            }else if (Common.walletBalance < Common.entryFee){
                Toast.makeText(getContext(), "You do not have sufficient balance to join.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }else if (Common.total == Common.joined){
                Toast.makeText(getContext(), "League already full please join other league.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }else {
                updateWallet(Common.joined);
            }
        });
        return view;
    }

    private void updateWallet(Integer joined) {
        totalBalance = Common.walletBalance - Common.entryFee;
        databaseReference3.child("INDIA11Users").child(uid).child("TotalBalance").child("totalBalance").setValue(totalBalance).addOnSuccessListener(aVoid -> {
            joinLeague(joined);
        });

    }

    private void joinLeague(Integer joined) {
        contestId = Common.avlContestId;
        subContestId = Common.tossCId;
        userId = Common.userNameID;
        winningStatus = "Waiting";
        selectedTeam = Common.selectedTeam;
        actualTeam = "Waiting for toss.";
        paidStatus = "Pending";
        entryFee = Common.entryFee;
        userFid = uid;
        TossJoinModel tossJoinModel = new TossJoinModel(userId, contestId, subContestId, winningStatus, selectedTeam, actualTeam,paidStatus,userFid,entryFee);

        databaseReference.child("JoinedTossLeague").child(Common.avlContestId).child(Common.tossCId).child(userId)
                .setValue(tossJoinModel);
        tossId = Common.tossCId;
        prize = Common.tossPrize;
        entry = Common.entryFee;
        JoinedTossModel joinedTossModel = new JoinedTossModel(tossId, selectedTeam,entry, prize);
        databaseReference2.child("UserJoinedToss").child(uid).child(Common.avlContestId).child(tossId).setValue(joinedTossModel);
        Toast.makeText(getContext(), "Toss joined successfully!", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference();
        spot = joined;
        databaseReference5.child("AvailableContests").child(Common.avlContestId).child("TossPrediction").child("TossSubContest")
                .child(tossId).child("joined").setValue(spot+1);
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();

    }

    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}