package com.example.india11.Adapters;

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
import com.example.india11.Model.JoinedTossModel;
import com.example.india11.R;
import com.example.india11.TossPrediction.PredictToss;
import com.example.india11.TossPrediction.TossLeaderBoard;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class JoinedTossAdapter extends RecyclerView.Adapter<JoinedTossAdapter.ViewHolder> {
    public Context context;
    private List<JoinedTossModel> joinedTossModelList;

    public JoinedTossAdapter(Context context, List<JoinedTossModel> joinedTossModelList) {
        this.context = context;
        this.joinedTossModelList = joinedTossModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_toss_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JoinedTossModel joinedTossModel = joinedTossModelList.get(position);
        holder.prize.setText(convertValueInIndianCurrency(joinedTossModel.getPrize()));
        holder.entry.setText(convertValueInIndianCurrency(joinedTossModel.getEntry()));
        holder.team.setText(joinedTossModel.getSelectedTeam());
        holder.itemView.setOnClickListener(view -> {
            Common.tossCId = joinedTossModelList.get(position).getTossId();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment toss = new TossLeaderBoard();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,toss)
                    .addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return joinedTossModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView team, entry, prize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.team);
            entry = itemView.findViewById(R.id.entry_fee);
            prize = itemView.findViewById(R.id.prize_pool);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
