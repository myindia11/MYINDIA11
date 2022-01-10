package com.example.india11.BottomNavigation;

import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.india11.Adapters.ReferralConnectionAdapter;
import com.example.india11.ChatGroup.AvailableGroups;
import com.example.india11.ChatGroup.CreateGroup;
import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentMyConnectionsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class MyConnections extends Fragment {
    private FragmentMyConnectionsBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String uid;
    Dialog loading;
    private List<ReferralConnectionModel> referralConnectionModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyConnectionsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.connectionRecycler.setHasFixedSize(true);
        binding.connectionRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        binding.createGroup.setOnClickListener(view1 -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String groupId=formatter.format(date);
            groupId=groupId.replace("/","").replace(":","").replace(" ","");
            String gid="MI"+groupId;
            Common.groupId = gid;
            Fragment create = new CreateGroup();
            loadFragment(create,"CreateGroup");
        });
        binding.groups.setOnClickListener(view1 -> {
            Fragment fragment = new AvailableGroups();
            loadFragment(fragment,"AvailableGroups");
        });
        binding.noTagLayout.setVisibility(View.VISIBLE);
        loadConnections();
        binding.sendEmail.setOnClickListener(view1 -> {
            String email = binding.emailId.getText().toString();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("AppLink").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String link = snapshot.child("link").getValue().toString();
                        Common.link = link;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if (email.isEmpty()){
                binding.emailId.requestFocus();
                binding.emailId.setError("Email");
            }else {
                loading.show();
                String subject = "MYINDIA11 download link.";
                String body="Hi, "+ Common.profileName+"\nSharing you the MYINDIA11 Fantasy Application link to download." +
                        "\nClick on the provided link to download the application and use my referral code "+Common.userNameID+
                        "\nYou will get Rs.125 as Bonus Balance which you can use to join the league."+
                        "\nURL: "+Common.link+
                        "\nWeb: "+"www.myindia11.in"+
                        "\n\nPlay more, Win more."+
                        "\n\nRegards,"+"\nMYINDIA11";
                JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),email,subject,body);
                javaMailAPI.execute();
                loading.dismiss();
                Toast.makeText(getContext(), "Email sent successfully!", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

    private void loadConnections() {
        databaseReference.child("INDIA11Users").child(uid).child("ReferralConnections")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.noTagLayout.setVisibility(View.GONE);
                            referralConnectionModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ReferralConnectionModel referralConnectionModel = dataSnapshot.getValue(ReferralConnectionModel.class);
                                referralConnectionModelList.add(referralConnectionModel);
                            }
                            ReferralConnectionAdapter referralConnectionAdapter = new ReferralConnectionAdapter(getContext(),referralConnectionModelList);
                            binding.connectionRecycler.setAdapter(referralConnectionAdapter);
                            loading.dismiss();
                        }else {
                            loading.dismiss();
                            binding.noTagLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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