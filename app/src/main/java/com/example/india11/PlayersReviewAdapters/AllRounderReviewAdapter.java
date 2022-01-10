package com.example.india11.PlayersReviewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Model.PlayerValuesModel;
import com.example.india11.Model.PlayersListModel;
import com.example.india11.Model.SelectedPlayersModel;
import com.example.india11.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllRounderReviewAdapter extends RecyclerView.Adapter<AllRounderReviewAdapter.ViewHolder> {
    public Context context;
    private List<PlayersListModel> playersListModelList;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5,databaseReference6;
    String uid;
    FirebaseAuth firebaseAuth;
    String playerName,scap,svcap;
    public AllRounderReviewAdapter(Context context, List<PlayersListModel> playersListModelList) {
        this.context = context;
        this.playersListModelList = playersListModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_players_review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayersListModel playersListModel = playersListModelList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        holder.team.setText(playersListModel.getTeamName());
        holder.player.setText(playersListModel.getPlayerName());
        //holder.identification.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.wicket_keeper_icon));
        Glide.with(context).load(playersListModel.getPlayerPic()).into(holder.identification);
        holder.status.setText(playersListModel.getPlayerStatus());
        databaseReference2.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    scap = snapshot.child("captain").getValue().toString();
                    svcap = snapshot.child("viceCaptain").getValue().toString();
                    if (playersListModel.getPlayerName().equals(scap)) {
                        holder.cText.setText("2X");
                        holder.cBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
                    } else {
                        holder.cText.setText("C");
                        holder.cBtn.setCardBackgroundColor(Color.parseColor("#E3E3E3"));
                    }
                    if (playersListModel.getPlayerName().equals(svcap)) {
                        holder.vcText.setText("1.5X");
                        holder.vcBtn.setCardBackgroundColor(Color.parseColor("#e97005"));
                    } else {
                        holder.vcText.setText("VC");
                        holder.vcBtn.setCardBackgroundColor(Color.parseColor("#E3E3E3"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cBtn.setOnClickListener(view -> {
            playerName = playersListModelList.get(position).getPlayerName();
            databaseReference5.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode).child("captain")
                    .setValue(playerName);
            databaseReference2.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(Common.teamCode).child("captain")
                    .setValue(playerName);
            Common.captainPid = playersListModelList.get(position).getPid();
        });
        holder.vcBtn.setOnClickListener(view -> {
            playerName = playersListModelList.get(position).getPlayerName();
            databaseReference5.child("INDIA11Users").child(uid).child("CreatedTeams").child(Common.teamCode).child("viceCaptain")
                    .setValue(playerName);
            databaseReference2.child("FinalCreatedTeams").child(uid).child(Common.avlContestId).child(Common.teamCode).child("viceCaptain")
                    .setValue(playerName);
            Common.viceCaptainPid = playersListModelList.get(position).getPid();
        });


    }

    @Override
    public int getItemCount() {
        return playersListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView team, player,status,cText,vcText;
        CircleImageView identification;
        MaterialCardView layout;
        CardView cBtn,vcBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.team);
            player = itemView.findViewById(R.id.player_name);
            identification = itemView.findViewById(R.id.identification_img);
            status = itemView.findViewById(R.id.player_status);
            layout = itemView.findViewById(R.id.layout);
            cBtn = itemView.findViewById(R.id.c_btn);
            vcBtn = itemView.findViewById(R.id.vc_btn);
            cText = itemView.findViewById(R.id.c_tag);
            vcText = itemView.findViewById(R.id.vc_tag);
        }
    }
}
