package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.MatchListModel;
import com.example.india11.R;

import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {
    private List<MatchListModel> modelList;
    private Context context;

    public MatchListAdapter(List<MatchListModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchListModel matchListModel = modelList.get(position);
        holder.teamOne.setText(matchListModel.getTeam1());
        holder.teamTwo.setText(matchListModel.getTeam2());
        holder.matchType.setText(matchListModel.getMatchType());
        holder.matchStatus.setText(matchListModel.getMatchStatus());
        holder.date.setText(matchListModel.getDate());
        holder.vs.setText(matchListModel.getTeam1()+" VS "+matchListModel.getTeam2());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamOne, teamTwo, matchType, matchStatus, date, vs;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamOne = itemView.findViewById(R.id.team_one_name);
            teamTwo = itemView.findViewById(R.id.team_two_name);
            matchType = itemView.findViewById(R.id.league_type);
            matchStatus = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.event_timer);
            vs = itemView.findViewById(R.id.contest_name);

        }
    }
}
