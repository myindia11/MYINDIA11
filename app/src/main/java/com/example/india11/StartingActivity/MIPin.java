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
import com.example.india11.databinding.FragmentMIPinBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class MIPin extends Fragment {
    private FragmentMIPinBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String miPin;
    Dialog toastDialog;
    TextView toastMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentMIPinBinding.inflate(inflater,container,false);
      View view = binding.getRoot();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
      binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
      binding.verifyOtp.setOnClickListener(view1 -> {
          miPin = binding.pinView.getText().toString();
          if (miPin.isEmpty()){
              toastMessage.setText("Please enter MIPIN");
              toastDialog.show();
              final Timer timer3 = new Timer();
              timer3.schedule(new TimerTask() {
                  public void run() {
                      toastDialog.dismiss();
                      timer3.cancel(); //this will cancel the timer of the system
                  }
              }, 1000);
              //Toast.makeText(getContext(), "Enter Code to verify!", Toast.LENGTH_SHORT).show();
          }else {
              if (Common.pinType.equals("Update")) {
                  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                  FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                  String uid = firebaseAuth.getCurrentUser().getUid();
                  databaseReference.child("INDIA11Users").child(uid).child("MiPin").child("miPin").setValue(miPin);
                  toastMessage.setText("MIPIN changed successfully.");
                  String subject = "MIPIN updated successfully.";
                  String body = "Hi, " + Common.profileName + "\nYour security MIPIN has been successfully changed/updated." +
                          "\nYour new MIPIN is: " + miPin +
                          "\nIf you have not changed your MIPIN you can immediately contact us." +
                          "\nEmail: " + "support@myindia11.in" +
                          "\nWeb: " + "www.myindia11.in" +
                          "\n\nPlay more, Win more." +
                          "\n\nRegards," + "\nMYINDIA11";
                  JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), Common.profileEmail, subject, body);
                  javaMailAPI.execute();
                  toastDialog.show();
                  final Timer timer2 = new Timer();
                  timer2.schedule(new TimerTask() {
                      public void run() {
                          toastDialog.dismiss();
                          timer2.cancel(); //this will cancel the timer of the system
                      }
                  }, 1000);
              }

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