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

import com.example.india11.Adapters.JoinedTossAdapter;
import com.example.india11.Common;
import com.example.india11.Model.JoinedTossModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentMyTossBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyToss extends Fragment {
    private FragmentMyTossBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    private List<JoinedTossModel> joinedTossModelList = new ArrayList<>();
    Dialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentMyTossBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadContests();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
       return view;
    }

    private void loadContests() {
        binding.tossRecycler.setHasFixedSize(true);
        binding.tossRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("UserJoinedToss").child(uid).child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    joinedTossModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        JoinedTossModel joinedTossModel = dataSnapshot.getValue(JoinedTossModel.class);
                        joinedTossModelList.add(joinedTossModel);
                    }
                    JoinedTossAdapter joinedTossAdapter = new JoinedTossAdapter(getContext(),joinedTossModelList);
                    binding.tossRecycler.setAdapter(joinedTossAdapter);
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