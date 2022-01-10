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

import com.example.india11.Adapters.JoinedSubContestsAdapter;
import com.example.india11.Common;
import com.example.india11.Model.JoinedSubContestsModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentMyContestsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyContests extends Fragment {
    private FragmentMyContestsBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    private List<JoinedSubContestsModel> joinedSubContestsModelList = new ArrayList<>();
    Dialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentMyContestsBinding.inflate(inflater,container,false);
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
        binding.subRecycler.setHasFixedSize(true);
        binding.subRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("UsersJoinedSubLeagues").child(Common.userNameID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            joinedSubContestsModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                JoinedSubContestsModel joinedSubContestsModel = dataSnapshot.getValue(JoinedSubContestsModel.class);
                                joinedSubContestsModelList.add(joinedSubContestsModel);
                            }
                            JoinedSubContestsAdapter joinedSubContestsAdapter = new JoinedSubContestsAdapter(getContext(),joinedSubContestsModelList);
                            binding.subRecycler.setAdapter(joinedSubContestsAdapter);
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