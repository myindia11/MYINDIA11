package com.example.india11.CreateTeam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.india11.Activities.CreateTeamActivity;
import com.example.india11.Adapters.AllRounderAdapter;
import com.example.india11.Adapters.BatsmanAdapter;
import com.example.india11.Adapters.BowlerAdapter;
import com.example.india11.Adapters.WicketKeeperAdapter;
import com.example.india11.Common;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.PreviewAdapters.AllRounderPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewModel;
import com.example.india11.PreviewAdapters.BowlPreviewModel;
import com.example.india11.PreviewAdapters.BowlerPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentTeamPreviewBinding;
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

public class TeamPreview extends Fragment {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    private FragmentTeamPreviewBinding binding;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5,databaseReference6;
    String uid,captainCode,viceCaptainCode;
    FirebaseAuth firebaseAuth;
    private List<WicketPreviewModel> wicketPreviewModelList = new ArrayList<>();
    private List<BatPreviewModel> batPreviewModelList = new ArrayList<>();
    private List<BowlPreviewModel> bowlPreviewModelList = new ArrayList<>();
    private List<AllPreviewModel> allPreviewModelList = new ArrayList<>();
    Dialog loading;
    String matchtime;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    String leftCredit,teamId,players,teamOneCount,teamTwoCount,captain,viceCaptain,cPlayerRole,vcPlayerRole;
    int wicket, bat, bowl, ar;
    List<PlayersListModel> playersListModelList1 = new ArrayList<>();
    List<PlayersListModel> playersListModelList2 = new ArrayList<>();
    List<PlayersListModel> playersListModelList3 = new ArrayList<>();
    List<PlayersListModel> playersListModelList4 = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeamPreviewBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        loading.show();
        //loadCreditInfo();
        loadContestTime();
        firebaseAuth = FirebaseAuth.getInstance();
        switch (Common.previewType){
            case "CVC":
            case "Team":
            case "MyTeam":
                uid = firebaseAuth.getCurrentUser().getUid();
                loadWicketKeepers();
                loadBatsman();
                loadAllRounders();
                loadBowlers();
                loadLeftCreditDetails();
                break;
            case "MyLeaderboard":
                uid = Common.firebaseId;
                //uid = firebaseAuth.getCurrentUser().getUid();
                loadWicketKeepers();
                loadBatsman();
                loadAllRounders();
                loadBowlers();
                loadLeftCreditDetails();
                break;
        }
        DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference10.child("SelectedPlayerIds").child(uid).child(Common.avlContestId)
                .child(Common.teamCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String capt = snapshot.child("captain").getValue().toString();
                    String viceCapt = snapshot.child("viceCaptain").getValue().toString();
                    Common.previousCap = capt;
                    Common.previousViceCap = viceCapt;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.edit.setOnClickListener(view1 -> {
            Common.buttonType = "Edit";
            uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference databaseReferenceOne = FirebaseDatabase.getInstance().getReference();
            databaseReferenceOne.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(Common.teamCode)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                captainCode = snapshot.child("captain").getValue().toString();
                                viceCaptainCode = snapshot.child("viceCaptain").getValue().toString();
                                String team = Common.teamCode;
                                String pcCode = captainCode.substring(0,2);
                                DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
                                if (pcCode.equals("AR")){
                                    databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("AllRounder").child(captainCode).child("cap").setValue("NO");
                                }
                                else if (pcCode.equals("BA")){
                                    databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("Batsman").child(captainCode).child("cap").setValue("NO");
                                }
                                else if (pcCode.equals("BO")){
                                    databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("Bowler").child(captainCode).child("cap").setValue("NO");

                                }
                                else if (pcCode.equals("WK")){
                                    databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("WicketKeeper").child(captainCode).child("cap").setValue("NO");
                                }
                                DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
                                String pvcCode = viceCaptainCode.substring(0,2);
                                if (pvcCode.equals("AR")){
                                    databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("AllRounder").child(viceCaptainCode).child("vcap").setValue("NO");
                                }
                                else if (pvcCode.equals("BA")){
                                    databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("Batsman").child(viceCaptainCode).child("vcap").setValue("NO");
                                }
                                else if (pvcCode.equals("BO")){
                                    databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("Bowler").child(viceCaptainCode).child("vcap").setValue("NO");

                                }
                                else if (pvcCode.equals("WK")){
                                    databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                            .child(team).child("WicketKeeper").child(viceCaptainCode).child("vcap").setValue("NO");
                                }
                                Common.previousCap = captainCode;
                                Common.previousViceCap = viceCaptainCode;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        return view;
    }
    private void loadContestTime() {
        databaseReference2.child("AvailableContests").child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchtime = snapshot.child("matchTime").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                players = String.valueOf(wicket + bat + bowl + ar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadLeftCreditDetails() {
        databaseReference2.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(Common.teamCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String credit = snapshot.child("leftCredit").getValue().toString();
                binding.creditLeft.setText(credit+"/100");
                String tOne = snapshot.child("teamOne").getValue().toString();
                String tTwo = snapshot.child("teamTwo").getValue().toString();
                String tOneCount = snapshot.child("teamOneCount").getValue().toString();
                String tTwoCount = snapshot.child("teamTwoCount").getValue().toString();
                binding.contestName.setText(Common.contestsName);
                binding.teamOneName.setText(tOne);
                binding.teamTwoName.setText(tTwo);
                binding.teamOneCount.setText(tOneCount);
                binding.teamTwoCount.setText(tTwoCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBowlers() {
        binding.bowlRecycler.setHasFixedSize(true);
        //binding.bowlRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        binding.bowlRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Bowler")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            bowlPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                BowlPreviewModel bowlPreviewModel = dataSnapshot.getValue(BowlPreviewModel.class);
                                bowlPreviewModelList.add(bowlPreviewModel);
                            }
                            BowlerPreviewAdapter bowlerPreviewAdapter = new BowlerPreviewAdapter(getContext(),bowlPreviewModelList);
                            binding.bowlRecycler.setAdapter(bowlerPreviewAdapter);
                            loading.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllRounders() {
        binding.allRecycler.setHasFixedSize(true);
        //binding.allRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        binding.allRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("AllRounder")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            allPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                AllPreviewModel allPreviewModel = dataSnapshot.getValue(AllPreviewModel.class);
                                allPreviewModelList.add(allPreviewModel);
                            }
                            AllRounderPreviewAdapter allRounderPreviewAdapter = new AllRounderPreviewAdapter(getContext(),allPreviewModelList);
                            binding.allRecycler.setAdapter(allRounderPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadBatsman() {
        binding.batsmanRecycler.setHasFixedSize(true);
        //binding.batsmanRecycler.setLayoutManager(new GridLayoutManager(getContext(),5));
        binding.batsmanRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("Batsman")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            batPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                BatPreviewModel batPreviewModel = dataSnapshot.getValue(BatPreviewModel.class);
                                batPreviewModelList.add(batPreviewModel);
                            }
                            BatPreviewAdapter batPreviewAdapter = new BatPreviewAdapter(getContext(),batPreviewModelList);
                            binding.batsmanRecycler.setAdapter(batPreviewAdapter);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadWicketKeepers() {
        binding.wicketRecycler.setHasFixedSize(true);
        //binding.wicketRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.wicketRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).child("WicketKeeper")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            wicketPreviewModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                WicketPreviewModel wicketPreviewModel = dataSnapshot.getValue(WicketPreviewModel.class);
                                wicketPreviewModelList.add(wicketPreviewModel);
                            }
                            WicketPreviewAdapter wicketPreviewAdapter = new WicketPreviewAdapter(getContext(),wicketPreviewModelList);
                            binding.wicketRecycler.setAdapter(wicketPreviewAdapter);
                            loading.dismiss();
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
                fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(null).detach(this).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
            });
        });
    }

}