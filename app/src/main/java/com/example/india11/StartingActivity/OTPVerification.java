package com.example.india11.StartingActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.R;
import com.example.india11.databinding.FragmentOTPVerificationBinding;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class OTPVerification extends Fragment {
    private FragmentOTPVerificationBinding binding;
    String verificationCode,enteredCode;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    Dialog toastDialog;
    TextView toastMessage;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentOTPVerificationBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
       binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
       binding.codeText.setText("Code sent to: "+ Common.email);
       verificationCode = Common.verificationCode;
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
       new CountDownTimer(30000, 1000){

           @Override
           public void onTick(long l) {
               binding.timer.setText("Resend Code in: "+l / 1000);
           }

           @Override
           public void onFinish() {
               binding.timer.setText("Resend Code Again");
           }
       }.start();
       binding.verifyOtp.setOnClickListener(view1 -> {
           verify();
           binding.loading.setVisibility(View.VISIBLE);
           binding.verifyOtp.setVisibility(View.INVISIBLE);
       });
       binding.timer.setOnClickListener(view1 -> {
           if (binding.timer.getText().equals("Resend Code Again")){
               Random randomNumber = new Random();
               int number = randomNumber.nextInt(900000)+100000;
               String emailCode = String.valueOf(number);
               Common.verificationCode = emailCode;
               String subject = "Verification code! MYINDIA11";
               String emailContent = "Verification code to change your password is: "+emailCode;
               JavaMailAPI javaMailAPI2 = new JavaMailAPI(getContext(), Common.email, subject, emailContent);
               javaMailAPI2.execute();
               toastMessage.setText("OTP resent successfully.");
               toastDialog.show();
               final Timer timer3 = new Timer();
               timer3.schedule(new TimerTask() {
                   public void run() {
                       toastDialog.dismiss();
                       timer3.cancel(); //this will cancel the timer of the system
                   }
               }, 1000);
           }else {
               toastMessage.setText("Please wait for timer!");
               toastDialog.show();
               final Timer timer3 = new Timer();
               timer3.schedule(new TimerTask() {
                   public void run() {
                       toastDialog.dismiss();
                       timer3.cancel(); //this will cancel the timer of the system
                   }
               }, 1000);
           }
       });

       return view;
    }

    private void verify() {
        enteredCode = binding.pinView.getText().toString();
        if (enteredCode.isEmpty()){
            toastMessage.setText("Enter Code to verify!");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            binding.loading.setVisibility(View.INVISIBLE);
            binding.verifyOtp.setVisibility(View.VISIBLE);
        }else if (!enteredCode.equals(verificationCode)){
            toastMessage.setText("Entered code is invalid! Please check.");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            binding.loading.setVisibility(View.INVISIBLE);
            binding.verifyOtp.setVisibility(View.VISIBLE);
        }else {
            binding.loading.setVisibility(View.INVISIBLE);
            binding.verifyOtp.setVisibility(View.VISIBLE);
            toastMessage.setText("Verified");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            Fragment change = new CreateNewPassword();
            loadFragment(change,"CreateNewPassword");
        }
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