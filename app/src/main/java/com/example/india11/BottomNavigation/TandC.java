package com.example.india11.BottomNavigation;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.TandCAdapter;
import com.example.india11.Model.TandCModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentTandCBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TandC extends Fragment {
    private FragmentTandCBinding binding;
    private List<TandCModel> tandCModelList = new ArrayList<>();
    DatabaseReference databaseReference;
    Dialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTandCBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        binding.tcrecycler.setHasFixedSize(true);
        binding.tcrecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("TermsAndCondition").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tandCModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TandCModel tandCModel = dataSnapshot.getValue(TandCModel.class);
                        tandCModelList.add(tandCModel);
                    }
                    TandCAdapter tandCAdapter = new TandCAdapter(getContext(), tandCModelList);
                    binding.tcrecycler.setAdapter(tandCAdapter);
                    loading.dismiss();
                }else {
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}