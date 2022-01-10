package com.example.india11.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.india11.R;
import com.example.india11.StartingActivity.Login;
import com.example.india11.StartingActivity.Onboarding;
import com.example.india11.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.example.india11.Common.activeFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    final FragmentManager fm = getSupportFragmentManager();
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        Fragment login=new Login();
        loadFragment(login,"Login");
    }
    @SuppressLint("StaticFieldLeak")
    private void loadFragment(Fragment fragment, String tag)
    {
        executorService.execute(() ->{
            if (fragment!=null)
            {
                fm.beginTransaction().replace(R.id.fragment_container, fragment,tag).addToBackStack(tag).commitAllowingStateLoss();
            }
            handler.post(() ->{
                activeFragment=fm.findFragmentById(R.id.fragment_container);
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}