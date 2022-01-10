package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Contests.ContestsDetails;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AvailableContestsAdapter extends RecyclerView.Adapter<AvailableContestsAdapter.ViewHolder> {
    public Context context;
    private List<AvailableContestsModel> availableContestsModelList;
    String contestId, matchTime;
    String currentDate, contestName;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    Date start, end;
    int teamName = 0;
    FirebaseAuth firebaseAuth;
    String uid;

    public AvailableContestsAdapter(Context context, List<AvailableContestsModel> availableContestsModelList) {
        this.context = context;
        this.availableContestsModelList = availableContestsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_contests_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        currentDate = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(new Date());
        AvailableContestsModel availableContestsModel = availableContestsModelList.get(position);
        Common.holder = holder;
        Common.position = position;
        try {
            start = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH).parse(availableContestsModel.getMatchTime());
            end = new SimpleDateFormat("dd.MM.yyyy, HH:mm",Locale.ENGLISH).parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.contestsName.setText(availableContestsModel.getContestsName());


        //holder.matchTime.setText(Common.time);
        holder.teamOne.setText(availableContestsModel.getTeamOne());

        holder.teamTwo.setText(availableContestsModel.getTeamTwo());
        holder.series.setText(availableContestsModel.getSeriesName().toUpperCase());

        Glide.with(context).load(availableContestsModel.getTeamOneLogo()).into(holder.teamOneLogo);
        Glide.with(context).load(availableContestsModel.getTeamTwoLogo()).into(holder.teamTwoLogo);
        Date endDate;
        try {
            endDate = simpleDateFormat.parse(availableContestsModelList.get(position).getMatchTime());
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final long[] startTime = {System.currentTimeMillis()};

        diff = milliseconds - startTime[0];


            mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime[0] = startTime[0] -1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime[0]) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //holder.matchTime.setText(daysLeft+"D "+hoursLeft+"H:"+minutesLeft+"M:"+secondsLeft+"S");
                if (!daysLeft.equals("0")){
                    holder.matchTime.setText(daysLeft+" Day");
                }else if (daysLeft.equals("0") && !hoursLeft.equals("0")){
                    holder.matchTime.setText(hoursLeft+" Hours");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && !minutesLeft.equals("0")){
                    holder.matchTime.setText(minutesLeft+" Minutes");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && minutesLeft.equals("0") && !secondsLeft.equals("0")){
                    holder.matchTime.setText(secondsLeft+" Seconds");
                }else {
                    holder.matchTime.setText("Match Started");
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();
        holder.itemView.setOnClickListener(view -> {
            DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReference();
            databaseReferenceT.child("INDIA11Users").child(uid).child("teamName").child(availableContestsModelList.get(position).getContestsId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("teamName")){
                        teamName = Integer.parseInt(snapshot.child("teamName").getValue().toString());
                        Common.teamName = teamName+1;

                    }
                    else {
                        Common.teamName = teamName+1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            contestId = availableContestsModel.getContestsId();
            //Toast.makeText(context, contestId, Toast.LENGTH_SHORT).show();
            Common.avlContestId = contestId;
            Common.matchName = availableContestsModel.getContestsName();
            Common.teamOneName = availableContestsModel.getTeamOne();
            Common.teamTwoName = availableContestsModel.getTeamTwo();
            contestName = availableContestsModel.getContestsName();
            matchTime = availableContestsModel.getMatchTime();
            Common.seriesId = availableContestsModelList.get(position).getSeriesId();
            Common.teamOneLogo = availableContestsModelList.get(position).getTeamOneLogo();
            Common.teamTwoLogo = availableContestsModelList.get(position).getTeamTwoLogo();
            Common.seriesName = availableContestsModelList.get(position).getSeriesName();
            Bundle bundle = new Bundle();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            bundle.putString("contest", contestName);
            Common.contestsName = contestName;
            Common.contestsTime = matchTime;
            bundle.putString("timeLeft", matchTime);
            Fragment contestDetails = new ContestsDetails();
            contestDetails.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, contestDetails)
                    .addToBackStack(null).commit();

        });
        if (holder.matchTime.getText().toString().equals("Match Started")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    @Override
    public int getItemCount() {
        return availableContestsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contestsName, matchTime, teamOne, teamTwo,series;
        ImageView teamOneLogo, teamTwoLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contestsName = itemView.findViewById(R.id.contest_name);
            matchTime = itemView.findViewById(R.id.event_timer);
            teamOne = itemView.findViewById(R.id.team_one_name);
            teamTwo = itemView.findViewById(R.id.team_two_name);
            teamOneLogo = itemView.findViewById(R.id.team_one_logo);
            teamTwoLogo = itemView.findViewById(R.id.team_two_logo);
            series = itemView.findViewById(R.id.seriesName);
        }
    }

    public String convertValueInIndianCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
