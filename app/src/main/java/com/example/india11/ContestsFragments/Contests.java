package com.example.india11.ContestsFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.SubContestsAdapter;
import com.example.india11.Common;
import com.example.india11.Model.SubContestsModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentContestsBinding;
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

public class Contests extends Fragment {
    private FragmentContestsBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;
    List<SubContestsModel> subContestsModelList = new ArrayList<>();
    private SubContestsAdapter subContestsAdapter;
    Dialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentContestsBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
       databaseReference = FirebaseDatabase.getInstance().getReference();
       firebaseAuth = FirebaseAuth.getInstance();
       UID = firebaseAuth.getCurrentUser().getUid();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        loading.show();
       loadSubContests();
       binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               subContestsAdapter.notifyDataSetChanged();
               binding.swipeRefresh.setRefreshing(false);

           }
       });
       binding.tag.setVisibility(View.VISIBLE);
       DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
       databaseReference2.child("AvailableContests").child(Common.avlContestId).child("MatchStatus")
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           String s = snapshot.child("status").getValue().toString();
                           if (s.equals("Live")){
                               binding.tag.setVisibility(View.VISIBLE);
                               binding.subContestRecycler.setVisibility(View.GONE);
                               binding.tag.setText("Match has started.");
                           }else if (s.equals("Completed")){
                               binding.tag.setVisibility(View.VISIBLE);
                               binding.subContestRecycler.setVisibility(View.GONE);
                               binding.tag.setText("Match has finished.");
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
       return view;
    }

    private void loadSubContests() {
        binding.subContestRecycler.setHasFixedSize(true);
        binding.subContestRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("SubContests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subContestsModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SubContestsModel subContestsModel = dataSnapshot.getValue(SubContestsModel.class);
                    subContestsModelList.add(subContestsModel);
                }
                subContestsAdapter = new SubContestsAdapter(getContext(),subContestsModelList);
                binding.subContestRecycler.setAdapter(subContestsAdapter);
                Collections.sort(subContestsModelList, new Comparator<SubContestsModel>() {
                    @Override
                    public int compare(SubContestsModel item1, SubContestsModel item2) {
                        return item1.getEntryFee().compareTo(item2.getEntryFee());
                    }
                });
                loading.dismiss();
                binding.tag.setVisibility(View.GONE);
                binding.fee.setOnClickListener(view -> {
                    Collections.sort(subContestsModelList, new Comparator<SubContestsModel>() {
                        @Override
                        public int compare(SubContestsModel item1, SubContestsModel item2) {
                            return item2.getEntryFee().compareTo(item1.getEntryFee());
                        }
                    });
                });
                binding.size.setOnClickListener(view -> {
                    Collections.sort(subContestsModelList, new Comparator<SubContestsModel>() {
                        @Override
                        public int compare(SubContestsModel item1, SubContestsModel item2) {
                            return item1.getTotalSpots().compareTo(item2.getTotalSpots());
                        }
                    });
                });

                subContestsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}