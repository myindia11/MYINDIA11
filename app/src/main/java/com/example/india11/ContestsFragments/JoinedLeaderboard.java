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

import com.example.india11.Adapters.JoinedLeaderboardAdapter;
import com.example.india11.Common;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.Model.SubContestsModel;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.PreviewAdapters.AllRounderPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewModel;
import com.example.india11.PreviewAdapters.BowlPreviewModel;
import com.example.india11.PreviewAdapters.BowlerPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentJoinedLeaderboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JoinedLeaderboard extends Fragment {
    private FragmentJoinedLeaderboardBinding binding;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseAuth firebaseAuth;
    String uid;
    Dialog loading;
    List<JoinedLeagueModel> joinedLeagueModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJoinedLeaderboardBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadLeaderBoard();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        return view;
    }


    private void loadLeaderBoard() {
        binding.joinedRecycler.setHasFixedSize(true);
        binding.joinedRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                joinedLeagueModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    JoinedLeagueModel joinedLeagueModel = dataSnapshot.getValue(JoinedLeagueModel.class);
                    joinedLeagueModelList.add(joinedLeagueModel);
                }
                JoinedLeaderboardAdapter joinedLeaderboardAdapter = new JoinedLeaderboardAdapter(getContext(),joinedLeagueModelList);
                binding.joinedRecycler.setAdapter(joinedLeaderboardAdapter);
                binding.noTag.setVisibility(View.GONE);
                Collections.sort(joinedLeagueModelList, (item1, item2) -> item2.getObtainedPoints().compareTo(item1.getObtainedPoints()));
                joinedLeaderboardAdapter.notifyDataSetChanged();
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}