package com.example.india11.CreateTeam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.AllRounderAdapter;
import com.example.india11.Adapters.BatsmanAdapter;
import com.example.india11.Adapters.BowlerAdapter;
import com.example.india11.Adapters.WicketKeeperAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentSelectPlayersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayers extends Fragment {
    private FragmentSelectPlayersBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;
    List<PlayersListModel> playersListModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectPlayersBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        UID = firebaseAuth.getCurrentUser().getUid();
        loadWicketKeepersList();
        loadBatsmanList();
        loadAllroundersList();
        loadBowlersList();
        return view;
    }

    private void loadBowlersList() {
        binding.bowlRecycler.setHasFixedSize(true);
        binding.bowlRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("Players").child("Bowler")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList.add(playersListModel);
                        }
                        BowlerAdapter bowlerAdapter = new BowlerAdapter(getContext(),playersListModelList);
                        binding.bowlRecycler.setAdapter(bowlerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllroundersList() {
        binding.allRecycler.setHasFixedSize(true);
        binding.allRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("Players").child("AllRounder")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList.add(playersListModel);
                        }
                        AllRounderAdapter allRounderAdapter = new AllRounderAdapter(getContext(),playersListModelList);
                        binding.allRecycler.setAdapter(allRounderAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadBatsmanList() {
        binding.batRecycler.setHasFixedSize(true);
        binding.batRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("Players").child("Batsman")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList.add(playersListModel);
                        }
                        BatsmanAdapter batsmanAdapter = new BatsmanAdapter(getContext(),playersListModelList);
                        binding.batRecycler.setAdapter(batsmanAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadWicketKeepersList() {
        binding.wicketRecycler.setHasFixedSize(true);
        binding.wicketRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("Players").child("WicketKeeper")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList.add(playersListModel);
                        }
                        WicketKeeperAdapter wicketKeeperAdapter = new WicketKeeperAdapter(getContext(),playersListModelList);
                        binding.wicketRecycler.setAdapter(wicketKeeperAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}