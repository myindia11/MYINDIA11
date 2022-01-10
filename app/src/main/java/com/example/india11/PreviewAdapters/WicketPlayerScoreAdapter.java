package com.example.india11.PreviewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WicketPlayerScoreAdapter extends RecyclerView.Adapter<WicketPlayerScoreAdapter.ViewHolder> {
    public Context context;
    private List<WicketPreviewModel> selectedPlayersModelList;
    String cap, vcap,uid;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    double points,gotPoint;

    public WicketPlayerScoreAdapter(Context context, List<WicketPreviewModel> selectedPlayersModelList) {
        this.context = context;
        this.selectedPlayersModelList = selectedPlayersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        WicketPreviewModel selectedPlayersModel = selectedPlayersModelList.get(position);
        uid = Common.firebaseId;
        holder.name.setText(selectedPlayersModel.getPlayerName());
        databaseReference2.child("ContestSeries").child(Common.seriesId)
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
        databaseReference1.child("ContestSeries").child(Common.seriesId)
                .child("AllPlayers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            double point = Double.parseDouble(snapshot.child(selectedPlayersModelList.get(position).getPid()).child("obtainedPoints").getValue().toString());
                            if (selectedPlayersModel.getCap().equals("YES")){
                                point = point*2;
                                databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                        .child(Common.teamCode).child("WicketKeeper")
                                        .child(selectedPlayersModelList.get(position).getPid()).child("obtainedPoints").setValue(point);
                            }else if (selectedPlayersModel.getVcap().equals("YES")){
                                point = point*1.5;
                                databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                        .child(Common.teamCode).child("WicketKeeper")
                                        .child(selectedPlayersModelList.get(position).getPid()).child("obtainedPoints").setValue(point);
                            }else {
                                databaseReference.child("FinalCreatedTeamsPlayer").child(uid).child(Common.avlContestId)
                                        .child(Common.teamCode).child("WicketKeeper")
                                        .child(selectedPlayersModelList.get(position).getPid()).child("obtainedPoints").setValue(point);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        Glide.with(context).load(selectedPlayersModel.getPlayerPic()).into(holder.playerImg);
        holder.credit.setText(String.valueOf(selectedPlayersModelList.get(position).getObtainedPoints()));
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

    }

    @Override
    public int getItemCount() {
        return selectedPlayersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView playerImg;
        LinearLayout back,nameBack;
        TextView cvc, name,credit;
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
        }
    }
}
