package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Common;
import com.example.india11.ContestsFragments.SubContestsDetails;
import com.example.india11.Model.JoinedSubContestsModel;
import com.example.india11.Model.SubContestsModel;
import com.example.india11.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class JoinedSubContestsAdapter extends RecyclerView.Adapter<JoinedSubContestsAdapter.ViewHolder> {
    public Context context;
    private List<JoinedSubContestsModel> subContestsModelList;

    public JoinedSubContestsAdapter(Context context, List<JoinedSubContestsModel> subContestsModelList) {
        this.context = context;
        this.subContestsModelList = subContestsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_subcontests_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JoinedSubContestsModel joinedSubContestsModel = subContestsModelList.get(position);
        holder.prizePool.setText(convertValueInIndianCurrency(joinedSubContestsModel.getPrizePool()));
        holder.entryFee.setText(convertValueInIndianCurrency(joinedSubContestsModel.getEntryFee()));
        holder.teamId.setText(joinedSubContestsModel.gettId());
        holder.leagueType.setText(joinedSubContestsModel.getLeagueType());
        holder.itemView.setOnClickListener(view -> {
            Common.subContestId = subContestsModelList.get(position).getScid();
            Common.avlContestId = subContestsModelList.get(position).getcId();
            Common.totalSpot = Integer.parseInt(subContestsModelList.get(position).getTotalSpots());
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment subContestsDetails = new SubContestsDetails();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,subContestsDetails)
                    .addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return subContestsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prizePool, entryFee, teamId, leagueType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prizePool = itemView.findViewById(R.id.prize_pool);
            entryFee = itemView.findViewById(R.id.entry_fee);
            teamId = itemView.findViewById(R.id.tid);
            leagueType = itemView.findViewById(R.id.league_type);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
