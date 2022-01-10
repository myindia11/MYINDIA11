package com.example.india11.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.CreateTeam.AllRounder;
import com.example.india11.CreateTeam.Batsman;
import com.example.india11.CreateTeam.Bowler;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.CreateTeam.WicketKeeper;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayerInfoModel;
import com.example.india11.PlayersReviewAdapters.AllRounderReviewAdapter;
import com.example.india11.R;
import com.example.india11.databinding.ActivityCreateTeamBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class CreateTeamActivity extends AppCompatActivity {
    private ActivityCreateTeamBinding binding;
    final FragmentManager fm = getSupportFragmentManager();
    Dialog wallet,delete,playersDialog;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReferenceInfo,databaseReferenceId,databaseReferenceSId;
    FirebaseAuth firebaseAuth;
    String uid,tBalance,bBalance,wBalance;
    TextView totalBalance, bonusBalance, winningBalance;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String leftCredit,teamId,players,teamOneCount,teamTwoCount,captain,viceCaptain,cPlayerRole,vcPlayerRole;
    int wicket, bat, bowl, ar;
    String matchtime;
    long diff;
    long milliseconds;
    String Captain,ViceCaptain,team,updateStatus;
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    LinearLayout deleteButton, continueButton;
    DatabaseReference databaseReference5,databaseReference6,databaseReference7,databaseReference8;
    RecyclerView allRounderR;
    LinearLayout editTeam, confirmTeam;
    private List<PlayersListModel> allPreviewModelList = new ArrayList<>();
    TextView cvcText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTeamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Fragment wicketK = new WicketKeeper();
        loadFragment(wicketK,"WicketKeeper");
        binding.wkLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.tab_back_shape));
        binding.batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReferenceId = FirebaseDatabase.getInstance().getReference();
        databaseReferenceInfo = FirebaseDatabase.getInstance().getReference();
        databaseReferenceSId = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        delete = new Dialog(this);
        delete.setContentView(R.layout.team_discard_layout);
        delete.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        delete.getWindow().setGravity(Gravity.BOTTOM);
        deleteButton = delete.findViewById(R.id.delete);
        continueButton = delete.findViewById(R.id.continueTeam);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyCode == KeyEvent.KEYCODE_BACK){
                        delete.show();
                        return true;
                    }
                }
                return false;
            }
        });
        binding.toolbarabout.setNavigationOnClickListener(view1 -> delete.show());
        deleteButton.setOnClickListener(view1 -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode).removeValue();
            databaseReference8.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode).removeValue();
            delete.dismiss();
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);

        });
        continueButton.setOnClickListener(view1 -> {
            delete.dismiss();
        });

        binding.wkLayout.setOnClickListener(view1 -> {
            Fragment wicket = new WicketKeeper();
            loadFragment(wicket,"WicketKeeper");
            binding.wkLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.tab_back_shape));
            binding.batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.batLayout.setOnClickListener(view1 -> {
            Fragment batsman = new Batsman();
            loadFragment(batsman,"Batsman");
            binding.batLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.tab_back_shape));
            binding.wkLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.arLayout.setOnClickListener(view1 -> {
            Fragment allrounder = new AllRounder();
            loadFragment(allrounder,"AllRounder");
            binding.arLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.tab_back_shape));
            binding.batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.wkLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.bowlLayout.setOnClickListener(view1 -> {
            Fragment bowler = new Bowler();
            loadFragment(bowler,"Bowler");
            binding.bowlLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.tab_back_shape));
            binding.batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.wkLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();


        wallet = new Dialog(this);
        wallet.setContentView(R.layout.top_dialog_for_wallet);
        wallet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        wallet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wallet.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        wallet.getWindow().setGravity(Gravity.TOP);

        playersDialog = new Dialog(this);
        playersDialog.setContentView(R.layout.all_players_review_layout);
        playersDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        playersDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        playersDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        playersDialog.getWindow().setGravity(Gravity.CENTER);
        playersDialog.setCanceledOnTouchOutside(false);
        playersDialog.setCancelable(false);
        allRounderR = playersDialog.findViewById(R.id.allrounderR);
        editTeam = playersDialog.findViewById(R.id.edit);
        confirmTeam = playersDialog.findViewById(R.id.proceed);
        cvcText = playersDialog.findViewById(R.id.cvc_text);

        allRounderR.setHasFixedSize(true);
        allRounderR.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        editTeam.setOnClickListener(view1 -> {
            playersDialog.dismiss();
        });

        confirmTeam.setOnClickListener(view1 -> {
            switch (Common.buttonType){
                case "Create":
                    if (captain.equals("") || viceCaptain.equals("")){
                        cvcText.setText("Please select captain/vice captain.");
                        Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                        binding.toast.startAnimation(animation);
                    }else {
                        DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceVC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceSC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
                        String cCode = Common.captainPid.substring(0,2);
                        if (cCode.equals("AR")){
                            cPlayerRole = "AllRounder";
                        }else if (cCode.equals("BA")){
                            cPlayerRole = "Batsman";
                        }else if (cCode.equals("BO")){
                            cPlayerRole = "Bowler";
                        }else if (cCode.equals("WK")){
                            cPlayerRole = "WicketKeeper";
                        }
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(cPlayerRole).child(Common.captainPid).child("cap").setValue("YES");
                        databaseReferenceSC.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(Common.captainPid).child("cap").setValue("YES");

                        String vcCode = Common.viceCaptainPid.substring(0,2);
                        if (vcCode.equals("AR")){
                            vcPlayerRole = "AllRounder";
                        }else if (vcCode.equals("BA")){
                            vcPlayerRole = "Batsman";
                        }else if (vcCode.equals("BO")){
                            vcPlayerRole = "Bowler";
                        }else if (vcCode.equals("WK")){
                            vcPlayerRole = "WicketKeeper";
                        }
                        databaseReferenceVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(vcPlayerRole).child(Common.viceCaptainPid).child("vcap").setValue("YES");
                        databaseReferenceSC.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(Common.viceCaptainPid).child("vcap").setValue("YES");

                        int teamName = Integer.parseInt(Common.teamCode);
                        databaseReferenceT.child("INDIA11Users").child(uid).child("teamName").child(Common.avlContestId).child("teamName").setValue(teamName);
                        databaseReference6.child("AvailableContests").child(Common.avlContestId).child("IsUserCreatedTeam").child(Common.profileMobile).setValue("YES");
                        Captain = Common.captainPid;
                        ViceCaptain = Common.viceCaptainPid;
                        team = Common.teamCode;
                        updateStatus = "Update";
                        SelectedPlayerInfoModel selectedPlayerInfoModel = new SelectedPlayerInfoModel(Captain,ViceCaptain,team,updateStatus);
                        databaseReferenceInfo.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team).setValue(selectedPlayerInfoModel);
                        ArrayList<String> playerIdList = new ArrayList<>();
                        databaseReferenceId.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            String key = dataSnapshot.getKey();
                                            playerIdList.add(key);
                                            databaseReferenceSId.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team)
                                                    .child("PlayersList").setValue(playerIdList);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        Toast.makeText(this, "Team " + Common.teamCode + "created successfully!", Toast.LENGTH_SHORT).show();
                        playersDialog.dismiss();
                        /*startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();*/
                        Intent intent = new Intent(this,HomeActivity.class);
                        startActivity(intent);
                    }
                    break;
                case "Edit":
                    if (captain.equals("") || viceCaptain.equals("")){
                        cvcText.setText("Please select captain/vice captain.");
                        Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                        binding.toast.startAnimation(animation);
                    }else {
                        DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceVC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceSC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferencePC = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference databaseReferencePVC = FirebaseDatabase.getInstance().getReference();
                        String cCode = Common.captainPid.substring(0,2);
                        if (cCode.equals("AR")){
                            cPlayerRole = "AllRounder";
                        }else if (cCode.equals("BA")){
                            cPlayerRole = "Batsman";
                        }else if (cCode.equals("BO")){
                            cPlayerRole = "Bowler";
                        }else if (cCode.equals("WK")){
                            cPlayerRole = "WicketKeeper";
                        }
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(cPlayerRole).child(Common.captainPid).child("cap").setValue("YES");
                        databaseReferenceSC.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(Common.captainPid).child("cap").setValue("YES");

                        String vcCode = Common.viceCaptainPid.substring(0,2);
                        if (vcCode.equals("AR")){
                            vcPlayerRole = "AllRounder";
                        }else if (vcCode.equals("BA")){
                            vcPlayerRole = "Batsman";
                        }else if (vcCode.equals("BO")){
                            vcPlayerRole = "Bowler";
                        }else if (vcCode.equals("WK")){
                            vcPlayerRole = "WicketKeeper";
                        }
                        databaseReferenceVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(vcPlayerRole).child(Common.viceCaptainPid).child("vcap").setValue("YES");
                        databaseReferenceSC.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .child(Common.viceCaptainPid).child("vcap").setValue("YES");

                        int teamName = Integer.parseInt(Common.teamCode);
                        databaseReferenceT.child("INDIA11Users").child(uid).child("teamName").child(Common.avlContestId).child("teamName").setValue(teamName);
                        databaseReference6.child("AvailableContests").child(Common.avlContestId).child("IsUserCreatedTeam").child(Common.profileMobile).setValue("YES");
                        Captain = Common.captainPid;
                        ViceCaptain = Common.viceCaptainPid;
                        team = Common.teamCode;
                        updateStatus = "Update";
                        SelectedPlayerInfoModel selectedPlayerInfoModel = new SelectedPlayerInfoModel(Captain,ViceCaptain,team,updateStatus);
                        databaseReferenceInfo.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team).setValue(selectedPlayerInfoModel);
                        ArrayList<String> playerIdList = new ArrayList<>();
                        databaseReferenceId.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                String key = dataSnapshot.getKey();
                                                playerIdList.add(key);
                                                databaseReferenceSId.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team)
                                                        .child("PlayersList").setValue(playerIdList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        Toast.makeText(this, "Team " + Common.teamCode + " edited successfully!", Toast.LENGTH_SHORT).show();
                        playersDialog.dismiss();
                        Intent intent = new Intent(this,HomeActivity.class);
                        startActivity(intent);
                    }

                    break;
            }

        });

        databaseReference7.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allPreviewModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel allPreviewModel = dataSnapshot.getValue(PlayersListModel.class);
                            allPreviewModelList.add(allPreviewModel);
                        }
                        AllRounderReviewAdapter allRounderPreviewAdapter = new AllRounderReviewAdapter(getApplicationContext(),allPreviewModelList);
                        allRounderR.setAdapter(allRounderPreviewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.wallet.setOnClickListener(view1 -> {
            wallet.show();
        });
        binding.matchName.setText(Common.matchName);
        binding.teamOne.setText(Common.teamOneName);
        binding.teamTwo.setText(Common.teamTwoName);
        totalBalance = wallet.findViewById(R.id.total);
        bonusBalance = wallet.findViewById(R.id.bonus);
        winningBalance = wallet.findViewById(R.id.winning);
        Glide.with(getApplicationContext()).load(Common.teamOneLogo).into(binding.teamOneLogo);
        Glide.with(getApplicationContext()).load(Common.teamTwoLogo).into(binding.teamTwoLogo);
        showBalance();
        binding.teamPreview.setOnClickListener(view1 -> {
            if ((wicket + bat + ar + bowl) > 11){
                binding.toast.setText("Maximum 11 players allowed!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else {
                Intent intent = new Intent(this,TeamPreviewActivity.class);
                Common.previewType = "Team";
                startActivity(intent);
            }
        });
        loadCreditInfo();
        binding.next.setOnClickListener(view1 -> {
            if (wicket < 1){
                binding.toast.setText("Minimum 1 wicket keeper required!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (bat < 3){
                binding.toast.setText("Minimum 3 batsmen required!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (ar < 1){
                binding.toast.setText("Minimum 1 all rounder required!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (bowl < 3){
                binding.toast.setText("Minimum 3 bowler required!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if ((wicket + bat + ar + bowl) > 11){
                binding.toast.setText("Maximum 11 players allowed!");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Integer.parseInt(teamOneCount) > 7){
                binding.toast.setText("Maximum 7 players allowed from "+Common.teamOneName);
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Integer.parseInt(teamTwoCount) > 7){
                binding.toast.setText("Maximum 7 players allowed from "+Common.teamTwoName);
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Double.parseDouble(leftCredit) < 0.0){
                binding.toast.setText("Running out of credit.");
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
                binding.toast.startAnimation(animation);
            }else {
                playersDialog.show();
            }
        });
        loadContestTime();
        setContentView(view);
    }
    @SuppressLint("StaticFieldLeak")
    private void loadFragment(Fragment fragment, String tag)
    {
        executorService.execute(() ->{
            if (fragment!=null)
            {
                fm.beginTransaction().replace(R.id.players_container, fragment,tag).addToBackStack(tag).commitAllowingStateLoss();
            }
            handler.post(() ->{
                activeFragment=fm.findFragmentById(R.id.players_container);
            });
        });
    }
    private void loadContestTime() {
        databaseReference2.child("AvailableContests").child(Common.avlContestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchtime = snapshot.child("matchTime").getValue().toString();
                runTimer(matchtime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void runTimer(String matchtime) {
        Date endDate;
        try {
            endDate = simpleDateFormat.parse(matchtime);
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final long[] startTime = {System.currentTimeMillis()};
        diff = milliseconds - startTime[0];
        mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime[0] = startTime[0] -1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime[0]) / 1000;
                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //binding.remainTime.setText(daysLeft+"D "+hoursLeft+"H:"+minutesLeft+"M:"+secondsLeft+"S");
                if (!daysLeft.equals("0")){
                    binding.remainTime.setText(daysLeft+" Day");
                }else if (daysLeft.equals("0") && !hoursLeft.equals("0")){
                    binding.remainTime.setText(hoursLeft+" Hours");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && !minutesLeft.equals("0")){
                    binding.remainTime.setText(minutesLeft+" Minutes");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && minutesLeft.equals("0") && !secondsLeft.equals("0")){
                    binding.remainTime.setText(secondsLeft+" Seconds");
                }else {
                    binding.remainTime.setText("Live");
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
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
                binding.teamOneCount.setText(teamOneCount);
                binding.teamTwoCount.setText(teamTwoCount);
                binding.credits.setText(leftCredit+"/100");
                players = String.valueOf(wicket + bat + bowl + ar);

                binding.total.setText(players+"/11");
                binding.wkText.setText("WK-"+wicket);
                binding.batText.setText("BT-"+bat);
                binding.arText.setText("AR-"+ar);
                binding.bowlText.setText("BL-"+bowl);
                switch (players) {
                    case "0":
                        binding.c1.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "1":
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "2":
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "3":
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "4":
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "5":
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "6":
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "7":
                        binding.c7.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "8":
                        binding.c8.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "9":
                        binding.c9.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "10":
                        binding.c10.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c11.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "11":
                        binding.c11.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c2.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c3.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c4.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c5.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c6.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c7.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c8.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c9.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c10.setCardBackgroundColor(Color.parseColor("#e97005"));
                        binding.c1.setCardBackgroundColor(Color.parseColor("#e97005"));
                        break;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showBalance() {
        databaseReference.child("INDIA11Users").child(uid).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("totalBalance").getValue().toString();
                totalBalance.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference2.child("INDIA11Users").child(uid).child("BonusBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bBalance = snapshot.child("bonusBalance").getValue().toString();
                bonusBalance.setText(convertValueInIndianCurrency(Long.valueOf(bBalance)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference3.child("INDIA11Users").child(uid).child("WinningBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wBalance = snapshot.child("winningBalance").getValue().toString();
                winningBalance.setText(convertValueInIndianCurrency(Long.valueOf(wBalance)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String convertValueInIndianCurrency(Long amount)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}