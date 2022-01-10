package com.example.india11.MenuBarLayouts;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.MoneyRelatedLayouts.AddAccount;
import com.example.india11.MoneyRelatedLayouts.AddAmount;
import com.example.india11.MoneyRelatedLayouts.WalletHistory;
import com.example.india11.MoneyRelatedLayouts.WithdrawAmount;
import com.example.india11.R;
import com.example.india11.databinding.FragmentWalletBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class Wallet extends Fragment {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    private FragmentWalletBinding binding;
    FirebaseAuth firebaseAuth;
    String UID, acHolder, acNumber, acBank, acIfsc,tBalance,bBalance,wBalance,amount;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4;
    Dialog addMoneyDialog;
    EditText enterAmount;
    LinearLayout addMoney;
    BottomSheetDialog miDetailDialog;
    View miView;
    PinView pinView;
    LinearLayout verify;
    Dialog toastDialog;
    TextView toastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentWalletBinding.inflate(inflater,container,false);
       View view = binding.getRoot();
       binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        addMoneyDialog = new Dialog(getContext());
        addMoneyDialog.setContentView(R.layout.add_money_layout);
        addMoneyDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addMoneyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addMoneyDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        miView = LayoutInflater.from(getContext())
                .inflate(R.layout.mipin_layout, (LinearLayout)view.findViewById(R.id.mi_layout));
        miDetailDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        miDetailDialog.setCancelable(true);
        miDetailDialog.setCanceledOnTouchOutside(false);
        miDetailDialog.setContentView(miView);
        pinView = miView.findViewById(R.id.pin_view);
        verify = miView.findViewById(R.id.verify_otp);
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        binding.add.setOnClickListener(view1 -> {
            //addMoneyDialog.show();
            /*Fragment add = new AddAmount();
            loadFragment(add,"AddAmount");*/
            Toast.makeText(getContext(), "Please contact admin to add money in your wallet as you are in testing phase.", Toast.LENGTH_LONG).show();
        });
        binding.addM.setOnClickListener(view1 -> {
            /*Fragment add = new AddAmount();
            loadFragment(add,"AddAmount");*/
            Toast.makeText(getContext(), "Please contact admin to add money in your wallet as you are in testing phase.", Toast.LENGTH_LONG).show();
        });
        binding.notification.setOnClickListener(view1 -> {
            //addMoneyDialog.show();
            Fragment notification = new Notification();
            loadFragment(notification,"Notification");
        });
        enterAmount = addMoneyDialog.findViewById(R.id.amount_to_add);
        addMoney = addMoneyDialog.findViewById(R.id.add_btn);
       databaseReference = FirebaseDatabase.getInstance().getReference();
       databaseReference2 = FirebaseDatabase.getInstance().getReference();
       databaseReference3 = FirebaseDatabase.getInstance().getReference();
       databaseReference4 = FirebaseDatabase.getInstance().getReference();
       firebaseAuth = FirebaseAuth.getInstance();
       UID = firebaseAuth.getCurrentUser().getUid();
       showAccountInfo();
       showAccountBalance();
       binding.addBank.setOnClickListener(view1 -> {
           String ac = binding.acNumber.getText().toString();
           if (ac.equals("Account Number")){
               miDetailDialog.show();
           }else {
               Fragment addBank = new AddAccount();
               loadFragment(addBank,"AddAccount");
           }

       });
       verify.setOnClickListener(view1 -> {
           String miPin = pinView.getText().toString();
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
               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               String uid = firebaseAuth.getCurrentUser().getUid();
               databaseReference.child("INDIA11Users").child(uid).child("MiPin").child("miPin").setValue(miPin);
               toastMessage.setText("MIPIN created successfully.");
               String subject = "MIPIN updated successfully.";
               String body="Hi, "+ Common.profileName+"\nYour security MIPIN has been successfully created." +
                       "\nYour new MIPIN is: "+miPin+
                       "\nIf you have not changed your MIPIN you can immediately contact us."+
                       "\nEmail: "+"support@myindia11.in"+
                       "\nWeb: "+"www.myindia11.in"+
                       "\n\nPlay more, Win more."+
                       "\n\nRegards,"+"\nMYINDIA11";
               JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),Common.profileEmail,subject,body);
               javaMailAPI.execute();
               toastDialog.show();
               final Timer timer2 = new Timer();
               timer2.schedule(new TimerTask() {
                   public void run() {
                       toastDialog.dismiss();
                       timer2.cancel(); //this will cancel the timer of the system
                   }
               }, 1000);
               miDetailDialog.dismiss();
               Fragment addBank = new AddAccount();
               loadFragment(addBank,"AddAccount");
           }
       });
       binding.withdraw.setOnClickListener(view1 -> {
           String ac = binding.acNumber.getText().toString();
           if (ac.equals("Account Number")){
               miDetailDialog.show();
           }else {
               Fragment withdraw = new WithdrawAmount();
               loadFragment(withdraw,"WithdrawAmount");
           }
       });
       binding.walletHistory.setOnClickListener(view1 -> {
           Fragment history = new WalletHistory();
           loadFragment(history,"WalletHistory");
       });
       addMoney.setOnClickListener(view1 -> {
           amount = enterAmount.getText().toString();
           if (amount.isEmpty()){
               enterAmount.setError("Enter amount to add");
               enterAmount.requestFocus();
           }else {
               Common.addedMoney = Long.parseLong(amount);
               //Toast.makeText(getContext(), amount, Toast.LENGTH_SHORT).show();
               addMoneyDialog.dismiss();
               razorpayPayment();
           }
       });
       return view;
    }

    private void showAccountBalance() {
        databaseReference2.child("INDIA11Users").child(UID).child("TotalBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tBalance = snapshot.child("totalBalance").getValue().toString();
                        binding.added.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
                        Common.totalBalance = tBalance;
                        Common.previousTotalMoney = Long.parseLong(tBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference3.child("INDIA11Users").child(UID).child("BonusBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                        bBalance = snapshot.child("bonusBalance").getValue().toString();
                        binding.bonus.setText(convertValueInIndianCurrency(Long.valueOf(bBalance)));
                        Common.bonusBalance = bBalance;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference4.child("INDIA11Users").child(UID).child("WinningBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        wBalance = snapshot.child("winningBalance").getValue().toString();
                        binding.winning.setText(convertValueInIndianCurrency(Long.valueOf(wBalance)));
                        Common.winningBalance = wBalance;
                        Common.previousWinningAmount = Long.parseLong(wBalance);
                        long t = Long.parseLong(tBalance);
                        long w = Long.parseLong(wBalance);
                        binding.total.setText(convertValueInIndianCurrency(t+w));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showAccountInfo() {
        databaseReference.child("INDIA11Users").child(UID).child("UserBankDetails").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        acHolder = snapshot.child("holderName").getValue().toString();
                        acNumber = snapshot.child("accountNumber").getValue().toString();
                        acBank = snapshot.child("bankName").getValue().toString();
                        acIfsc = snapshot.child("ifsCode").getValue().toString();
                        if (acHolder.equals("None")){
                            binding.holderName.setText("Account Holder Name");
                            Common.accountHolder = acHolder;
                        }else if (acNumber.equals("None")){
                            binding.acNumber.setText("Account Number");
                            Common.accountNumber = acNumber;
                        }else if (acBank.equals("None")){
                            binding.bankName.setText("Bank Name");
                            Common.accountBank = acBank;
                        }else if (acIfsc.equals("None")){
                            binding.ifsc.setText("IFSC");
                            Common.accountIfsc = acIfsc;
                        }else {
                            char delimiter =' ';
                            String number = acNumber.replaceAll(".{4}(?!$)", "$0" + delimiter);
                            binding.holderName.setText(acHolder);
                            binding.acNumber.setText(number);
                            binding.bankName.setText(acBank);
                            binding.ifsc.setText(acIfsc);
                            Common.accountHolder = acHolder;
                            Common.accountBank = acBank;
                            Common.accountNumber = acNumber;
                            Common.accountIfsc = acIfsc;
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
    public String convertValueInIndianCurrency(Long amount)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
    private void razorpayPayment() {
        String net_amount = String.valueOf(amount).replace(",", "");

        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "MI11");
            options.put("description", "Cephnox Technologies Pvt. Ltd.");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(net_amount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", Common.profileEmail);
            preFill.put("contact", Common.profileMobile);

            options.put("prefill", preFill);
            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}