package com.example.india11.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Activities.MainActivity;
import com.example.india11.Common;
import com.example.india11.MenuBarLayouts.Notification;
import com.example.india11.MenuBarLayouts.PointsTable;
import com.example.india11.MenuBarLayouts.Wallet;
import com.example.india11.R;
import com.example.india11.StartingActivity.ForgotPassword;
import com.example.india11.StartingActivity.MIPin;
import com.example.india11.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class Settings extends Fragment {
    private FragmentSettingsBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String UID;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        UID = firebaseAuth.getCurrentUser().getUid();
        binding.version.setText("Version "+Common.versionName);
        binding.checkNoti.setOnClickListener(view1 -> {
            Fragment noti = new Notification();
            loadFragment(noti,"Notification");
        });
        binding.wallet.setOnClickListener(view1 -> {
            Fragment checkWallet = new Wallet();
            loadFragment(checkWallet,"Wallet");
        });
        binding.points.setOnClickListener(view1 -> {
            Fragment points = new PointsTable();
            loadFragment(points,"PointsTable");
        });
        binding.profile.setOnClickListener(view1 -> {
            Fragment profile = new Profile();
            loadFragment(profile,"Profile");
        });
        binding.logOut.setOnClickListener(view1 -> {
            if (FirebaseAuth.getInstance() != null){
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        binding.refer.setOnClickListener(view1 -> {
            Fragment refer = new ReferAndEarn();
            loadFragment(refer,"ReferAndEarn");
        });
        binding.tc.setOnClickListener(view1 -> {
            Fragment tc = new TandC();
            loadFragment(tc,"TandC");
        });
        binding.help.setOnClickListener(view1 -> {
            Fragment help = new HelpCenter();
            loadFragment(help,"HelpCenter");
        });
        binding.play.setOnClickListener(view1 -> {
            Fragment play = new HowToPlay();
            loadFragment(play,"HowToPlay");
        });
        binding.changePassword.setOnClickListener(view1 -> {
            Fragment change = new ForgotPassword();
            loadFragment(change,"ForgotPassword");
        });
        binding.changeMipin.setOnClickListener(view1 -> {
            Common.pinType = "Update";
            Fragment change = new MIPin();
            loadFragment(change,"MIPin");
        });
        binding.privacy.setOnClickListener(view1 -> {
            Fragment privacy = new PrivacyPolicy();
            loadFragment(privacy,"PrivacyPolicy");
        });
        return view;
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
}