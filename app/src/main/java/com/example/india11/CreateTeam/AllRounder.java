package com.example.india11.CreateTeam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.AllRounderAdapter;
import com.example.india11.Adapters.SelectedPlayerLayoutAdapter;
import com.example.india11.Adapters.WicketKeeperAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.PreviewAdapters.AllRounderPreviewAdapter;
import com.example.india11.R;
import com.example.india11.databinding.FragmentAllRounderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRounder extends Fragment {
    private FragmentAllRounderBinding binding;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseAuth firebaseAuth;
    String UID;
    List<PlayersListModel> playersListModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllRounderBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        UID = firebaseAuth.getCurrentUser().getUid();
        loadAllroundersList();
        return view;
    }

    private void loadAllroundersList() {
        binding.allrounderRecycler.setHasFixedSize(true);
        binding.allrounderRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
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
                        binding.allrounderRecycler.setAdapter(allRounderAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}