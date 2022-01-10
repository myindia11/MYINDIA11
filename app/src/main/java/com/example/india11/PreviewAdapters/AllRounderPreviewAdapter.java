package com.example.india11.PreviewAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Adapters.AllListAdapter;
import com.example.india11.Adapters.AllRounderAdapter;
import com.example.india11.Adapters.BatListAdapter;
import com.example.india11.Adapters.BowlListAdapter;
import com.example.india11.Adapters.WicketListAdapter;
import com.example.india11.Common;
import com.example.india11.Contests.ContestsDetails;
import com.example.india11.CreateTeam.SelectCandVC;
import com.example.india11.EditTeam.EditAllRounder;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayerInfoModel;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.PlayersReviewAdapters.AllRounderReviewAdapter;
import com.example.india11.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AllRounderPreviewAdapter extends RecyclerView.Adapter<AllRounderPreviewAdapter.ViewHolder> {
    public Context context;
    private List<AllPreviewModel> selectedPlayersModelList;
    String uid,cPlayerRole,vcPlayerRole,team,Captain,ViceCaptain,updateStatus;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference,databaseReference1,databaseReference5,databaseReference4;
    Dialog editTeamDialog,playersDialog;
    ListView listView;
    LinearLayout next, submit,wicketLayout,batLayout,arLayout,bowlLayout;
    TextView playerType, toastMessage, totalPlayers, creditsLeft, teamOneName, teamTwoName;
    TextView teamOneCount, teamTwoCount, timeLeft;
    ImageView teamOneLogo, teamTwoLogo;
    private ArrayList<PlayersListModel> playersListModelList = new ArrayList<>();
    private ArrayList<PlayersListModel> playersListModelListBat = new ArrayList<>();
    private ArrayList<PlayersListModel> playersListModelListWk = new ArrayList<>();
    private ArrayList<PlayersListModel> playersListModelListBowl = new ArrayList<>();
    DatabaseReference databaseReferencePlayers = FirebaseDatabase.getInstance().getReference();
    RecyclerView allRounderR;
    LinearLayout editTeam, confirmTeam;
    TextView cvcText;
    private List<PlayersListModel> allPreviewModelList = new ArrayList<>();


    public AllRounderPreviewAdapter(Context context, List<AllPreviewModel> selectedPlayersModelList) {
        this.context = context;
        this.selectedPlayersModelList = selectedPlayersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_player_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        AllPreviewModel selectedPlayersModel = selectedPlayersModelList.get(position);
        DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference();
        holder.name.setText(selectedPlayersModel.getPlayerName());
        playersDialog = new Dialog(context);
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
        allRounderR.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        editTeam.setOnClickListener(view1 -> {
            playersDialog.dismiss();
        });
        //holder.credit.setText(selectedPlayersModel.getCreditScore()+" Cr");
        switch (Common.previewType){
            case "MyTeam":
                uid = firebaseAuth.getCurrentUser().getUid();
                break;
            case "MyLeaderboard":
                uid = Common.firebaseId;
                break;
            case "Team":
                uid = firebaseAuth.getCurrentUser().getUid();
                break;

        }

        editTeamDialog = new Dialog(context);
        editTeamDialog.setContentView(R.layout.edit_team_layout);
        editTeamDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        editTeamDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editTeamDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        listView = editTeamDialog.findViewById(R.id.playersList);
        next = editTeamDialog.findViewById(R.id.next);
        submit = editTeamDialog.findViewById(R.id.submit);
        playerType = editTeamDialog.findViewById(R.id.head);
        toastMessage = editTeamDialog.findViewById(R.id.toast);
        totalPlayers = editTeamDialog.findViewById(R.id.total);
        creditsLeft = editTeamDialog.findViewById(R.id.credits);
        teamOneName = editTeamDialog.findViewById(R.id.team_one);
        teamTwoName = editTeamDialog.findViewById(R.id.team_two);
        teamOneCount = editTeamDialog.findViewById(R.id.team_one_count);
        teamTwoCount = editTeamDialog.findViewById(R.id.team_two_count);
        timeLeft = editTeamDialog.findViewById(R.id.remainTime);
        teamOneLogo = editTeamDialog.findViewById(R.id.team_one_logo);
        teamTwoLogo = editTeamDialog.findViewById(R.id.team_two_logo);
        wicketLayout = editTeamDialog.findViewById(R.id.wk_layout);
        batLayout = editTeamDialog.findViewById(R.id.bat_layout);
        arLayout = editTeamDialog.findViewById(R.id.ar_layout);
        bowlLayout = editTeamDialog.findViewById(R.id.bowl_layout);
        Glide.with(context).load(Common.teamOneLogo).into(teamOneLogo);
        Glide.with(context).load(Common.teamTwoLogo).into(teamTwoLogo);
        teamOneName.setText(Common.teamOneName);
        teamTwoName.setText(Common.teamTwoName);
        playerType.setText("All Rounder");
        databaseReference1.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers").child(selectedPlayersModelList.get(position).getPid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String status = snapshot.child("playerStatus").getValue().toString();
                            if (status.equals("In lineup")){
                                holder.indicator.setCardBackgroundColor(Color.parseColor("#228B22"));
                            }else if (status.equals("Not in lineup")){
                                holder.indicator.setCardBackgroundColor(Color.parseColor("#D10000"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        Glide.with(context).load(selectedPlayersModel.getPlayerPic()).into(holder.playerImg);
        //Toast.makeText(context, "ContestSeries "+Common.seriesId+" All Players "+selectedPlayersModelList.get(position).getPid(), Toast.LENGTH_SHORT).show();
        holder.credit.setText(String.valueOf(selectedPlayersModelList.get(position).getCreditScore()+"Cr"));
        if (selectedPlayersModel.getCap().equals("YES")){
            holder.cvc.setText("C");
        }else if (selectedPlayersModel.getVcap().equals("YES")){
            holder.cvc.setText("VC");
        }else {
            holder.back.setVisibility(View.GONE);
        }

        if (selectedPlayersModel.getTeamName().equals(Common.teamOneName)){
            holder.nameBack.setBackgroundColor(Color.parseColor("#000000"));
            holder.name.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.nameBack.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.name.setTextColor(Color.parseColor("#000000"));
        }



        holder.change.setOnClickListener(view -> {
            DatabaseReference databaseReference9 = FirebaseDatabase.getInstance().getReference();
            databaseReference9.child("SelectedPlayerIds").child(uid).child(Common.avlContestId)
                    .child(Common.teamCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String c = snapshot.child("captain").getValue().toString();
                        String vc = snapshot.child("viceCaptain").getValue().toString();
                        Common.previousViceCap = vc;
                        Common.previousCap = c;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //editTeamDialog.show();
            arLayout.setBackgroundColor(Color.parseColor("#E97005"));
            batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            wicketLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            databaseReferencePlayers.child("AvailableContests").child(Common.avlContestId).child("Players").child("AllRounder")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            playersListModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                                playersListModelList.add(playersListModel);
                            }
                            AllListAdapter allRounderAdapter = new AllListAdapter(context,playersListModelList);
                            listView.setAdapter(allRounderAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        arLayout.setOnClickListener(view -> {
            arLayout.setBackgroundColor(Color.parseColor("#E97005"));
            batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            wicketLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            databaseReferencePlayers.child("AvailableContests").child(Common.avlContestId).child("Players").child("AllRounder")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            playersListModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                                playersListModelList.add(playersListModel);
                            }
                            AllListAdapter allRounderAdapter = new AllListAdapter(context,playersListModelList);
                            listView.setAdapter(allRounderAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        batLayout.setOnClickListener(view -> {
            arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            batLayout.setBackgroundColor(Color.parseColor("#E97005"));
            bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            wicketLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            databaseReferencePlayers.child("AvailableContests").child(Common.avlContestId).child("Players").child("Batsman")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            playersListModelListBat.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                                playersListModelListBat.add(playersListModel);
                            }
                            BatListAdapter batsmanAdapter = new BatListAdapter(context,playersListModelListBat);
                            listView.setAdapter(batsmanAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        bowlLayout.setOnClickListener(view -> {
            arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bowlLayout.setBackgroundColor(Color.parseColor("#E97005"));
            wicketLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            databaseReferencePlayers.child("AvailableContests").child(Common.avlContestId).child("Players").child("Bowler")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            playersListModelListBowl.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                                playersListModelListBowl.add(playersListModel);
                            }
                            BowlListAdapter bowlerAdapter = new BowlListAdapter(context,playersListModelListBowl);
                            listView.setAdapter(bowlerAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        wicketLayout.setOnClickListener(view -> {
            arLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            batLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bowlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            wicketLayout.setBackgroundColor(Color.parseColor("#E97005"));
            databaseReferencePlayers.child("AvailableContests").child(Common.avlContestId).child("Players").child("WicketKeeper")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            playersListModelListWk.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                PlayersListModel playersListModel = dataSnapshot.getValue(PlayersListModel.class);
                                playersListModelListWk.add(playersListModel);
                            }
                            WicketListAdapter wicketKeeperAdapter = new WicketListAdapter(context,playersListModelListWk);
                            listView.setAdapter(wicketKeeperAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
        next.setOnClickListener(view -> {
            submit.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        });
        submit.setOnClickListener(view -> {
            DatabaseReference databaseReference8 = FirebaseDatabase.getInstance().getReference();
            databaseReference8.child("SelectedPlayersList").child(uid).child(Common.avlContestId)
                    .child(Common.teamCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Common.previousViceCap) && snapshot.hasChild(Common.previousCap)){
                        editTeamDialog.dismiss();
                    }else {
                        editTeamDialog.dismiss();
                        playersDialog.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //editTeamDialog.dismiss();
        });
        DatabaseReference databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference7.child("SelectedPlayersList").child(uid).child(Common.avlContestId).child(Common.teamCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allPreviewModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PlayersListModel allPreviewModel = dataSnapshot.getValue(PlayersListModel.class);
                            allPreviewModelList.add(allPreviewModel);
                        }
                        AllRounderReviewAdapter allRounderPreviewAdapter = new AllRounderReviewAdapter(context,allPreviewModelList);
                        allRounderR.setAdapter(allRounderPreviewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        confirmTeam.setOnClickListener(view -> {
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
                                    databaseReferenceSVC.child("SelectedPlayerIds").child(uid).child(Common.avlContestId).child(team)
                                            .child("PlayersList").setValue(playerIdList);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            Toast.makeText(context, "Team " + Common.teamCode + " edited successfully!", Toast.LENGTH_SHORT).show();
            playersDialog.dismiss();
        });
    }
    @Override
    public int getItemCount() {
        return selectedPlayersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView playerImg;
        LinearLayout back,nameBack,change;
        TextView cvc, name, credit;
        CardView indicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg = itemView.findViewById(R.id.playerImg);
            back = itemView.findViewById(R.id.c_vc_back);
            cvc = itemView.findViewById(R.id.c_vc_tag);
            name = itemView.findViewById(R.id.player_name);
            nameBack = itemView.findViewById(R.id.name_back);
            credit = itemView.findViewById(R.id.score);
            indicator = itemView.findViewById(R.id.indicator);
            change = itemView.findViewById(R.id.change_player);
        }
    }
}
