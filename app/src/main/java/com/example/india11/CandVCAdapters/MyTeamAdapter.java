package com.example.india11.CandVCAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.service.controls.actions.CommandAction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Activities.TeamPreviewActivity;
import com.example.india11.Common;
import com.example.india11.ContestsFragments.SubContestsDetails;
import com.example.india11.CreateTeam.CreatePlayerTeam;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTeamAdapter extends RecyclerView.Adapter<MyTeamAdapter.ViewHolder> {
    public Context context;
    private List<PlayerValuesModel> playerValuesModelList;
    String tCode,uid,captainCode,viceCaptainCode;
    DatabaseReference databaseReferenceOne, databaseReferenceTwo,databaseReferenceThree;
    FirebaseAuth firebaseAuth;


    public MyTeamAdapter(Context context, List<PlayerValuesModel> playerValuesModelList) {
        this.context = context;
        this.playerValuesModelList = playerValuesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerValuesModel playerValuesModel = playerValuesModelList.get(position);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceOne = FirebaseDatabase.getInstance().getReference();
        databaseReferenceTwo = FirebaseDatabase.getInstance().getReference();
        databaseReferenceThree = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceTwo.child("AvailableContests").child(Common.avlContestId).child("MatchStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("status").getValue().toString();
                    if (status.equals("Upcoming")){
                        holder.edit.setVisibility(View.VISIBLE);
                    }else if (status.equals("Live")){
                        holder.edit.setVisibility(View.GONE);
                    }else if (status.equals("Completed")){
                        holder.edit.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceThree.child("SelectedPlayerIds").child(uid).child(Common.avlContestId)
                .child(playerValuesModelList.get(position).getTeamCode()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    captainCode = snapshot.child("captain").getValue().toString();
                    viceCaptainCode = snapshot.child("viceCaptain").getValue().toString();
                    String team = playerValuesModelList.get(position).getTeamCode();
                    String pcCode = captainCode.substring(0,2);
                    DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference();
                    if (pcCode.equals("AR")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("AllRounder").child(captainCode).child("cap").setValue("YES");
                    }
                    else if (pcCode.equals("BA")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("Batsman").child(captainCode).child("cap").setValue("YES");
                    }
                    else if (pcCode.equals("BO")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("Bowler").child(captainCode).child("cap").setValue("YES");

                    }
                    else if (pcCode.equals("WK")){
                        databaseReferenceC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("WicketKeeper").child(captainCode).child("cap").setValue("YES");
                    }
                    DatabaseReference databaseReferenceSVC = FirebaseDatabase.getInstance().getReference();
                    String pvcCode = viceCaptainCode.substring(0,2);
                    if (pvcCode.equals("AR")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("AllRounder").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                    else if (pvcCode.equals("BA")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("Batsman").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                    else if (pvcCode.equals("BO")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("Bowler").child(viceCaptainCode).child("vcap").setValue("YES");

                    }
                    else if (pvcCode.equals("WK")){
                        databaseReferenceSVC.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                .child(team).child("WicketKeeper").child(viceCaptainCode).child("vcap").setValue("YES");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.codeLay.setVisibility(View.GONE);
        holder.teamId.setText("Team-"+playerValuesModel.getTeamCode());
        holder.capName.setText(playerValuesModel.getCaptain());
        holder.vcapName.setText(playerValuesModel.getViceCaptain());
        holder.wicket.setText("WK "+playerValuesModel.getWK());
        holder.bat.setText("BT "+playerValuesModel.getBT());
        holder.bowl.setText("BL "+playerValuesModel.getBR());
        holder.allRounder.setText("AR "+playerValuesModel.getAR());
        int all = playerValuesModel.getAR()+playerValuesModel.getWK()+playerValuesModel.getBR()+playerValuesModel.getBT();
        if (all < 11){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        holder.frameLayout.setOnClickListener(view -> {
            tCode = playerValuesModelList.get(position).getTeamCode();
            Common.teamCode = tCode;
            Common.teamOneCount = String.valueOf(playerValuesModelList.get(position).getTeamOneCount());
            Common.teamTwoCount = String.valueOf(playerValuesModelList.get(position).getTeamTwoCount());
            Common.teamOneName = playerValuesModelList.get(position).getTeamOne();
            Common.teamTwoName = playerValuesModelList.get(position).getTeamTwo();
            Common.previewType = "MyTeam";
            Common.oldCap = playerValuesModelList.get(position).getCaptain();
            Common.oldVcap = playerValuesModelList.get(position).getViceCaptain();
            //Toast.makeText(context, Common.previousCap+" + "+Common.previousViceCap, Toast.LENGTH_SHORT).show();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment preview = new TeamPreview();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                    .addToBackStack(null).commit();
        });
        holder.preview.setOnClickListener(view -> {
            tCode = playerValuesModelList.get(position).getTeamCode();
            Common.teamCode = tCode;
            Common.oldCap = playerValuesModelList.get(position).getCaptain();
            Common.oldVcap = playerValuesModelList.get(position).getViceCaptain();
            Common.teamOneCount = String.valueOf(playerValuesModelList.get(position).getTeamOneCount());
            Common.teamTwoCount = String.valueOf(playerValuesModelList.get(position).getTeamTwoCount());
            Common.teamOneName = playerValuesModelList.get(position).getTeamOne();
            Common.teamTwoName = playerValuesModelList.get(position).getTeamTwo();
            Common.previewType = "MyTeam";
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment preview = new TeamPreview();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                    .addToBackStack(null).commit();
        });
        holder.edit.setOnClickListener(view -> {
            tCode = playerValuesModelList.get(position).getTeamCode();
            Common.teamCode = tCode;
            Common.buttonType = "Edit";

        });

    }
    @Override
    public int getItemCount() {
        return playerValuesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamId, capName,vcapName, wicket, bat, bowl, allRounder,preview,edit,cCode,vcCode;
        LinearLayout codeLay;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamId = itemView.findViewById(R.id.team_id);
            capName = itemView.findViewById(R.id.cap_name);
            vcapName = itemView.findViewById(R.id.vcap_name);
            wicket = itemView.findViewById(R.id.wicket_count);
            bat = itemView.findViewById(R.id.bat_count);
            bowl = itemView.findViewById(R.id.bowl_count);
            allRounder = itemView.findViewById(R.id.ar_count);
            preview = itemView.findViewById(R.id.preview);
            edit = itemView.findViewById(R.id.edit);
            codeLay = itemView.findViewById(R.id.code_lay);
            cCode = itemView.findViewById(R.id.ccode);
            vcCode = itemView.findViewById(R.id.vccode);
            frameLayout = itemView.findViewById(R.id.frame);

        }
    }
}
