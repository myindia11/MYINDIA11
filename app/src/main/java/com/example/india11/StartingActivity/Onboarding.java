package com.example.india11.StartingActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.Adapters.ViewPagerAdapter;
import com.example.india11.R;
import com.example.india11.databinding.FragmentOnboardingBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class Onboarding extends Fragment {
    private FragmentOnboardingBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    int images[] = {R.drawable.play_img,R.drawable.win_img,R.drawable.withdrawal_img};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.signup.setOnClickListener(view1 -> {
            Fragment signup = new Signup();
            loadFragment(signup,"Signup");
        });
        binding.login.setOnClickListener(view1 -> {
            Fragment login = new Login();
            loadFragment(login,"Login");
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(),images);
        binding.viewPager.setAdapter(viewPagerAdapter);
        return view;
    }
    private void loadFragment(Fragment fragment, String tag) {
        executorService.execute(() -> {
            if (fragment != null) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, tag).addToBackStack(tag).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            });
        });
    }
}