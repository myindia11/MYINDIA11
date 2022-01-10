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

import com.example.india11.Adapters.RankInfoAdapter;
import com.example.india11.Common;
import com.example.india11.Model.RankInfoModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentContestsWinningInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContestsWinningInfo extends Fragment {
    private FragmentContestsWinningInfoBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    Dialog loading;
    List<RankInfoModel> rankInfoModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContestsWinningInfoBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadDetails();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        return view;
    }

    private void loadDetails() {
        binding.winningInfoRecycler.setHasFixedSize(true);
        binding.winningInfoRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("ContestWinningInfo").child(Common.subContestId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        rankInfoModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            RankInfoModel rankInfoModel = dataSnapshot.getValue(RankInfoModel.class);
                            rankInfoModelList.add(rankInfoModel);
                        }
                        RankInfoAdapter rankInfoAdapter = new RankInfoAdapter(getContext(),rankInfoModelList);
                        binding.winningInfoRecycler.setAdapter(rankInfoAdapter);
                        loading.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}