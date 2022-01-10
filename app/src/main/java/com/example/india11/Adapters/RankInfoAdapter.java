package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.RankInfoModel;
import com.example.india11.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RankInfoAdapter extends RecyclerView.Adapter<RankInfoAdapter.ViewHolder> {
    public Context context;
    private List<RankInfoModel> rankInfoModelList;

    public RankInfoAdapter(Context context, List<RankInfoModel> rankInfoModelList) {
        this.context = context;
        this.rankInfoModelList = rankInfoModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankInfoModel rankInfoModel = rankInfoModelList.get(position);
        holder.rank.setText("#"+rankInfoModel.getRank());
        holder.amount.setText(convertValueInIndianCurrency(rankInfoModel.getAmount()));
    }

    @Override
    public int getItemCount() {
        return rankInfoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            amount = itemView.findViewById(R.id.winning_amount);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
