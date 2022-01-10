package com.example.india11.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.BottomNavigation.Chat;
import com.example.india11.Common;
import com.example.india11.CreateTeam.TeamPreview;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReferralConnectionAdapter extends RecyclerView.Adapter<ReferralConnectionAdapter.ViewHolder> {
    public Context context;
    private List<ReferralConnectionModel> referralConnectionModelList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    CircleImageView opponent, you;
    TextView opponentName, yourName, youPlayed, opponentPlayed, opponentWon, youWon, youEarned, opponentEarned;
    Dialog comparison;

    public ReferralConnectionAdapter(Context context, List<ReferralConnectionModel> referralConnectionModelList) {
        this.context = context;
        this.referralConnectionModelList = referralConnectionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_connection_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        comparison = new Dialog(context);
        comparison.setContentView(R.layout.comparison_layout);
        comparison.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        comparison.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        comparison.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        comparison.getWindow().setGravity(Gravity.BOTTOM);
        opponent = comparison.findViewById(R.id.opponentPic);
        you = comparison.findViewById(R.id.profilePic);
        opponentPlayed = comparison.findViewById(R.id.opponentPlayed);
        youPlayed = comparison.findViewById(R.id.youPlayed);
        opponentWon = comparison.findViewById(R.id.opponentWon);
        youWon = comparison.findViewById(R.id.youWon);
        opponentEarned = comparison.findViewById(R.id.opponentEarned);
        youEarned = comparison.findViewById(R.id.youEarned);
        opponentName = comparison.findViewById(R.id.opponentName);
        yourName = comparison.findViewById(R.id.profileName);
        ReferralConnectionModel referralConnectionModel = referralConnectionModelList.get(position);
        databaseReference.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid())
                .child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("userName").getValue().toString();
                    String image = snapshot.child("profilePic").getValue().toString();
                    //holder.user.setText(name);
                    if (!image.equals("")){
                        Glide.with(context).load(image).into(holder.picture);
                    }else {
                        Glide.with(context).load(referralConnectionModel.getConnectionPic()).into(holder.picture);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.name.setText(referralConnectionModel.getConnectionName());
        holder.mobile.setText(referralConnectionModel.getConnectionMobile());
        holder.call.setOnClickListener(view -> {
            String number = referralConnectionModelList.get(position).getConnectionMobile();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));
            if (ActivityCompat.checkSelfPermission((view.getContext()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 10);
            }
            else {
                try{
                    context.startActivity(callIntent);  //call activity and make phone call
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(context,"Your Activity is not found",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemView.setOnClickListener(view -> {
            comparison.show();
            DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference();
            databaseReference5.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid()).child("UserStats")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String earned = snapshot.child("earned").getValue().toString();
                                String played = snapshot.child("played").getValue().toString();
                                String won = snapshot.child("won").getValue().toString();
                                opponentPlayed.setText(played);
                                opponentWon.setText(won);
                                opponentEarned.setText(convertValueInIndianCurrency(Double.parseDouble(earned)));
                                Glide.with(context).load(Common.userProfilePic).into(you);
                                yourName.setText(Common.nickName);
                                youPlayed.setText(Common.played);
                                youWon.setText(Common.won);
                                youEarned.setText(convertValueInIndianCurrency(Double.parseDouble(Common.earned)));
                                Glide.with(context).load(referralConnectionModelList.get(position).getConnectionPic()).into(opponent);
                                opponentName.setText(referralConnectionModelList.get(position).getConnectionName());
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
        return referralConnectionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView picture;
        ImageView call;
        TextView name, mobile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.phone);
            call = itemView.findViewById(R.id.call);
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
