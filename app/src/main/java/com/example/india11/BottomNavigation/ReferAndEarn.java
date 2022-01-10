package com.example.india11.BottomNavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentReferAndEarnBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReferAndEarn extends Fragment {
    private FragmentReferAndEarnBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReferAndEarnBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.invcode.setText(Common.userNameID);
        binding.refer.setOnClickListener(view1 -> {
            share();
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
}