package com.example.india11.EditTeam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Adapters.AllListAdapter;
import com.example.india11.Adapters.BatListAdapter;
import com.example.india11.Common;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayerInfoModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentEditBatsmanBinding;
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

public class EditBatsman extends Fragment {
    private FragmentEditBatsmanBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String matchtime,uid;
    int wicket, bat, bowl, ar;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    ArrayList<PlayersListModel> playersListModelList3 = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference1,databaseReference5,databaseReference4;
    String cap, vcap,teamOneCount,teamTwoCount,leftCredit,teamId,captain,viceCaptain,players,cPlayerRole,
            Captain,ViceCaptain,updateStatus,team,vcPlayerRole;
    RecyclerView allRounderR;
    LinearLayout editTeam, confirmTeam;
    TextView cvcText;
    private List<PlayersListModel> allPreviewModelList = new ArrayList<>();
    Dialog playersDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditBatsmanBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        Glide.with(getContext()).load(Common.teamOneLogo).into(binding.teamOneLogo);
        Glide.with(getContext()).load(Common.teamTwoLogo).into(binding.teamTwoLogo);
        binding.teamOne.setText(Common.teamOneName);
        binding.teamTwo.setText(Common.teamTwoName);
        binding.playerType.setText("Edit Batter");
        playersDialog = new Dialog(getContext());
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
        allRounderR.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        editTeam.setOnClickListener(view1 -> {
            playersDialog.dismiss();
        });
        loadContestTime();
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
        databaseReference5.child("AvailableContests").child(Common.avlContestId).child("Players").child("Batsman")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersListModelList3.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                            playersListModelList3.add(playersListModel);
                        }
                        BatListAdapter allRounderAdapter = new BatListAdapter(getContext(),playersListModelList3);
                        binding.playersList.setAdapter(allRounderAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.next.setOnClickListener(view1 -> {
            if (bat < 3){
                binding.toast.setText("Minimum 3 batter required!");
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if ((wicket + bat + ar + bowl) > 11){
                binding.toast.setText("Maximum 11 players allowed!");
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Integer.parseInt(teamOneCount) > 7){
                binding.toast.setText("Maximum 7 players allowed from "+Common.teamOneName);
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Integer.parseInt(teamTwoCount) > 7){
                binding.toast.setText("Maximum 7 players allowed from "+Common.teamTwoName);
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
                binding.toast.startAnimation(animation);
            }else if (Double.parseDouble(leftCredit) < 0.0){
                binding.toast.setText("Running out of credit.");
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.shake);
                binding.toast.startAnimation(animation);
            }else {
                DatabaseReference databaseReferenceId = FirebaseDatabase.getInstance().getReference();
                DatabaseReference databaseReferenceSId = FirebaseDatabase.getInstance().getReference();
                ArrayList<String> playerIdList = new ArrayList<>();
                databaseReferenceId.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String key = dataSnapshot.getKey();
                                    playerIdList.add(key);
                                    databaseReferenceSId.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(Common.teamCode)
                                            .child("PlayersList").setValue(playerIdList);
                                    binding.submit.setVisibility(View.VISIBLE);
                                    binding.next.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        binding.submit.setOnClickListener(view1 -> {
            DatabaseReference databaseReference122 = FirebaseDatabase.getInstance().getReference();
            databaseReference122.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Common.previousCap) && snapshot.hasChild(Common.previousViceCap)){
                                Toast.makeText(getContext(), "Edited successfully.", Toast.LENGTH_SHORT).show();
                                Fragment preview = new TeamPreview();
                                loadFragment(preview,"TeamPreview");
                            }else if (!snapshot.hasChild(Common.previousCap) || !snapshot.hasChild(Common.previousViceCap)
                                    && snapshot.getChildrenCount() == 11) {
                                Toast.makeText(getContext(), "Select Captain and Vice Captain", Toast.LENGTH_SHORT).show();
                                playersDialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        confirmTeam.setOnClickListener(view1 -> {
            DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferenceVC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferenceSC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferencePC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferencePVC = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReferenceSId = FirebaseDatabase.getInstance().getReference();
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
            databaseReferenceSVC.child("AvailableContests").child(Common.avlContestId).child("IsUserCreatedTeam").child(Common.profileMobile).setValue("YES");
            Captain = Common.captainPid;
            ViceCaptain = Common.viceCaptainPid;
            team = Common.teamCode;
            updateStatus = "Update";
            SelectedPlayerInfoModel selectedPlayerInfoModel = new SelectedPlayerInfoModel(Captain,ViceCaptain,team,updateStatus);
            databaseReferencePC.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team).setValue(selectedPlayerInfoModel);
            ArrayList<String> playerIdList = new ArrayList<>();
            databaseReferencePVC.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
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
            Toast.makeText(getContext(), "Team " + Common.teamCode + " edited successfully!", Toast.LENGTH_SHORT).show();
            playersDialog.dismiss();
            Fragment preview = new TeamPreview();
            loadFragment(preview,"TeamPreview");
                        /*startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();*/

        });
        return view;
    }
    private void loadContestTime() {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
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