package com.example.india11.ContestsFragments;

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

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Adapters.AlreadyJoinedTeamsAdapter;
import com.example.india11.Adapters.AvailableTeamsSwitchAdapter;
import com.example.india11.Common;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentChangeTeamBinding;
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

public class ChangeTeam extends Fragment implements AvailableTeamsSwitchAdapter.SendData {
    private FragmentChangeTeamBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    private List<PlayerValuesModel> playerValuesModelList = new ArrayList<>();
    private List<PlayerValuesModel> switchTeamsModelList = new ArrayList<>();
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4,databaseReference5;
    DatabaseReference databaseReference6,databaseReference7,databaseReference8,databaseReference9,databaseReference10,databaseReference11;
    FirebaseAuth firebaseAuth;
    String uid,newTid;
    String teamCode;
    Double leftCredit;
    Dialog toastDialog;
    TextView toastMessage;
    Integer WK,BT,AR,BR,teamOneCount,teamTwoCount;
    String teamOne, teamTwo, captain, viceCaptain;
    String UID, FID, CID, SCID, TID,creationId;
    Integer rank;
    Double obtainedPoints;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeTeamBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view12 -> requireActivity().onBackPressed());
        binding.heading.setText("Available teams to replace "+ "Team-"+Common.teamCode);
        binding.oldId.setText("Team-"+Common.teamCode);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReference9 = FirebaseDatabase.getInstance().getReference();
        databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference11 = FirebaseDatabase.getInstance().getReference();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadJoinedTeams();
        loadAvailableTeams();
        binding.joinBtn.setOnClickListener(view1 -> {
            if (binding.newId.getText().toString().equals("TEAMID")){
                toastMessage.setText("Please select a team.");
                toastDialog.show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
            }else {
                updateTeam();
            }

        });

        return view;
    }

    private void updateTeam() {
        databaseReference2.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId).child(Common.creationId)
                .child("tid").setValue(newTid);
        databaseReference3.child("JoinedTeamIds").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .child(Common.teamCode).removeValue();
        databaseReference4.child("JoinedTeamIds").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .child(newTid).setValue(newTid);
        databaseReference5.child("JoinedUsersTeams").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .child(Common.teamCode).removeValue();
        teamCode = Common.swTc;
        leftCredit = Common.creditLeft;
        WK = Common.wk;
        BT = Common.bt;
        AR = Common.ar;
        BR = Common.br;
        teamOneCount = Common.tOneC;
        teamTwoCount = Common.tTwoC;
        teamOne = Common.tOne;
        teamTwo = Common.tTwo;
        captain = Common.c;
        viceCaptain = Common.vc;
        PlayerValuesModel playerValuesModel = new PlayerValuesModel(teamCode,leftCredit,WK,BT,AR,BR,teamOneCount,teamTwoCount
                ,teamOne, teamTwo, captain, viceCaptain);
        databaseReference6.child("JoinedUsersTeams").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID)
                .child(teamCode).setValue(playerValuesModel);
        databaseReference7.child("UsersJoinedSubLeagues").child(Common.userNameID).child(Common.subContestId).child("tId")
                .setValue(teamCode);
        if (Common.totalSpots <= 10){
            databaseReference8.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                    .child(Common.subContestId).child("SingleTeam").child(Common.userNameID).setValue(teamCode);
        }else {
            databaseReference8.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                    .child(Common.subContestId).child("MultipleTeam").child(Common.userNameID).child(Common.teamCode)
                    .removeValue();
            UID = Common.userNameID;
            FID = Common.firebaseId;
            CID = Common.avlContestId;
            SCID = Common.subContestId;
            TID = teamCode;
            creationId = Common.creationId;
            rank = Common.rank;
            obtainedPoints = Common.obPoints;
            String paid = "NO";
            JoinedLeagueModel joinedLeagueModel = new JoinedLeagueModel(
                    UID, FID, CID, SCID, TID,creationId,paid,rank,obtainedPoints);
            databaseReference9.child("AvailableContests").child(Common.avlContestId).child("SubContestsJoinedTeam")
                    .child(Common.subContestId).child("MultipleTeam").child(Common.userNameID).child(TID).setValue(joinedLeagueModel);
            Toast.makeText(getContext(), "Team switched successfully!", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();*/
            Fragment fragment = new SubContestsDetails();
            loadFragment(fragment,"SubContestsDetails");
        }

    }

    private void loadAvailableTeams() {
        binding.availableRecycler.setHasFixedSize(true);
        binding.availableRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference1.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               switchTeamsModelList.clear();
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   PlayerValuesModel playerModel = dataSnapshot.getValue(PlayerValuesModel.class);
                   switchTeamsModelList.add(playerModel);
               }
               AvailableTeamsSwitchAdapter switchAdapter = new AvailableTeamsSwitchAdapter(getContext(),switchTeamsModelList,ChangeTeam.this::sendTeamData);
               binding.availableRecycler.setAdapter(switchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadJoinedTeams() {
        binding.joinedRecycler.setHasFixedSize(true);
        binding.joinedRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("JoinedUsersTeams").child(Common.avlContestId).child(Common.subContestId).child(Common.userNameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerValuesModelList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PlayerValuesModel playerValuesModel = dataSnapshot.getValue(PlayerValuesModel.class);
                        playerValuesModelList.add(playerValuesModel);
                    }
                    AlreadyJoinedTeamsAdapter alreadyJoinedTeamsAdapter = new AlreadyJoinedTeamsAdapter(getContext(),playerValuesModelList);
                    binding.joinedRecycler.setAdapter(alreadyJoinedTeamsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void sendTeamData(PlayerValuesModel playerValuesModel) {
        newTid = playerValuesModel.getTeamCode();
        binding.newId.setText("Team-"+newTid);
        teamCode = playerValuesModel.getTeamCode();
        leftCredit = playerValuesModel.getLeftCredit();
        WK = playerValuesModel.getWK();
        BT = playerValuesModel.getBT();
        BR = playerValuesModel.getBR();
        AR = playerValuesModel.getAR();
        teamOneCount = playerValuesModel.getTeamOneCount();
        teamTwoCount = playerValuesModel.getTeamTwoCount();
        teamOne = playerValuesModel.getTeamOne();
        teamTwo = playerValuesModel.getTeamTwo();
        captain = playerValuesModel.getCaptain();
        viceCaptain = playerValuesModel.getViceCaptain();
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