package com.example.india11.StartingActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.R;
import com.example.india11.databinding.FragmentResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class ResetPassword extends Fragment {
    private FragmentResetPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    Dialog toastDialog;
    TextView toastMessage;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        binding.sendEmail.setOnClickListener(view1 -> {
            String email = binding.email.getText().toString();
            if (email.isEmpty()){
                binding.email.requestFocus();
                binding.email.setError("Enter your email!");
            }else {
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                toastMessage.setText("Reset link has been sent to "+email+" please check and follow the instructions.");
                                toastDialog.show();
                                final Timer timer3 = new Timer();
                                timer3.schedule(new TimerTask() {
                                    public void run() {
                                        toastDialog.dismiss();
                                        timer3.cancel(); //this will cancel the timer of the system
                                    }
                                }, 2000);
                                Fragment login = new Login();
                                loadFragment(login,"Login");
                            }else {
                                toastMessage.setText("Failed to send reset password link. Please try again!");
                                toastDialog.show();
                                final Timer timer3 = new Timer();
                                timer3.schedule(new TimerTask() {
                                    public void run() {
                                        toastDialog.dismiss();
                                        timer3.cancel(); //this will cancel the timer of the system
                                    }
                                }, 2000);
                            }
                        });
            }
        });
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