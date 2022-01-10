package com.example.india11.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Contests.ContestsDetails;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinedMatchAdapter extends RecyclerView.Adapter<JoinedMatchAdapter.ViewHolder> {
    private Context context;
    private List<AvailableContestsModel> availableContestsModelList;
    String contestId, matchTime;
    String currentDate, contestName;
    long diff;
    long milliseconds;
    CountDownTimer mCountDownTimer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    public JoinedMatchAdapter(Context context, List<AvailableContestsModel> availableContestsModelList) {
        this.context = context;
        this.availableContestsModelList = availableContestsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_match_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        currentDate = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(new Date());
        AvailableContestsModel availableContestsModel = availableContestsModelList.get(position);
        holder.contestsName.setText(availableContestsModel.getContestsName());
        //holder.matchTime.setText(Common.time);
        holder.teamOne.setText(availableContestsModel.getTeamOne());

        holder.teamTwo.setText(availableContestsModel.getTeamTwo());

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
                if (!daysLeft.equals("0")){
                    holder.matchTime.setText(daysLeft+" Day");
                }else if (daysLeft.equals("0") && !hoursLeft.equals("0")){
                    holder.matchTime.setText(hoursLeft+" Hours");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && !minutesLeft.equals("0")){
                    holder.matchTime.setText(minutesLeft+" Minutes");
                }else if (daysLeft.equals("0") && hoursLeft.equals("0") && minutesLeft.equals("0") && !secondsLeft.equals("0")){
                    holder.matchTime.setText(secondsLeft+" Seconds");
                }else {
                    holder.matchTime.setText("Live");
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
        databaseReference.child("AvailableContests").child(availableContestsModelList.get(position).getContestsId())
                .child("MatchStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("status").getValue().toString();
                    holder.status.setText(status);
                    if (status.equals("Upcoming")){
                        holder.layout.setVisibility(View.VISIBLE);
                        //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }else {
                        holder.layout.setVisibility(View.GONE);
                        //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(view -> {
            contestId = availableContestsModel.getContestsId();
            //Toast.makeText(context, contestId, Toast.LENGTH_SHORT).show();
            Common.avlContestId = contestId;
            Common.matchName = availableContestsModel.getContestsName();
            Common.teamOneName = availableContestsModel.getTeamOne();
            Common.teamTwoName = availableContestsModel.getTeamTwo();
            contestName = availableContestsModel.getContestsName();
            matchTime = availableContestsModel.getMatchTime();
            Common.teamOneLogo = availableContestsModelList.get(position).getTeamOneLogo();
            Common.teamTwoLogo = availableContestsModelList.get(position).getTeamTwoLogo();
            Common.seriesName = availableContestsModelList.get(position).getSeriesName();
            Common.seriesId = availableContestsModelList.get(position).getSeriesId();
            Common.seriesId = availableContestsModelList.get(position).getSeriesId();
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
    }

    @Override
    public int getItemCount() {
        return availableContestsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contestsName, matchTime, teamOne, teamTwo,status;
        ImageView teamOneLogo, teamTwoLogo;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contestsName = itemView.findViewById(R.id.contest_name);
            matchTime = itemView.findViewById(R.id.event_timer);
            teamOne = itemView.findViewById(R.id.team_one_name);
            teamTwo = itemView.findViewById(R.id.team_two_name);
            teamOneLogo = itemView.findViewById(R.id.team_one_logo);
            teamTwoLogo = itemView.findViewById(R.id.team_two_logo);
            status = itemView.findViewById(R.id.status);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
