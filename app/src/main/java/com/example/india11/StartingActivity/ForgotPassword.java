package com.example.india11.StartingActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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

import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.R;
import com.example.india11.databinding.FragmentForgotPasswordBinding;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class ForgotPassword extends Fragment {
    private FragmentForgotPasswordBinding binding;
    String email;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    Dialog toastDialog;
    TextView toastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        binding.sendEmail.setOnClickListener(view1 -> {
            email = binding.email.getText().toString();
            if (email.isEmpty()){
                binding.email.requestFocus();
                binding.email.setError("Enter email address!");
            }else if (!email.equals(Common.profileEmail)){
                toastMessage.setText("The provided email is not registered with us.");
                toastDialog.show();
                final Timer timer3 = new Timer();
                timer3.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer3.cancel(); //this will cancel the timer of the system
                    }
                }, 2000);
            }else {
                generateOtp(email);
            }
        });
        return view;
    }

    private void generateOtp(String email) {
        Random randomNumber = new Random();
        int number = randomNumber.nextInt(900000)+100000;
        sendCode(number,email);
    }

    private void sendCode(int number, String email) {
        String emailCode = String.valueOf(number);
        Common.verificationCode = emailCode;
        Common.email = email;
        String subject = "Verification code! MYINDIA11";
        String emailContent = "Verification code to change your password is: "+emailCode;
        JavaMailAPI javaMailAPI2 = new JavaMailAPI(getContext(), email, subject, emailContent);
        javaMailAPI2.execute();
        Fragment otp = new OTPVerification();
        loadFragment(otp,"OTPVerification");
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