package com.example.india11.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Common;
import com.example.india11.Model.TossSubContestModel;
import com.example.india11.R;
import com.example.india11.TossPrediction.PredictToss;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TossSubContestAdapter extends RecyclerView.Adapter<TossSubContestAdapter.ViewHolder> {
    public Context context;
    private List<TossSubContestModel> tossSubContestModelList;
    Dialog toastDialog;
    TextView toastMessage;

    public TossSubContestAdapter(Context context, List<TossSubContestModel> tossSubContestModelList) {
        this.context = context;
        this.tossSubContestModelList = tossSubContestModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toss_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TossSubContestModel tossSubContestModel = tossSubContestModelList.get(position);
        toastDialog = new Dialog(context);
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        holder.prizePool.setText(convertValueInIndianCurrency(tossSubContestModel.getPrizePool()));
        holder.entry.setText(convertValueInIndianCurrency(tossSubContestModel.getEntryFee()));
        holder.totalSpot.setText(String.valueOf(tossSubContestModel.getTotalSpots()));
        holder.joinedSpot.setText(String.valueOf(tossSubContestModel.getJoined()));
        holder.winner.setText(tossSubContestModel.getTotalWinners());
        holder.progressBar.setMax(tossSubContestModel.getTotalSpots());
        holder.progressBar.setProgress(tossSubContestModel.getJoined());
        holder.join.setOnClickListener(view -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("JoinedTossLeague").child(Common.avlContestId).child(tossSubContestModelList.get(position).getTossId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Common.userNameID)){
                                //Toast.makeText(context, "You have already joined this contest.", Toast.LENGTH_SHORT).show();
                                toastMessage.setText("You have already joined this contest.");
                                toastDialog.show();
                                final Timer timer2 = new Timer();
                                timer2.schedule(new TimerTask() {
                                    public void run() {
                                        toastDialog.dismiss();
                                        timer2.cancel(); //this will cancel the timer of the system
                                    }
                                }, 1000);
                            }
                            else {
                                Common.entryFee = tossSubContestModelList.get(position).getEntryFee();
                                Common.tossCId = tossSubContestModelList.get(position).getTossId();
                                Common.tossPrize = tossSubContestModelList.get(position).getPrizePool();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Fragment toss = new PredictToss();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, toss)
                                        .addToBackStack(null).commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return tossSubContestModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prizePool, entry, totalSpot, joinedSpot, winner;
        ProgressBar progressBar;
        LinearLayout join;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prizePool = itemView.findViewById(R.id.prize_pool);
            entry = itemView.findViewById(R.id.entry_fee);
            totalSpot = itemView.findViewById(R.id.total_spots);
            joinedSpot = itemView.findViewById(R.id.left_spot);
            winner = itemView.findViewById(R.id.winners_number);
            progressBar = itemView.findViewById(R.id.joinedProgress);
            join = itemView.findViewById(R.id.join_btn);

        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
