package com.example.india11.MatchStatus;

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
import android.widget.TextView;

import com.example.india11.Adapters.CompletedMatchAdapter;
import com.example.india11.Adapters.JoinedMatchAdapter;
import com.example.india11.Common;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentCompletedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Completed extends Fragment {
    private FragmentCompletedBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;
    private CompletedMatchAdapter joinedSubContestsAdapter;
    public List<AvailableContestsModel> availableContestsModel = new ArrayList<>();
    DatabaseReference databaseReference2,databaseReference3;
    Dialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentCompletedBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
        Common.matchStatus = "Completed";
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        binding.noTagLayout.setVisibility(View.VISIBLE);
        loading.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        binding.myContestsRecycler.setHasFixedSize(true);
        binding.myContestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("UsersJoinedLeague").child(UID).child("Contests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.noTagLayout.setVisibility(View.GONE);
                    availableContestsModel.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AvailableContestsModel joinedSubContestsModel = dataSnapshot.getValue(AvailableContestsModel.class);
                        availableContestsModel.add(joinedSubContestsModel);
                    }
                    joinedSubContestsAdapter = new CompletedMatchAdapter(getContext(),availableContestsModel);
                    binding.myContestsRecycler.setAdapter(joinedSubContestsAdapter);
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
       return view;
    }
}