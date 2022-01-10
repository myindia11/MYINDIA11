package com.example.india11.ChatGroup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Adapters.ReferralConnectionAdapter;
import com.example.india11.Adapters.SelecteGroupMemberAdapter;
import com.example.india11.Common;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentSelectGroupMembersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class SelectGroupMembers extends Fragment {
    private FragmentSelectGroupMembersBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<ReferralConnectionModel> referralConnectionModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String uid;
    Dialog toastDialog;
    TextView toastMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentSelectGroupMembersBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.gName.setText(Common.groupName);
        uid = firebaseAuth.getCurrentUser().getUid();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        binding.groupMembersRecycler.setHasFixedSize(true);
        binding.groupMembersRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("INDIA11Users").child(uid).child("ReferralConnections")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            referralConnectionModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ReferralConnectionModel referralConnectionModel = dataSnapshot.getValue(ReferralConnectionModel.class);
                                referralConnectionModelList.add(referralConnectionModel);
                            }
                            SelecteGroupMemberAdapter referralConnectionAdapter = new SelecteGroupMemberAdapter(getContext(),referralConnectionModelList);
                            binding.groupMembersRecycler.setAdapter(referralConnectionAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.confirm.setOnClickListener(view1 -> {
            toastMessage.setText(Common.groupName+" Successfully created.");
            toastDialog.show();
            final Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer2.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("ChatGroups").child(Common.groupId).child("groupStatus").setValue("YES");
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
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