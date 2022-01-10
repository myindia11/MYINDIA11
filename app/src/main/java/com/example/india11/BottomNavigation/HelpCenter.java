package com.example.india11.BottomNavigation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.Model.ComplaintModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentHelpCenterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class HelpCenter extends Fragment {
    private FragmentHelpCenterBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    Dialog complaint;
    EditText messageBox;
    LinearLayout submitComplaint;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseAuth firebaseAuth;
    String uid;
    String complaintId, complaintDate, message, userName, userEmail, userMobile,userId,status;

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpCenterBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        complaint = new Dialog(getContext());
        complaint.setContentView(R.layout.complaint_layout);
        complaint.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        complaint.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        complaint.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        complaint.getWindow().setGravity(Gravity.BOTTOM);
        messageBox = complaint.findViewById(R.id.message);
        submitComplaint = complaint.findViewById(R.id.submit);

        binding.complaint.setOnClickListener(view1 -> {
            complaint.show();
        });

        binding.email.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@myindia11.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(intent, ""));
        });
        binding.faq.setOnClickListener(view1 -> {
            Fragment faq = new FAQ();
            loadFragment(faq,"FAQ");
        });
        binding.fb.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Will be available soon.", Toast.LENGTH_SHORT).show();
        });
        binding.twitter.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Will be available soon.", Toast.LENGTH_SHORT).show();
        });
        binding.insta.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Will be available soon.", Toast.LENGTH_SHORT).show();
        });
        submitComplaint.setOnClickListener(view1 -> {
            message = messageBox.getText().toString();
            if (message.isEmpty()){
                messageBox.requestFocus();
                messageBox.setError("Message please!");
            }else{
                Map<String,Object> serverTimeStamp=new HashMap<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
                Date date = new Date();
                String ticket=dateFormat.format(date);
                ticket=ticket.replace("/","").replace(":","").replace(" ","");
                String tId="MI11C"+ticket;
                complaintId = tId;
                complaintDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                userName = Common.profileName;
                userEmail = Common.profileEmail;
                userMobile = Common.profileMobile;
                userId = uid;
                status = "Pending";
                ComplaintModel complaintModel = new ComplaintModel(complaintId, complaintDate, message, userName, userEmail, userMobile,userId,status);
                databaseReference.child("UsersRaisedComplaint").child(complaintId).setValue(complaintModel);
                databaseReference1.child("INDIA11Users").child(uid).child("RaisedComplaint").child(complaintId).setValue(complaintModel)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Complaint raised successfully.", Toast.LENGTH_SHORT).show();
                            String subject = "Raised complaint "+complaintId+" status.";
                            String body="Hello, "+Common.profileName+"\nYour complaint has been raised successfully against complaint" +
                                    " id: "+complaintId+"\nTime: "+complaintDate+"\nDescription: "+message+"\nThanks for reaching out to us " +
                                    " your query will get resolved shortly. Till then Stay safe and enjoy playing on MYINDIA11."+"\n" +
                                    "\nRegards,"+"\nMYINDIA11 Team";
                            JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),Common.profileEmail,subject,body);
                            javaMailAPI.execute();
                            complaint.dismiss();
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
                fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(tag).commit();

            }
            handler.post(() -> {
                activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
            });
        });
    }
}