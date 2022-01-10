package com.example.india11.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.BottomNavigation.TeamScorePreview;
import com.example.india11.Common;
import com.example.india11.ContestsFragments.ChangeTeam;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.Model.JoinedLeagueModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalLeaderboardAdapter extends RecyclerView.Adapter<PersonalLeaderboardAdapter.ViewHolder> {
    public Context context;
    private List<JoinedLeagueModel> joinedLeagueModelList;
    FirebaseAuth firebaseAuth;
    String uid,s;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4,databaseReference5,
            databaseReference6,databaseReference7,databaseReference8,databaseReference9,databaseReference10;

    public PersonalLeaderboardAdapter(Context context, List<JoinedLeagueModel> joinedLeagueModelList) {
        this.context = context;
        this.joinedLeagueModelList = joinedLeagueModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_layout,parent,false);
        return new ViewHolder(view);
    }

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
        JoinedLeagueModel joinedLeagueModel = joinedLeagueModelList.get(position);
        databaseReference.child("AvailableContests").child(Common.avlContestId).child("MatchStatus")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            s = snapshot.child("status").getValue().toString();
                            if (s.equals("Live")){
                                holder.points.setText(String.valueOf(joinedLeagueModel.getObtainedPoints()));
                                holder.rank.setText(String.valueOf(joinedLeagueModel.getRank()));
                                holder.changeTeam.setVisibility(View.GONE);
                            }else if (s.equals("Completed")){
                                holder.points.setText(String.valueOf(joinedLeagueModel.getObtainedPoints()));
                                holder.rank.setText(String.valueOf(joinedLeagueModel.getRank()));
                                holder.changeTeam.setVisibility(View.GONE);
                            }else if (s.equals("Upcoming")){
                                holder.points.setText(String.valueOf(0));
                                holder.rank.setText(String.valueOf(0));
                                holder.changeTeam.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.tid.setText("T"+joinedLeagueModelList.get(position).getTID());
        holder.rank.setText(String.valueOf(joinedLeagueModelList.get(position).getRank()));
        databaseReference1.child("INDIA11Users").child(joinedLeagueModelList.get(position).getFID())
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
        if (joinedLeagueModelList.get(position).getFID().equals(uid)){
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }

        holder.itemView.setOnClickListener(view -> {
            Common.previewType = "MyLeaderboard";
            Common.firebaseId = joinedLeagueModelList.get(position).getFID();
            Common.avlContestId = joinedLeagueModelList.get(position).getCID();
            Common.teamCode = joinedLeagueModelList.get(position).getTID();
            Common.creationId = joinedLeagueModelList.get(position).getCreationId();
            Common.user = joinedLeagueModelList.get(position).getUID();
            Common.tid = joinedLeagueModelList.get(position).getTID();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            if (s.equals("Live")||s.equals("Completed")){
                Fragment preview = new TeamScorePreview();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                        .addToBackStack(null).commit();
            }else if (s.equals("Upcoming") && joinedLeagueModelList.get(position).getFID().equals(uid)){
                Fragment preview = new TeamPreview();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,preview)
                        .addToBackStack(null).commit();
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
            trophy = itemView.findViewById(R.id.cup);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
