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
import com.example.india11.CreateTeam.CreatePlayerTeam;
import com.example.india11.Model.WalletModel;
import com.example.india11.MoneyRelatedLayouts.TransactionDetail;
import com.example.india11.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.ViewHolder> {
    private List<WalletModel> walletModelList;
    public Context context;

    public WalletHistoryAdapter(List<WalletModel> walletModelList, Context context) {
        this.walletModelList = walletModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_history_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WalletModel walletModel = walletModelList.get(position);
        holder.transactionType.setText(walletModelList.get(position).getTransactionType());
        holder.time.setText(walletModelList.get(position).getDateOfRequest());
        holder.tid.setText("Ref. ID: "+walletModelList.get(position).getReferenceId());
        if (walletModelList.get(position).getTransactionType().equals("Added to Wallet")){
            holder.tag.setText("C");
            holder.amount.setText("+"+convertValueInIndianCurrency(Double.parseDouble(walletModelList.get(position).getAmountToWithdraw())));
        }else if (walletModelList.get(position).getTransactionType().equals("Withdrawal Request")){
            holder.tag.setText("D");
            holder.amount.setText("-"+convertValueInIndianCurrency(Double.parseDouble(walletModelList.get(position).getAmountToWithdraw())));
        }else if (walletModelList.get(position).getTransactionType().equals("Winning Amount")){
            holder.tag.setText("W");
            holder.amount.setText("+"+convertValueInIndianCurrency(Double.parseDouble(walletModelList.get(position).getAmountToWithdraw())));
        }
        holder.itemView.setOnClickListener(view -> {
            Common.amountRequest = walletModelList.get(position).getAmountToWithdraw();
            Common.refId= walletModelList.get(position).getReferenceId();
            Common.requestTime = walletModelList.get(position).getDateOfRequest();
            Common.trasactionType = walletModelList.get(position).getTransactionType();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment transactionDetail = new TransactionDetail();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,transactionDetail)
                    .addToBackStack(null).commit();
        });

    }

    @Override
    public int getItemCount() {
        return walletModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView transactionType, tid, time, amount,tag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionType = itemView.findViewById(R.id.transaction_type);
            tid = itemView.findViewById(R.id.tid);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.transaction_amount);
            tag = itemView.findViewById(R.id.tag);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
