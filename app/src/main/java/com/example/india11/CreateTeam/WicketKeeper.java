package com.example.india11.CreateTeam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.WicketKeeperAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentWicketKeeperBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WicketKeeper extends Fragment {
    private FragmentWicketKeeperBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;
    List<PlayersListModel> playersListModelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWicketKeeperBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        UID = firebaseAuth.getCurrentUser().getUid();
        loadWicketKeepersList();
        return view;
    }

    private void loadWicketKeepersList() {
        binding.wicketRecycler.setHasFixedSize(true);
        binding.wicketRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
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