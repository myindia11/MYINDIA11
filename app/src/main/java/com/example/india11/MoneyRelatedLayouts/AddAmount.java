package com.example.india11.MoneyRelatedLayouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentAddAmountBinding;
import com.google.firebase.database.ServerValue;
import com.razorpay.Checkout;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AddAmount extends Fragment{
    private FragmentAddAmountBinding binding;
    String upiId = "9877728796-1@okbizaxis";
    String name = "Cephnox Technologies";
    String note = "Money Adding to Wallet";
    String amount;
    final int UPI_PAYMENT = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAmountBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view12 -> requireActivity().onBackPressed());
        binding.amountBox.setText("100");
        binding.r1.setText(convertValueInIndianCurrency(100));
        binding.r2.setText(convertValueInIndianCurrency(200));
        binding.r3.setText(convertValueInIndianCurrency(500));
        binding.r4.setText(convertValueInIndianCurrency(1000));
        binding.hundred.setOnClickListener(view1 -> {
            binding.amountBox.setText("100");
            binding.hundred.setCardBackgroundColor(Color.parseColor("#C9FACB"));
            binding.tHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.fHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.thousand.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.tHundred.setOnClickListener(view1 -> {
            binding.amountBox.setText("200");
            binding.hundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.tHundred.setCardBackgroundColor(Color.parseColor("#C9FACB"));
            binding.fHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.thousand.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.fHundred.setOnClickListener(view1 -> {
            binding.amountBox.setText("500");
            binding.hundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.tHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.fHundred.setCardBackgroundColor(Color.parseColor("#C9FACB"));
            binding.thousand.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        });
        binding.thousand.setOnClickListener(view1 -> {
            binding.amountBox.setText("1000");
            binding.hundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.tHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.fHundred.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.thousand.setCardBackgroundColor(Color.parseColor("#C9FACB"));
        });
        binding.next.setOnClickListener(view1 -> {
            String amountToAdd = binding.amountBox.getText().toString();
            int am = Integer.parseInt(amountToAdd);
            if (amountToAdd.isEmpty()){
                binding.amountBox.setError("Amount");
                binding.amountBox.requestFocus();
            }else if ( am < 5){
                binding.amountBox.setError("Minimum Rs.5 needed to add.");
                binding.amountBox.requestFocus();
            }else {
                binding.amountTray.setVisibility(View.GONE);
                binding.payTray.setVisibility(View.VISIBLE);
                amount = amountToAdd;

            }
        });
        binding.upkey1.setOnClickListener(view1 -> {
            binding.amountTray.setVisibility(View.GONE);
            binding.downkey1.setVisibility(View.VISIBLE);
            binding.upkey1.setVisibility(View.GONE);
        });
        binding.downkey1.setOnClickListener(view1 -> {
            binding.amountTray.setVisibility(View.VISIBLE);
            binding.upkey1.setVisibility(View.VISIBLE);
            binding.downkey1.setVisibility(View.GONE);
        });
        binding.upkey2.setOnClickListener(view1 -> {
            binding.payTray.setVisibility(View.GONE);
            binding.downkey2.setVisibility(View.VISIBLE);
            binding.upkey2.setVisibility(View.GONE);
        });
        binding.downkey2.setOnClickListener(view1 -> {
            binding.payTray.setVisibility(View.VISIBLE);
            binding.upkey2.setVisibility(View.VISIBLE);
            binding.downkey2.setVisibility(View.GONE);
        });
        binding.Pay.setOnClickListener(view1 -> {
            Common.addedMoney = Long.parseLong(amount);
            razorpayPayment();
        });
        return view;
    }

    private void payUsingUpi(String name, String upiId, String note, String amount) {
        Random random = new Random();
        int refId = random.nextInt(99999)+10000;
        Random random1 = new Random();
        int trId = random1.nextInt(999999999)+100000000;
        String tid = String.valueOf(System.currentTimeMillis());
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "BCR2DN6TY6AMJXC6")
                .appendQueryParameter("tid",tid)
                .appendQueryParameter("tr", String.valueOf(trId))
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    private void razorpayPayment() {
        String net_amount = String.valueOf(amount).replace(",", "");

        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Appname");
            options.put("description", "Cephnox Technologies Pvt. Ltd.");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/india11fantasy-bb723.appspot.com/o/myindia_11_half_logo.png?alt=media&token=eb4501b3-2997-4051-b382-44a4fd7e96dd");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        //Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        //Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    //Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getContext())) {
            String str = data.get(0);
            //Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(getContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                //Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                //Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(getContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
               // Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Toast.makeText(getContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}
