package com.example.india11.ContestsFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.india11.CandVCAdapters.MyTeamAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentMyContestsTeamsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyContestsTeams extends Fragment {
    private FragmentMyContestsTeamsBinding binding;
    DatabaseReference databaseReference,databaseReference2;
    private List<PlayerValuesModel> playerValuesModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    String uid;
    Dialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyContestsTeamsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Glide.with(getContext()).load(Common.teamOneLogo).into(binding.teamOneLogo);
        Glide.with(getContext()).load(Common.teamTwoLogo).into(binding.teamTwoLogo);
        binding.teamOneName.setText(Common.teamOneName);
        binding.teamTwoName.setText(Common.teamTwoName);
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("AvailableContests").child(Common.avlContestId).child("MatchStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("status").getValue().toString();
                    if (status.equals("Upcoming")){
                        binding.matchStatus.setText(status);
                        binding.matchStatus.setTextColor(Color.parseColor("#000000"));
                    }else if (status.equals("Live")){
                        binding.matchStatus.setText(status);
                        binding.matchStatus.setTextColor(Color.parseColor("#2C5E1A"));
                    }else if (status.equals("Completed")){
                        binding.matchStatus.setText(status);
                        binding.matchStatus.setTextColor(Color.parseColor("#B22222"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadTeams();
        return view;
    }

    private void loadTeams() {
        binding.teamsRecycler.setHasFixedSize(true);
        binding.teamsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    playerValuesModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PlayerValuesModel playerValuesModel = dataSnapshot.getValue(PlayerValuesModel.class);
                        playerValuesModelList.add(playerValuesModel);
                    }
                    MyTeamAdapter myTeamAdapter = new MyTeamAdapter(getContext(),playerValuesModelList);
                    binding.teamsRecycler.setAdapter(myTeamAdapter);
                    loading.dismiss();
                }else {
                    loading.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}