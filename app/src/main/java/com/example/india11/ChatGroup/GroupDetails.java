package com.example.india11.ChatGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.GroupMembersAdapter;
import com.example.india11.Common;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentGroupDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class GroupDetails extends Fragment {
    private FragmentGroupDetailsBinding binding;
    DatabaseReference databaseReference,databaseReference1;
    private List<ReferralConnectionModel> referralConnectionModelList = new ArrayList<>();
    DatabaseReference databaseReference2,databaseReference3;
    FirebaseAuth firebaseAuth;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupDetailsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.collapsingToolbar.setTitle(Common.groupName);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        binding.membersRecycler.setHasFixedSize(true);
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        binding.membersRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("ChatGroups").child(Common.groupId).child("GroupMembers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            referralConnectionModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ReferralConnectionModel referralConnectionModel = dataSnapshot.getValue(ReferralConnectionModel.class);
                                referralConnectionModelList.add(referralConnectionModel);
                            }
                            GroupMembersAdapter groupMembersAdapter = new GroupMembersAdapter(getContext(),referralConnectionModelList);
                            binding.membersRecycler.setAdapter(groupMembersAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference1.child("ChatGroups").child(Common.groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String createdBy = snapshot.child("groupAdmin").getValue().toString();
                    String createdOn = snapshot.child("groupCreatedTime").getValue().toString();
                    String gid = snapshot.child("groupId").getValue().toString();
                    binding.gid.setText("GID: "+gid);
                    binding.created.setText(createdBy+", "+createdOn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.leave.setOnClickListener(view1 -> {
            databaseReference2.child("ChatGroups").child(Common.groupId).child("GroupMembers").child(uid).removeValue();
        });
        binding.addMembers.setOnClickListener(view1 -> {
            Fragment add = new SelectGroupMembers();
            loadFragment(add,"SelectGroupMembers");
        });
        return view;
    }
    private void loadFragment(Fragment fragment, String tag) {
        executorService.execute(() -> {
            if (fragment != null) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(tag).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
            });
        });
    }
}