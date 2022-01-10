package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Model.TossJoinModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TossLeaderboardAdapter extends RecyclerView.Adapter<TossLeaderboardAdapter.ViewHolder> {
    public Context context;
    private List<TossJoinModel> tossJoinModelList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public TossLeaderboardAdapter(Context context, List<TossJoinModel> tossJoinModelList) {
        this.context = context;
        this.tossJoinModelList = tossJoinModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_toss_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TossJoinModel tossJoinModel = tossJoinModelList.get(position);
        holder.selectedTeam.setText(tossJoinModel.getSelectedTeam());
        databaseReference.child("INDIA11Users").child(tossJoinModelList.get(position).getUserFid())
                .child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("userName").getValue().toString();
                    String image = snapshot.child("profilePic").getValue().toString();
                    holder.user.setText(name);
                    Glide.with(context).load(image).into(holder.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.winningTeam.setText(tossJoinModel.getActualTeam());
        if (tossJoinModelList.get(position).getSelectedTeam().equals(tossJoinModelList.get(position).getActualTeam())){
            holder.coin.setVisibility(View.VISIBLE);
        }else {
            holder.coin.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tossJoinModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, selectedTeam, winningTeam;
        ImageView  coin;
        CircleImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.username);
            selectedTeam = itemView.findViewById(R.id.selected);
            winningTeam = itemView.findViewById(R.id.winning);
            profile = itemView.findViewById(R.id.profile);
            coin = itemView.findViewById(R.id.coin);
        }
    }
}
