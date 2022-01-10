package com.example.india11.StartingActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentCreateNewPasswordBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class CreateNewPassword extends Fragment {
    private FragmentCreateNewPasswordBinding binding;
    String password, confirmPassword;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    Dialog toastDialog;
    TextView toastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNewPasswordBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        binding.submit.setOnClickListener(view1 -> {
            password = binding.password.getText().toString();
            confirmPassword = binding.confirmPassword.getText().toString();
            if (password.isEmpty()){
                binding.password.requestFocus();
                binding.password.setError("Enter current password!");
            }else if (confirmPassword.isEmpty()){
                binding.confirmPassword.requestFocus();
                binding.confirmPassword.setError("Enter new password!");
            }else if (confirmPassword.length() < 8){
                binding.confirmPassword.requestFocus();
                binding.confirmPassword.setError("Password must be of minimum 8 characters.");
            }else if (confirmPassword.equals(password)){
                binding.confirmPassword.requestFocus();
                binding.confirmPassword.setError("Current password and new password can not be same!");
            }else if (firebaseAuth.getCurrentUser() != null){
                AuthCredential credential = EmailAuthProvider.getCredential(Common.profileEmail,password );
                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful())
                            {
                                firebaseUser.updatePassword(confirmPassword)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Intent intent=new Intent(getActivity(), HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            }
                            else
                            {
                                toastMessage.setText("Failed to update!");
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
            else {
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}