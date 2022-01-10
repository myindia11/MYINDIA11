package com.example.india11.PlayersList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.BowlListAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentBowlListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BowlList extends Fragment {
    private FragmentBowlListBinding binding;
    ArrayList<PlayersListModel> playersListModelList1 = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBowlListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("Players").child("Bowler")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList1.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList1.add(playersListModel);
                        }
                        BowlListAdapter bowlerAdapter = new BowlListAdapter(getApplicationContext(),playersListModelList1);
                        binding.bowlList.setAdapter(bowlerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return view;
    }
}