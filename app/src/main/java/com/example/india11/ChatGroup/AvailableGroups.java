package com.example.india11.ChatGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.AvailableGroupAdapter;
import com.example.india11.Model.GroupModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentAvailableGroupsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableGroups extends Fragment {
    private FragmentAvailableGroupsBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uid;
    private List<GroupModel> groupModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAvailableGroupsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.swiperefresh.setRefreshing(false);
        uid = firebaseAuth.getCurrentUser().getUid();
        binding.recyclerviewMess.setHasFixedSize(true);
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.recyclerviewMess.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("INDIA11Users").child(uid).child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    groupModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        GroupModel groupModel = dataSnapshot.getValue(GroupModel.class);
                        groupModelList.add(groupModel);
                    }
                    AvailableGroupAdapter availableGroupAdapter = new AvailableGroupAdapter(getContext(),groupModelList);
                    binding.recyclerviewMess.setAdapter(availableGroupAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}