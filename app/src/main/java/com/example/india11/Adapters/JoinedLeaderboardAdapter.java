


package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.BottomNavigation.TeamScorePreview;
import com.example.india11.Common;
import com.example.india11.ContestsFragments.ChangeTeam;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.PlayersLeaderboardModel;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.RankInfoModel;
import com.example.india11.Model.RunnerModel;
import com.example.india11.Model.TotalBalanceModel;
import com.example.india11.Model.WinningBalanceModel;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.PreviewAdapters.AllRounderPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewAdapter;
import com.example.india11.PreviewAdapters.BatPreviewModel;
import com.example.india11.PreviewAdapters.BowlPreviewModel;
import com.example.india11.PreviewAdapters.BowlerPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewAdapter;
import com.example.india11.PreviewAdapters.WicketPreviewModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedLeaderboardAdapter extends RecyclerView.Adapter<JoinedLeaderboardAdapter.ViewHolder> {
    public Context context;
    private List<JoinedLeagueModel> joinedLeagueModelList;
    FirebaseAuth firebaseAuth;
    int i;
    int j=1;
    String userOne = "", userTwo = "",userThree = "";
    String uid,checkUid,teamNo,c,vc,upStatus,team,matchStatus,startRank,endRank,prizeMoney;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4,databaseReference5,
    databaseReference6,databaseReference7,databaseReference8,databaseReference9,databaseReference10,databaseReference11,databaseReference12;
    double totalPoint, getPoint;
    double total,arPoint,btPoint,brPoint,wkPoint;
    DatabaseReference databaseReference13, databaseReference14, databaseReference15,
            databaseReferenceThree,databaseReference16, databaseReference17, databaseReference18, databaseReference19, databaseReference20;
    String p1Id,p2Id,p3Id,p4Id,p5Id,p6Id,p7Id,p8Id,p9Id,p10Id,p11Id,capId,vCapId;
    double p1Point,p2Point,p3Point,p4Point,p5Point,p6Point,p7Point,p8Point,p9Point,p10Point,p11Point;
    String UID, FID, CID, SCID, TID,creationId, paid, winningAmount,captainCode,viceCaptainCode;
    Integer rank;
    Double obtainedPoints;

    public JoinedLeaderboardAdapter(Context context, List<JoinedLeagueModel> joinedLeagueModelList) {
        this.context = context;
        this.joinedLeagueModelList = joinedLeagueModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
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
        databaseReference12 = FirebaseDatabase.getInstance().getReference();

        databaseReference13 = FirebaseDatabase.getInstance().getReference();
        databaseReference14 = FirebaseDatabase.getInstance().getReference();
        databaseReference15 = FirebaseDatabase.getInstance().getReference();
        databaseReference16 = FirebaseDatabase.getInstance().getReference();
        databaseReference17 = FirebaseDatabase.getInstance().getReference();
        databaseReference18 = FirebaseDatabase.getInstance().getReference();
        databaseReference19 = FirebaseDatabase.getInstance().getReference();
        databaseReference20 = FirebaseDatabase.getInstance().getReference();
        databaseReferenceThree = FirebaseDatabase.getInstance().getReference();

        JoinedLeagueModel joinedLeagueModel = joinedLeagueModelList.get(position);
        databaseReferenceThree.child("SelectedPlayerIds").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                .child(joinedLeagueModel.getTID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    captainCode = snapshot.child("captain").getValue().toString();
                    viceCaptainCode = snapshot.child("viceCaptain").getValue().toString();
                    String team = joinedLeagueModel.getTID();
                    String pcCode = captainCode.substring(0,2);
                    DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
                    if (pcCode.equals("AR")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("AllRounder").child(captainCode).child("cap").setValue("YES");
                    }
                    else if (pcCode.equals("BA")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("Batsman").child(captainCode).child("cap").setValue("YES");
                    }
                    else if (pcCode.equals("BO")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("Bowler").child(captainCode).child("cap").setValue("YES");

                    }
                    else if (pcCode.equals("WK")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("WicketKeeper").child(captainCode).child("cap").setValue("YES");
                    }
                    DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
                    String pvcCode = viceCaptainCode.substring(0,2);
                    if (pvcCode.equals("AR")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("AllRounder").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                    else if (pvcCode.equals("BA")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("Batsman").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                    else if (pvcCode.equals("BO")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("Bowler").child(viceCaptainCode).child("vcap").setValue("YES");

                    }
                    else if (pvcCode.equals("WK")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("WicketKeeper").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference4.child("AvailableContests").child(Common.avlContestId).child("PointStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String status = snapshot.child("status").getValue().toString();
                            if (status.equals("Update")){
                                databaseReference17.child("SelectedPlayerIds").child(joinedLeagueModelList.get(position).getFID()).child(Common.avlContestId)
                                        .child(joinedLeagueModelList.get(position).getTID()).child("updateStatus").setValue("Update");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        DatabaseReference databaseReference21 = FirebaseDatabase.getInstance().getReference();
        databaseReference21.child("AvailableContests").child(Common.avlContestId).child("MatchStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String s = snapshot.child("status").getValue().toString();
                            if (s.equals("Live")){
                                holder.points.setText(String.valueOf(joinedLeagueModel.getObtainedPoints()));
                                holder.rank.setText(String.valueOf(joinedLeagueModel.getRank()));
                            }else if (s.equals("Completed")){
                                holder.points.setText(String.valueOf(joinedLeagueModel.getObtainedPoints()));
                                holder.rank.setText(String.valueOf(joinedLeagueModel.getRank()));
                            }else if (s.equals("Upcoming")){
                                holder.points.setText(String.valueOf(0));
                                holder.rank.setText(String.valueOf(0));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        databaseReference6.child("SelectedPlayerIds").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                .child(joinedLeagueModel.getTID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    capId = snapshot.child("captain").getValue().toString();
                    vCapId = snapshot.child("viceCaptain").getValue().toString();
                    upStatus = snapshot.child("updateStatus").getValue().toString();
                    team = snapshot.child("team").getValue().toString();
                    databaseReference.child("SelectedPlayerIds").child(joinedLeagueModel.getFID()).child(Common.avlContestId)
                                .child(team).child("PlayersList")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            p1Id = snapshot.child("0").getValue().toString();
                                            p2Id = snapshot.child("1").getValue().toString();
                                            p3Id = snapshot.child("2").getValue().toString();
                                            p4Id = snapshot.child("3").getValue().toString();
                                            p5Id = snapshot.child("4").getValue().toString();
                                            p6Id = snapshot.child("5").getValue().toString();
                                            p7Id = snapshot.child("6").getValue().toString();
                                            p8Id = snapshot.child("7").getValue().toString();
                                            p9Id = snapshot.child("8").getValue().toString();
                                            p10Id = snapshot.child("9").getValue().toString();
                                            p11Id = snapshot.child("10").getValue().toString();
                                            databaseReference1.child("ContestSeries").child(Common.seriesId)
                                                    .child("AllPlayers")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()){
                                                                p1Point = Double.parseDouble(snapshot.child(p1Id).child("obtainedPoints").getValue().toString());
                                                                p2Point = Double.parseDouble(snapshot.child(p2Id).child("obtainedPoints").getValue().toString());
                                                                p3Point = Double.parseDouble(snapshot.child(p3Id).child("obtainedPoints").getValue().toString());
                                                                p4Point = Double.parseDouble(snapshot.child(p4Id).child("obtainedPoints").getValue().toString());
                                                                p5Point = Double.parseDouble(snapshot.child(p5Id).child("obtainedPoints").getValue().toString());
                                                                p6Point = Double.parseDouble(snapshot.child(p6Id).child("obtainedPoints").getValue().toString());
                                                                p7Point = Double.parseDouble(snapshot.child(p7Id).child("obtainedPoints").getValue().toString());
                                                                p8Point = Double.parseDouble(snapshot.child(p8Id).child("obtainedPoints").getValue().toString());
                                                                p9Point = Double.parseDouble(snapshot.child(p9Id).child("obtainedPoints").getValue().toString());
                                                                p10Point = Double.parseDouble(snapshot.child(p10Id).child("obtainedPoints").getValue().toString());
                                                                p11Point = Double.parseDouble(snapshot.child(p11Id).child("obtainedPoints").getValue().toString());

                                                                if (capId.equals(p1Id)){
                                                                    p1Point = p1Point*2;
                                                                }else if (vCapId.equals(p1Id)){
                                                                    p1Point = p1Point*1.5;
                                                                }
                                                                if (capId.equals(p2Id)){
                                                                    p2Point = p2Point*2;
                                                                }else if (vCapId.equals(p2Id)){
                                                                    p2Point = p2Point*1.5;
                                                                }
                                                                if (capId.equals(p3Id)){
                                                                    p3Point = p3Point*2;
                                                                }else if (vCapId.equals(p3Id)){
                                                                    p3Point = p3Point*1.5;
                                                                }
                                                                if (capId.equals(p4Id)){
                                                                    p4Point = p4Point*2;
                                                                }else if (vCapId.equals(p4Id)){
                                                                    p4Point = p4Point*1.5;
                                                                }
                                                                if (capId.equals(p5Id)){
                                                                    p5Point = p5Point*2;
                                                                }else if (vCapId.equals(p5Id)){
                                                                    p5Point = p5Point*1.5;
                                                                }
                                                                if (capId.equals(p6Id)){
                                                                    p6Point = p6Point*2;
                                                                }else if (vCapId.equals(p6Id)){
                                                                    p6Point = p6Point*1.5;
                                                                }
                                                                if (capId.equals(p7Id)){
                                                                    p7Point = p7Point*2;
                                                                }else if (vCapId.equals(p7Id)){
                                                                    p7Point = p7Point*1.5;
                                                                }
                                                                if (capId.equals(p8Id)){
                                                                    p8Point = p8Point*2;
                                                                }else if (vCapId.equals(p8Id)){
                                                                    p8Point = p8Point*1.5;
                                                                }
                                                                if (capId.equals(p9Id)){
                                                                    p9Point = p9Point*2;
                                                                }else if (vCapId.equals(p9Id)){
                                                                    p9Point = p9Point*1.5;
                                                                }
                                                                if (capId.equals(p10Id)){
                                                                    p10Point = p10Point*2;
                                                                }else if (vCapId.equals(p10Id)){
                                                                    p10Point = p10Point*1.5;
                                                                }
                                                                if (capId.equals(p11Id)){
                                                                    p11Point = p11Point*2;
                                                                }else if (vCapId.equals(p11Id)){
                                                                    p11Point = p11Point*1.5;
                                                                }
                                                                if (team.equals(joinedLeagueModel.getTID())){
                                                                    double totalGotPoint = p1Point+p2Point+p3Point+p4Point+p5Point+p6Point+p7Point+p8Point+p9Point+p10Point+p11Point;
                                                                    databaseReference3.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId)
                                                                            .child(joinedLeagueModel.getCreationId())
                                                                            .child("obtainedPoints").setValue(totalGotPoint);
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.tid.setText("T"+joinedLeagueModelList.get(position).getTID());
        //holder.rank.setText(String.valueOf(joinedLeagueModelList.get(position).getRank()));

        databaseReference20.child("INDIA11Users").child(joinedLeagueModelList.get(position).getFID())
                .child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image = snapshot.child("profilePic").getValue().toString();
                    String nickName = snapshot.child("nickName").getValue().toString();
                    Glide.with(context).load(image).into(holder.profile);
                    holder.user.setText(nickName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference8.child("JoinedLeagues").child(Common.avlContestId).child(Common.subContestId)
                .child(joinedLeagueModelList.get(position).getCreationId())
                .child("rank").setValue(position+1);
        if (joinedLeagueModelList.get(position).getFID().equals(uid)){
            holder.layout.setBackgroundColor(Color.parseColor("#FFF3EF"));
        }else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        databaseReference10.child("AvailableContests").child(Common.avlContestId).child("MatchStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            matchStatus = snapshot.child("status").getValue().toString();
                            //Toast.makeText(context, Common.totalWinners+matchStatus, Toast.LENGTH_SHORT).show();
                            if (Common.totalSpot <= 3 && matchStatus.equals("Live")){
                                holder.changeTeam.setVisibility(View.GONE);
                                if (joinedLeagueModel.getRank() == 1){
                                    DatabaseReference databaseReference22 = FirebaseDatabase.getInstance().getReference();
                                    databaseReference22.child("AvailableContests").child(Common.avlContestId).child("RunnerUsers").child(Common.subContestId)
                                            .child("userOne").setValue(joinedLeagueModelList.get(position).getUID());
                                }
                            }else if (Common.totalSpot >= 4 && matchStatus.equals("Live")){
                                holder.changeTeam.setVisibility(View.GONE);
                                if (joinedLeagueModel.getRank() == 1){
                                    userOne = joinedLeagueModelList.get(position).getUID();
                                    DatabaseReference databaseReference23 = FirebaseDatabase.getInstance().getReference();
                                    databaseReference23.child("AvailableContests").child(Common.avlContestId).child("RunnerUsers").child(Common.subContestId)
                                            .child("userOne").setValue(userOne);
                                }else if (joinedLeagueModel.getRank() == 2){
                                    userTwo = joinedLeagueModelList.get(position).getUID();
                                    DatabaseReference databaseReference24 = FirebaseDatabase.getInstance().getReference();
                                    databaseReference24.child("AvailableContests").child(Common.avlContestId).child("RunnerUsers").child(Common.subContestId)
                                            .child("userTwo").setValue(userTwo);
                                }else if (joinedLeagueModel.getRank() == 3){
                                    userThree = joinedLeagueModelList.get(position).getUID();
                                    DatabaseReference databaseReference25 = FirebaseDatabase.getInstance().getReference();
                                    databaseReference25.child("AvailableContests").child(Common.avlContestId).child("RunnerUsers").child(Common.subContestId)
                                            .child("userThree").setValue(userThree);
                                }
                            }
                            if (matchStatus.equals("Completed")) {
                                databaseReference17.child("AvailableContests").child(Common.avlContestId)
                                        .child("ContestWinningInfo").child(Common.subContestId)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                long s = snapshot.getChildrenCount();
                                                while (j<=s){
                                                    databaseReference15.child("AvailableContests").child(Common.avlContestId)
                                                            .child("ContestWinningInfo").child(Common.subContestId)
                                                            .child(String.valueOf(j))
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()){
                                                                        startRank = snapshot.child("startRank").getValue().toString();
                                                                        endRank = snapshot.child("lastRank").getValue().toString();
                                                                        prizeMoney = snapshot.child("amount").getValue().toString();
                                                                        int start=Integer.parseInt(startRank);
                                                                        int end = Integer.parseInt(endRank);
                                                                        while (start<=end){
                                                                            holder.changeTeam.setVisibility(View.GONE);
                                                                            holder.trophy.setVisibility(View.VISIBLE);
                                                                            int finalStart = start;
                                                                            databaseReference11.child("JoinedLeagues").child(Common.avlContestId)
                                                                                    .child(Common.subContestId).child(joinedLeagueModelList.get(position).getCreationId())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if (snapshot.exists()){
                                                                                                String stat = snapshot.child("paid").getValue().toString();
                                                                                                int rank = Integer.parseInt(snapshot.child("rank").getValue().toString());
                                                                                                if (stat.equals("NO") && rank == finalStart){
                                                                                                    databaseReference12.child("JoinedLeagues").child(Common.avlContestId)
                                                                                                            .child(Common.subContestId).child(joinedLeagueModelList.get(position).getCreationId())
                                                                                                            .child("paid").setValue("YES");
                                                                                                    UID = joinedLeagueModel.getUID();
                                                                                                    FID = joinedLeagueModel.getFID();
                                                                                                    CID = Common.avlContestId;
                                                                                                    SCID = Common.subContestId;
                                                                                                    TID = joinedLeagueModel.getTID();
                                                                                                    creationId = joinedLeagueModel.getCreationId();
                                                                                                    paid = "Pending";
                                                                                                    winningAmount = "";
                                                                                                    rank = joinedLeagueModel.getRank();
                                                                                                    obtainedPoints = joinedLeagueModel.getObtainedPoints();
                                                                                                    RunnerModel runnerModel = new RunnerModel(UID, FID, CID, SCID, TID,creationId, paid, winningAmount,
                                                                                                            rank,obtainedPoints);
                                                                                                    databaseReference9.child("AvailableContests").child(Common.avlContestId)
                                                                                                            .child("WinnersList").child(Common.subContestId).child(creationId).setValue(runnerModel);
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                            start++;
                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                    j++;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(view -> {
            Common.previewType = "MyLeaderboard";
            Common.firebaseId = joinedLeagueModelList.get(position).getFID();
            Common.avlContestId = joinedLeagueModelList.get(position).getCID();
            Common.teamCode = joinedLeagueModelList.get(position).getTID();
            Common.creationId = joinedLeagueModelList.get(position).getCreationId();
            Common.user = joinedLeagueModelList.get(position).getUID();
            Common.tid = joinedLeagueModelList.get(position).getTID();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            if (matchStatus.equals("Live")||matchStatus.equals("Completed")){
                Fragment preview = new TeamScorePreview();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                        .addToBackStack(null).commit();
            }else if (matchStatus.equals("Upcoming") && joinedLeagueModelList.get(position).getFID().equals(uid)){
                Fragment preview = new TeamPreview();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                        .addToBackStack(null).commit();
            }else {
                Toast.makeText(context, "You can see others team after match starts.", Toast.LENGTH_SHORT).show();
            }
        });
        holder.changeTeam.setOnClickListener(view -> {
            Common.creationId = joinedLeagueModelList.get(position).getCreationId();
            Common.teamCode = joinedLeagueModelList.get(position).getTID();
            Common.firebaseId = joinedLeagueModelList.get(position).getFID();
            Common.obPoints = joinedLeagueModelList.get(position).getObtainedPoints();
            Common.rank = joinedLeagueModelList.get(position).getRank();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment change = new ChangeTeam();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,change)
                    .addToBackStack(null).commit();
        });



    }

    @Override
    public int getItemCount() {
        return joinedLeagueModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, points, rank,tid;
        LinearLayout layout,fieldLayout;
        ImageView changeTeam,trophy;
        RecyclerView playersRecycler;
        CircleImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.username);
            points = itemView.findViewById(R.id.points);
            rank = itemView.findViewById(R.id.rank);
            layout = itemView.findViewById(R.id.layout);
            tid = itemView.findViewById(R.id.tid);
            changeTeam = itemView.findViewById(R.id.change_team);
            fieldLayout = itemView.findViewById(R.id.field_layout);
            playersRecycler = itemView.findViewById(R.id.playersRecycler);
            trophy = itemView.findViewById(R.id.cup);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
