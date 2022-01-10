package com.example.india11.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.ObjIntConsumer;

import static com.example.india11.Common.activeFragment;
import static com.example.india11.Common.link;

public class Profile extends Fragment {
    private FragmentProfileBinding binding;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseAuth firebaseAuth;
    String uid;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    int played, won;
    double invested, earned, percent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        loadProfileInfo();
        loadStatsData();
        binding.edit.setOnClickListener(view1 -> {
            Fragment edit = new EditProfile();
            loadFragment(edit,"EditProfile");
        });
        binding.share.setOnClickListener(view1 -> {
            share();
        });
        binding.sendEmail.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@myindia11.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(intent, ""));
        });
        binding.copy.setOnClickListener(view1 -> {
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
            databaseReference2.child("AppLink").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String link = snapshot.child("link").getValue().toString();
                        String share_message="Hi, install MYINDIA11 and win cash daily.Get cash bonus by using my referral code +"+Common.userNameID+" "+link+"\n\n";
                        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipCode = ClipData.newPlainText("CouponCode", share_message);
                        clipboardManager.setPrimaryClip(clipCode);
                        Toast.makeText(getContext(), "Code copied successfully!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        return view;
    }

    private void share() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("AppLink").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String link = snapshot.child("link").getValue().toString();
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Share with");
                    String share_message="Hi, install MYINDIA11 and win cash daily.Get cash bonus by using my referral code +"+Common.userNameID+" "+link+"\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT,share_message);
                    startActivity(Intent.createChooser(intent,"Share Via"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadStatsData() {
        databaseReference.child("INDIA11Users").child(uid).child("UserStats").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                played = Integer.parseInt(snapshot.child("played").getValue().toString());
                won = Integer.parseInt(snapshot.child("won").getValue().toString());
                invested = Double.parseDouble(snapshot.child("invested").getValue().toString());
                earned = Double.parseDouble(snapshot.child("earned").getValue().toString());
                binding.playedMatch.setText(String.valueOf(played));
                binding.wonMatch.setText(String.valueOf(won));
                binding.invested.setText(convertValueInIndianCurrency(invested));
                binding.earned.setText(convertValueInIndianCurrency(earned));
                if (won == 0 && played==0){
                    percent=0;
                }else {
                    percent = (won/played)*100;
                    binding.progress.setProgress((int) percent);
                    DecimalFormat decimalFormat = new DecimalFormat("##.##%");
                    String per = decimalFormat.format(percent);
                    binding.percent.setText(per);
                }

                if (invested < 100){
                    binding.badge.setVisibility(View.GONE);
                }else {
                    binding.badge.setVisibility(View.VISIBLE);
                }
                if (invested < 150 && played < 8){
                    binding.note.setText("Invest Rs."+(150-invested)+" more or join "+(8-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(8);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 150 && played == 8){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(2);
                }else if (invested < 250 && played < 12){
                    binding.note.setText("Invest Rs."+(250-invested)+" more or join "+(12-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(12);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 250 && played == 12){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(3);
                }else if (invested < 450 && played < 16){
                    binding.note.setText("Invest Rs."+(450-invested)+" more or join "+(16-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(16);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 450 && played == 16){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(4);
                }else if (invested < 750 && played < 20){
                    binding.note.setText("Invest Rs."+(750-invested)+" more or join "+(20-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(20);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 750 && played == 20){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(5);
                }else if (invested < 1000 && played < 25){
                    binding.note.setText("Invest Rs."+(1000-invested)+" more or join "+(25-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(25);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 1000 && played == 25){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(6);
                }else if (invested < 1200 && played < 30){
                    binding.note.setText("Invest Rs."+(1200-invested)+" more or join "+(30-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(30);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 1200 && played == 30){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(7);
                }else if (invested < 1500 && played < 35){
                    binding.note.setText("Invest Rs."+(1500-invested)+" more or join "+(35-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(35);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 1500 && played == 35){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(8);
                }else if (invested < 1750 && played < 40){
                    binding.note.setText("Invest Rs."+(1750-invested)+" more or join "+(40-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(40);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 1750 && played == 40){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(9);
                }else if (invested < 2000 && played < 45){
                    binding.note.setText("Invest Rs."+(2000-invested)+" more or join "+(45-played)+" more matches to reach next level.");
                    binding.levelProgress.setMax(45);
                    binding.levelProgress.setProgress(played);
                }else if (invested > 2000 && played == 45){
                    DatabaseReference databaseReferenceLevel = FirebaseDatabase.getInstance().getReference();
                    databaseReferenceLevel.child("INDIA11Users").child(uid).child("UserInfo").child("level")
                            .setValue(10);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProfileInfo() {
        databaseReference.child("INDIA11Users").child(uid).child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userNameID = snapshot.child("userReferralCode").getValue().toString();
                binding.invcode.setText(userNameID);
                String profileName = snapshot.child("userName").getValue().toString();
                binding.name.setText(profileName);
                String profileEmail = snapshot.child("userEmail").getValue().toString();
                binding.email.setText(profileEmail);
                String profileMobile = snapshot.child("userMobile").getValue().toString();
                binding.mobile.setText(profileMobile);
                String nick = snapshot.child("nickName").getValue().toString();
                binding.username.setText(nick);
                int level = Integer.parseInt(snapshot.child("level").getValue().toString());
                binding.level.setText(String.valueOf(level));
                binding.upcomingLevel.setText(String.valueOf(level+1));
                String joined = snapshot.child("joinedOn").getValue().toString();
                binding.joinedOn.setText(joined);
                String profile = snapshot.child("profilePic").getValue().toString();
                Glide.with(getContext()).load(profile).into(binding.profilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadFragment(Fragment fragment, String tag) {
        executorService.execute(() -> {
            if (fragment != null) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(tag).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
            });
        });
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}