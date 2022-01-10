package com.example.india11.MoneyRelatedLayouts;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.MenuBarLayouts.Notification;
import com.example.india11.Model.TotalBalanceModel;
import com.example.india11.Model.WalletModel;
import com.example.india11.Model.WinningBalanceModel;
import com.example.india11.Model.WithdrawlRequestModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentWithdrawAmountBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;


public class WithdrawAmount extends Fragment {
    private FragmentWithdrawAmountBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String UID,requestAmount,referenceID,tBalance;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5;
    public long winningBalance,withdrawAmount,balance;
    public long amountLimit = 150;
    String transactionType;
    Dialog loading;
    Dialog toastDialog;
    TextView toastMessage;
    BottomSheetDialog bankDetailDialog;
    View bankView;
    TextView holder,bank,account,ifs,amount;
    LinearLayout edit, confirm;
    String accountHolderName, accountNumber, bankName, ifsCode, amountToWithdraw, referenceId, dateOfRequest, status, mobileNo, emailId;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWithdrawAmountBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        loading = new Dialog(getContext());
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        bankView = LayoutInflater.from(getContext())
                .inflate(R.layout.withdrawal_request_layout, (LinearLayout)view.findViewById(R.id.bank_layout));
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
        amount = bankView.findViewById(R.id.amount);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 =FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference4.child("INDIA11Users").child(UID).child("WinningBalance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tBalance = snapshot.child("winningBalance").getValue().toString();
                binding.moneyBalance.setText(convertValueInIndianCurrency(Long.valueOf(tBalance)));
                balance = Long.parseLong(tBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.notification.setOnClickListener(view1 -> {
            //addMoneyDialog.show();
            Fragment notification = new Notification();
            loadFragment(notification,"Notification");
        });

        binding.note.setText("Note: Minimum withdrawal amount should be "+convertValueInIndianCurrency(Long.valueOf(150)));
        char delimiter =' ';
        String number = Common.accountNumber.replaceAll(".{4}(?!$)", "$0" + delimiter);
        binding.holderName.setText(Common.accountHolder);
        binding.acNumber.setText(number);
        binding.bankName.setText(Common.accountBank);
        binding.ifsc.setText(Common.accountIfsc);
        //binding.moneyBalance.setText(convertValueInIndianCurrency(Long.valueOf(Common.totalBalance)));
        //balance = Long.parseLong(Common.totalBalance);
        binding.request.setOnClickListener(view1 -> {
            requestAmount = binding.amount.getText().toString();
            withdrawAmount = Long.parseLong(requestAmount);
            if (withdrawAmount == 0){
                binding.amount.setError("Enter amount to withdraw.");
                binding.amount.requestFocus();
            }else if (withdrawAmount < amountLimit){
                Toast.makeText(getContext(), "Withdraw amount should be more than Rs.150", Toast.LENGTH_SHORT).show();

            }else if (balance == 0 || balance < 150){
                Toast.makeText(getContext(), "You do not have sufficient balance to withdraw.", Toast.LENGTH_SHORT).show();
            }else if (withdrawAmount > balance){
                Toast.makeText(getContext(), "Your entered amount is more than balance available.", Toast.LENGTH_SHORT).show();
            }else{
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
                databaseReference2.child("INDIA11Users").child(UID).child("UserBankDetails").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String accountNumber = snapshot.child("accountNumber").getValue().toString();
                            String bankName = snapshot.child("bankName").getValue().toString();
                            String holderName = snapshot.child("holderName").getValue().toString();
                            String ifsCode = snapshot.child("ifsCode").getValue().toString();
                            Common.accountHolder = holderName;
                            Common.accountIfsc = ifsCode;
                            Common.accountBank = bankName;
                            Common.accountNumber = accountNumber;
                            bankDetailDialog.show();
                            amount.setText(convertValueInIndianCurrency(withdrawAmount));
                            holder.setText(holderName);
                            bank.setText(bankName);
                            account.setText(accountNumber);
                            ifs.setText(ifsCode);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        edit.setOnClickListener(view1 -> {
            bankDetailDialog.dismiss();
            Fragment fragment = new AddAccount();
            loadFragment(fragment,"AddAccount");
        });
        confirm.setOnClickListener(view2 -> {
            loading.show();
            Random random = new Random();
            int refId = random.nextInt(1000000000);
            referenceID = "INDREFWITH"+refId;
            Common.withdrawAmountRequest = Long.parseLong(requestAmount);
            submitRequest(referenceID);
        });
        return view;
    }

    private void submitRequest(String referenceID) {
        binding.loading.setVisibility(View.VISIBLE);
        binding.request.setVisibility(View.INVISIBLE);
        accountHolderName = Common.accountHolder;
        accountNumber = Common.accountNumber;
        bankName = Common.accountBank;
        ifsCode = Common.accountIfsc;
        amountToWithdraw = requestAmount;
        referenceId = referenceID;
        dateOfRequest = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        status = "Pending";
        mobileNo = Common.profileMobile;
        emailId = Common.profileEmail;
        transactionType = "Withdrawal Request";
        WithdrawlRequestModel withdrawlRequestModel = new WithdrawlRequestModel(accountHolderName, accountNumber, bankName, ifsCode, amountToWithdraw, referenceId, dateOfRequest, status, mobileNo, emailId);
        WalletModel walletModel = new WalletModel(amountToWithdraw, referenceId, dateOfRequest,transactionType);
        databaseReference.child("INDIA11Users").child(UID).child("WalletHistory").child(referenceId).setValue(walletModel)
                .addOnSuccessListener(aVoid -> {
                    winningBalance = Common.previousWinningAmount - Common.withdrawAmountRequest;
                    WinningBalanceModel winningBalanceModel = new WinningBalanceModel(winningBalance);
                    databaseReference2.child("INDIA11Users").child(UID).child("WinningBalance").setValue(winningBalanceModel).addOnSuccessListener(aVoid1 -> {
                    });
                });
        databaseReference3.child("AmountWithdrawRequest").child(referenceId).setValue(withdrawlRequestModel)
                .addOnSuccessListener(aVoid -> {
                    binding.loading.setVisibility(View.INVISIBLE);
                    binding.request.setVisibility(View.VISIBLE);
                    Common.totalBalance = String.valueOf(winningBalance);
                    String subject = "Withdrawal request of "+convertValueInIndianCurrency(Long.valueOf(amountToWithdraw))+" received.";
                    String body="Hello, "+Common.profileName+"\nYour amount withdrawal request has been placed." +"\nReference"+
                            " id: "+referenceID+"\nTime: "+dateOfRequest+"\nThanks for reaching out to us " +
                            " your amount will be processed and credited to your given bank details within 24hrs. except bank holidays. Till then Stay safe and enjoy playing on MYINDIA11."+"\n" +
                            "\nRegards,"+"\nMYINDIA11 Team";
                    JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),Common.profileEmail,subject,body);
                    javaMailAPI.execute();
                    loading.dismiss();
                    bankDetailDialog.dismiss();
                    toastMessage.setText("Request submitted successfully!");
                    toastDialog.show();
                    final Timer timer3 = new Timer();
                    timer3.schedule(new TimerTask() {
                        public void run() {
                            toastDialog.dismiss();
                            timer3.cancel(); //this will cancel the timer of the system
                        }
                    }, 1000);
                    //Toast.makeText(getContext(), "Request submitted successfully!", Toast.LENGTH_SHORT).show();
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
}