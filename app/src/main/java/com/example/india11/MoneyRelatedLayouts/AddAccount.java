package com.example.india11.MoneyRelatedLayouts;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.MenuBarLayouts.Notification;
import com.example.india11.Model.AccountDetailsModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentAddAccountBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class AddAccount extends Fragment {
    private FragmentAddAccountBinding binding;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseAuth firebaseAuth;
    String UID,hName,bName,acNum1,acNum2,ifsc,miPinEntered,miPin;
    ProgressBar progressBar;
    String holderName, bankName, accountNumber, ifsCode;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    Dialog loading;
    Dialog toastDialog;
    TextView toastMessage;
    BottomSheetDialog bankDetailDialog;
    View bankView;
    TextView holder,bank,account,ifs;
    LinearLayout edit, confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAccountBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        binding.toolbarabout.setNavigationOnClickListener(view12 -> requireActivity().onBackPressed());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        binding.save.setOnClickListener(view1 -> {
            saveAccountDetails();
        });
        binding.notification.setOnClickListener(view1 -> {
            Fragment notification = new Notification();
            loadFragment(notification,"Notification");
        });
        bankView = LayoutInflater.from(getContext())
                .inflate(R.layout.bank_detail_confirmation_layout, (LinearLayout)view.findViewById(R.id.bank_layout));
        bankDetailDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bankDetailDialog.setCancelable(true);
        bankDetailDialog.setCanceledOnTouchOutside(false);
        bankDetailDialog.setContentView(bankView);
        holder = bankView.findViewById(R.id.holder);
        bank = bankView.findViewById(R.id.bank);
        account = bankView.findViewById(R.id.account);
        ifs = bankView.findViewById(R.id.ifsc);
        edit = bankView.findViewById(R.id.edit);
        confirm = bankView.findViewById(R.id.submit);
        edit.setOnClickListener(view1 -> {
            bankDetailDialog.dismiss();
        });

        return view;
    }

    private void saveAccountDetails() {
        hName = binding.name.getText().toString();
        bName = binding.bname.getText().toString();
        acNum1 = binding.acn.getText().toString();
        acNum2 = binding.cacn.getText().toString();
        ifsc = binding.ifs.getText().toString();
        miPinEntered = binding.pinView.getText().toString();
        if (hName.isEmpty()){
            binding.name.setError("Enter Account Holder Name");
            binding.name.requestFocus();
        }else if (bName.isEmpty()){
            binding.bname.setError("Enter Bank Name");
            binding.bname.requestFocus();
        }else if (acNum1.isEmpty()){
            binding.acn.setError("Enter Account Number");
            binding.acn.requestFocus();
        }else if (acNum2.isEmpty()){
            binding.cacn.setError("Enter Confirm Account Number");
            binding.cacn.requestFocus();
        }else if (!acNum1.equals(acNum2)){
            toastMessage.setText("Account Number does not match please check!");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            //Toast.makeText(getContext(), "Account Number does not match please check!", Toast.LENGTH_SHORT).show();
        }else if (ifsc.isEmpty()){
            binding.ifs.setError("Enter IFS Code");
            binding.ifs.requestFocus();
        }else if (miPinEntered.isEmpty()){
            toastMessage.setText("Please enter MIPIN.");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            //Toast.makeText(getContext(), "Please enter MIPIN.", Toast.LENGTH_SHORT).show();
        }else {
            databaseReference1.child("INDIA11Users").child(UID).child("MiPin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        miPin = snapshot.child("miPin").getValue().toString();
                        if (!miPinEntered.equals(miPin)){
                            toastMessage.setText("Wrong MIPIN entered. Please try again!");
                            toastDialog.show();
                            final Timer timer3 = new Timer();
                            timer3.schedule(new TimerTask() {
                                public void run() {
                                    toastDialog.dismiss();
                                    timer3.cancel(); //this will cancel the timer of the system
                                }
                            }, 1000);
                        }else {
                            holder.setText(hName);
                            bank.setText(bName);
                            account.setText(acNum1);
                            ifs.setText(ifsc);
                            bankDetailDialog.show();
                            confirm.setOnClickListener(view -> {
                                submitDetails(hName,bName,acNum1,ifsc);
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void submitDetails(String hName, String bName, String acNum1, String ifsc) {
        loading.show();
        binding.loading.setVisibility(View.VISIBLE);
        binding.save.setVisibility(View.INVISIBLE);
        holderName = hName;
        bankName = bName;
        accountNumber = acNum1;
        ifsCode = ifsc;
        AccountDetailsModel accountDetailsModel = new AccountDetailsModel(holderName, bankName, accountNumber, ifsCode);
        databaseReference.child("INDIA11Users").child(UID).child("UserBankDetails").setValue(accountDetailsModel).addOnSuccessListener(aVoid -> {
            binding.loading.setVisibility(View.INVISIBLE);
            binding.save.setVisibility(View.VISIBLE);
            loading.dismiss();
            bankDetailDialog.dismiss();
            toastMessage.setText("Account details saved successfully!");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            //Toast.makeText(getContext(), "Account details saved successfully!", Toast.LENGTH_SHORT).show();
        });
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