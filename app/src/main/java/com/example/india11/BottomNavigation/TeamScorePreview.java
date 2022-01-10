package com.example.india11.BottomNavigation;

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

import com.example.india11.Common;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.PreviewAdapters.AllRounderPreviewAdapter;
import com.example.india11.PreviewAdapters.AllRounderScoreAdapter;
import com.example.india11.PreviewAdapters.BatPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewModel;
import com.example.india11.PreviewAdapters.BatsmanPlayerScoreAdapter;
import com.example.india11.PreviewAdapters.BowlPreviewModel;
import com.example.india11.PreviewAdapters.BowlerPreviewAdapter;
import com.example.india11.PreviewAdapters.BowlerScoreAdapter;
import com.example.india11.PreviewAdapters.WicketPlayerScoreAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentTeamScorePreviewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeamScorePreview extends Fragment {

    private FragmentTeamScorePreviewBinding binding;
    DatabaseReference databaseReference,databaseReference2,databaseReference3;
    String uid;
    Dialog loading;
    FirebaseAuth firebaseAuth;
    private List<WicketPreviewModel> wicketPreviewModelList = new ArrayList<>();
    private List<BatPreviewModel> batPreviewModelList = new ArrayList<>();
    private List<BowlPreviewModel> bowlPreviewModelList = new ArrayList<>();
    private List<AllPreviewModel> allPreviewModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeamScorePreviewBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = Common.firebaseId;
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        loadWicketKeepers();
        loadBatsman();
        loadAllRounders();
        loadBowlers();
        binding.username.setText(Common.user);
        binding.tid.setText("T"+Common.tid);
        databaseReference3.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId)
                .child(Common.creationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String obtainedPoints = snapshot.child("obtainedPoints").getValue().toString();
                    String rank = snapshot.child("rank").getValue().toString();
                    binding.score.setText(obtainedPoints+" Points");
                    binding.rank.setText("Rank: "+rank);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private void loadWicketKeepers() {
        binding.wicketRecycler.setHasFixedSize(true);
        //binding.wicketRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.wicketRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("WicketKeeper")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            wicketPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                WicketPreviewModel wicketPreviewModel = dataSnapshot.getValue(WicketPreviewModel.class);
                                wicketPreviewModelList.add(wicketPreviewModel);
                            }
                            WicketPlayerScoreAdapter wicketPreviewAdapter = new WicketPlayerScoreAdapter(getContext(),wicketPreviewModelList);
                            binding.wicketRecycler.setAdapter(wicketPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadBatsman() {
        binding.batsmanRecycler.setHasFixedSize(true);
        //binding.batsmanRecycler.setLayoutManager(new GridLayoutManager(getContext(),5));
        binding.batsmanRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Batsman")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            batPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                BatPreviewModel batPreviewModel = dataSnapshot.getValue(BatPreviewModel.class);
                                batPreviewModelList.add(batPreviewModel);
                            }
                            BatsmanPlayerScoreAdapter batPreviewAdapter = new BatsmanPlayerScoreAdapter(getContext(),batPreviewModelList);
                            binding.batsmanRecycler.setAdapter(batPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadAllRounders() {
        binding.allRecycler.setHasFixedSize(true);
        //binding.allRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        binding.allRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("AllRounder")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            allPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                AllPreviewModel allPreviewModel = dataSnapshot.getValue(AllPreviewModel.class);
                                allPreviewModelList.add(allPreviewModel);
                            }
                            AllRounderScoreAdapter allRounderPreviewAdapter = new AllRounderScoreAdapter(getContext(),allPreviewModelList);
                            binding.allRecycler.setAdapter(allRounderPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadBowlers() {
        binding.bowlRecycler.setHasFixedSize(true);
        //binding.bowlRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        binding.bowlRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Bowler")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            bowlPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                BowlPreviewModel bowlPreviewModel = dataSnapshot.getValue(BowlPreviewModel.class);
                                bowlPreviewModelList.add(bowlPreviewModel);
                            }
                            BowlerScoreAdapter bowlerPreviewAdapter = new BowlerScoreAdapter(getContext(),bowlPreviewModelList);
                            binding.bowlRecycler.setAdapter(bowlerPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}