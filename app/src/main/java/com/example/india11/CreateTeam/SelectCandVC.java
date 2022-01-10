package com.example.india11.CreateTeam;

import android.annotation.SuppressLint;
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

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.CVCModels.AllrounderModel;
import com.example.india11.CVCModels.BatsmanModel;
import com.example.india11.CVCModels.BowlerModel;
import com.example.india11.CVCModels.WicketModel;
import com.example.india11.CandVCAdapters.CVCAllAdapter;
import com.example.india11.CandVCAdapters.CVCBatAdapter;
import com.example.india11.CandVCAdapters.CVCBowlAdapter;
import com.example.india11.CandVCAdapters.CVCWiketAdapter;
import com.example.india11.Common;
import com.example.india11.Contests.ContestsDetails;
import com.example.india11.Model.IsTeamCreatedModel;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentSelectCandVCBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class SelectCandVC extends Fragment {
    private FragmentSelectCandVCBinding binding;
    DatabaseReference databaseReferenceWicket,databaseReferenceBat,databaseReferenceBowl,databaseReference4,databaseReferenceAll,databaseReference,databaseReference6;
    private List<WicketModel> wicketModelList = new ArrayList<>();
    private List<BatsmanModel> batsmanModelList = new ArrayList<>();
    private List<AllrounderModel> allrounderModelList = new ArrayList<>();
    private List<BowlerModel> bowlerModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    String uid;
    Dialog delete;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String matchtime,captain,viceCaptain;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    String leftCredit,teamId,players,teamOneCount,teamTwoCount;
    int wicket, bat, bowl, ar;
    LinearLayout deleteButton, continueButton;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectCandVCBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        delete = new Dialog(getContext());
        delete.setContentView(R.layout.team_discard_layout);
        delete.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        delete.getWindow().setGravity(Gravity.BOTTOM);
        deleteButton = delete.findViewById(R.id.delete);
        continueButton = delete.findViewById(R.id.continueTeam);

        databaseReferenceAll = FirebaseDatabase.getInstance().getReference();
        databaseReferenceBat = FirebaseDatabase.getInstance().getReference();
        databaseReferenceBowl = FirebaseDatabase.getInstance().getReference();
        databaseReferenceWicket = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadWicketKeepers();
        loadBatsmen();
        loadAllrounders();
        loadBowlers();
        loadCreditInfo();
        return view;
    }
    private void loadCreditInfo() {
        databaseReference4.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leftCredit = snapshot.child("leftCredit").getValue().toString();
                teamId = snapshot.child("teamCode").getValue().toString();
                wicket = Integer.parseInt(snapshot.child("wk").getValue().toString());
                bat = Integer.parseInt(snapshot.child("bt").getValue().toString());
                bowl = Integer.parseInt(snapshot.child("br").getValue().toString());
                ar = Integer.parseInt(snapshot.child("ar").getValue().toString());
                teamOneCount = snapshot.child("teamOneCount").getValue().toString();
                teamTwoCount = snapshot.child("teamTwoCount").getValue().toString();
                captain = snapshot.child("captain").getValue().toString();
                viceCaptain = snapshot.child("viceCaptain").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBowlers() {
        binding.bowlerRecycler.setLayoutFrozen(true);
        binding.bowlerRecycler.setHasFixedSize(true);
        binding.bowlerRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReferenceBowl.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Bowler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //playersListModelList.clear();
                bowlerModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BowlerModel bowlerModel = dataSnapshot.getValue(BowlerModel.class);
                    bowlerModelList.add(bowlerModel);
                }
                CVCBowlAdapter cvcBowlAdapter = new CVCBowlAdapter(getContext(),bowlerModelList);
                binding.bowlerRecycler.setAdapter(cvcBowlAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAllrounders() {
        binding.allRecycler.setLayoutFrozen(true);
        binding.allRecycler.setHasFixedSize(true);
        binding.allRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReferenceAll.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("AllRounder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //playersListModelList.clear();
                allrounderModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AllrounderModel allrounderModel = dataSnapshot.getValue(AllrounderModel.class);
                    allrounderModelList.add(allrounderModel);

                }
                CVCAllAdapter cvcAllAdapter = new CVCAllAdapter(getContext(),allrounderModelList);
                binding.allRecycler.setAdapter(cvcAllAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBatsmen() {
        binding.batsRecycler.setLayoutFrozen(true);
        binding.batsRecycler.setHasFixedSize(true);
        binding.batsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReferenceBat.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Batsman").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //playersListModelList.clear();
                batsmanModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BatsmanModel batsmanModel = dataSnapshot.getValue(BatsmanModel.class);
                    batsmanModelList.add(batsmanModel);

                }
                CVCBatAdapter cvcBatAdapter = new CVCBatAdapter(getContext(),batsmanModelList);
                binding.batsRecycler.setAdapter(cvcBatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadWicketKeepers() {
        binding.wicketRecycler.setLayoutFrozen(true);
        binding.wicketRecycler.setHasFixedSize(true);
        binding.wicketRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReferenceWicket.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("WicketKeeper").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //playersListModelList.clear();
                wicketModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    WicketModel wicketModel = dataSnapshot.getValue(WicketModel.class);
                    wicketModelList.add(wicketModel);

                }
                CVCWiketAdapter cvcWiketAdapter = new CVCWiketAdapter(getContext(),wicketModelList);
                binding.wicketRecycler.setAdapter(cvcWiketAdapter);
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