package com.example.india11.Adapters;

import android.content.Context;
import android.service.controls.actions.CommandAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Common;
import com.example.india11.Model.PlayersLeaderboardModel;
import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PlayersLeaderboardAdapter extends RecyclerView.Adapter<PlayersLeaderboardAdapter.ViewHolder> {
    public Context context;
    private List<PlayersLeaderboardModel> selectedPlayersModelList;
    Double score,totalScore;
    String cap , vCap,uid;
    DatabaseReference databaseReference, databaseReference1, databaseReference2;
    FirebaseAuth firebaseAuth;

    public PlayersLeaderboardAdapter(Context context, List<PlayersLeaderboardModel> selectedPlayersModelList) {
        this.context = context;
        this.selectedPlayersModelList = selectedPlayersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_leaderboard_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayersLeaderboardModel playersLeaderboardModel = selectedPlayersModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return selectedPlayersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView identification;
        TextView playerName, teamName, score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            identification = itemView.findViewById(R.id.img);
            playerName = itemView.findViewById(R.id.player);
            teamName = itemView.findViewById(R.id.team);
            score = itemView.findViewById(R.id.points);
        }
    }
}
