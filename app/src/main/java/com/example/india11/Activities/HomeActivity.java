package com.example.india11.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.BottomNavigation.MyMatches;
import com.example.india11.BottomNavigation.MyTeams;
import com.example.india11.BottomNavigation.Settings;
import com.example.india11.BottomNavigation.Winners;
import com.example.india11.Common;
import com.example.india11.Dashboard.Dashboard;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.Model.AllTransactionModel;
import com.example.india11.Model.TotalBalanceModel;
import com.example.india11.Model.WalletModel;
import com.example.india11.R;
import com.example.india11.databinding.ActivityHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.razorpay.PaymentResultListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class HomeActivity extends AppCompatActivity implements PaymentResultListener {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    final FragmentManager fm = getSupportFragmentManager();
    private ActivityHomeBinding binding;
    public long totalBalance;
    String UID;
    Dialog locationDialog;
    TextView reason;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String amountToWithdraw, referenceId, dateOfRequest,transactionType;
    FusedLocationProviderClient fusedLocationProviderClient;
    BottomSheetDialog successDetailsDialog,failedDetailsDialog;
    View successView,failedView;
    TextView userName1, successAmount, successDate, successTime, successTid, successRfid, successSEmail;
    ImageView successBar;
    LinearLayout successShare;
    TextView userName2, failedAmount, failedDate, failedTime, failedTid, failedRfid, failedSEmail;
    ImageView failedBar;
    LinearLayout failedShare, success,failed;

    /*<color name="colorStart">#45D99D</color>
    <color name="colorEnd">#00D680</color>*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        locationDialog = new Dialog(this);
        locationDialog.setContentView(R.layout.location_dialog);
        locationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        locationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        locationDialog.getWindow().setGravity(Gravity.BOTTOM);
        reason = locationDialog.findViewById(R.id.reason);
        successView = LayoutInflater.from(this)
                .inflate(R.layout.payment_success_layout, (LinearLayout)view.findViewById(R.id.success_view));
        failedView = LayoutInflater.from(this)
                .inflate(R.layout.payment_cancelled_layout, (LinearLayout)view.findViewById(R.id.failed_view));
        successDetailsDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        successDetailsDialog.setCancelable(true);
        successDetailsDialog.setCanceledOnTouchOutside(false);
        successDetailsDialog.setContentView(successView);
        successDetailsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        userName1 = successView.findViewById(R.id.name);
        successAmount = successView.findViewById(R.id.amount);
        successDate = successView.findViewById(R.id.date);
        successTime = successView.findViewById(R.id.time);
        successTid = successView.findViewById(R.id.tid);
        successRfid = successView.findViewById(R.id.ref_id);
        successSEmail = successView.findViewById(R.id.support_email);
        successShare = successView.findViewById(R.id.share);
        successBar = successView.findViewById(R.id.barcode);
        success = successView.findViewById(R.id.success_view);

        failedDetailsDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        failedDetailsDialog.setCancelable(true);
        failedDetailsDialog.setCanceledOnTouchOutside(false);
        failedDetailsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        failedDetailsDialog.setContentView(failedView);
        userName2 = failedView.findViewById(R.id.name1);
        failedAmount = failedView.findViewById(R.id.amount1);
        failedDate = failedView.findViewById(R.id.date1);
        failedTime = failedView.findViewById(R.id.time1);
        failedTid = failedView.findViewById(R.id.tid1);
        failedRfid = failedView.findViewById(R.id.ref_id1);
        failedSEmail = failedView.findViewById(R.id.support_email1);
        failedShare = failedView.findViewById(R.id.share1);
        failedBar = failedView.findViewById(R.id.barcode1);
        failed = failedView.findViewById(R.id.failed_view);


        successSEmail.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@myindia11.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(intent, ""));
        });
        failedSEmail.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@myindia11.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(intent, ""));
        });

        successShare.setOnClickListener(view1 -> {
            shareImage(getScreenShot(success));
            Toast.makeText(this, "Share..", Toast.LENGTH_SHORT).show();
        });
        failedShare.setOnClickListener(view1 -> {
            shareImage(getScreenShot1(failed));
            Toast.makeText(this, "Share..", Toast.LENGTH_SHORT).show();
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getUserLocation();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Fragment dashboard = new Dashboard();
        loadFragment(dashboard,"Dashboard");
        setContentView(view);
    }
    public static Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap= Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public static Bitmap getScreenShot1(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap= Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void shareImage(Bitmap bitmap){
        Random random = new Random();
        int id = random.nextInt(999999999)+100000000;
        String rfid = "MI11"+String.valueOf(id);

        try {

            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/"+rfid+".jpeg"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, rfid+".jpeg");
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.india11", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,Common.refId);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Share through..."));
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void loadFragment(Fragment fragment, String tag)
    {
        executorService.execute(() ->{
            if (fragment!=null)
            {
                fm.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commitAllowingStateLoss();
            }
            handler.post(() ->{
                activeFragment=fm.findFragmentById(R.id.container);
            });
        });
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        String state = addresses.get(0).getAdminArea();
                        if (state.equals("Telangana")){
                            reason.setText("As per the order of State Government of Telangana we do not offer our services in Telangana State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);
                        }else if (state.equals("Karnataka")){
                            reason.setText("As per the order of State Government of Karnataka we do not offer our services in Karnataka State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);
                        }else if (state.equals("Andhra Pradesh")){
                            reason.setText("As per the order of State Government of Andhra Pradesh we do not offer our services in Andhra Pradesh State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);
                        }else if (state.equals("Assam")){
                            reason.setText("As per the order of State Government of Assam we do not offer our services in Assam State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);

                        }else if (state.equals("Odisha")){
                            reason.setText("As per the order of State Government of Odisha we do not offer our services in Odisha State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);

                        }else if (state.equals("Nagaland")){
                            reason.setText("As per the order of State Government of Nagaland we do not offer our services in Nagaland State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);

                        }else if (state.equals("Sikkim")){
                            reason.setText("As per the order of State Government of Sikkim we do not offer our services in Sikkim State. In non restricted states we offer our services as usual. For any help and support you can contact us by mailing at: support@myindia11.in. We are always happy to help you.");
                            locationDialog.show();
                            locationDialog.setCanceledOnTouchOutside(false);
                            locationDialog.setCancelable(false);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "Enable location permission for MYINDIA11 in setting.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentSuccess(String s) {
        totalBalance = Common.previousTotalMoney + Common.addedMoney;
        //Toast.makeText(this, String.valueOf(totalBalance), Toast.LENGTH_SHORT).show();
        TotalBalanceModel totalBalanceModel = new TotalBalanceModel(totalBalance);
        databaseReference.child("INDIA11Users").child(UID).child("TotalBalance").setValue(totalBalanceModel).addOnSuccessListener(aVoid -> {
        });
        successDetailsDialog.show();
        userName1.setText("Hi,"+Common.profileName);
        successTid.setText("Transaction Id: "+s);
        successRfid.setText(s);
        successAmount.setText(" "+convertValueInIndianCurrency(Common.addedMoney));
        amountToWithdraw = String.valueOf(Common.addedMoney);
        transactionType = "Added to Wallet";
        referenceId = s;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String d = sdf.format(calendar.getTime());
        successDate.setText("Date: "+d);
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String t = sdf1.format(calendar1.getTime());
        successTime.setText("Time: "+t);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_128,300,50);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            successBar.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        dateOfRequest = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        WalletModel walletModel = new WalletModel(amountToWithdraw, referenceId, dateOfRequest,transactionType);
        DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference();
        String tid = referenceId;
        String userName = Common.profileName;
        String userMobile = Common.profileMobile;
        String userEmail = Common.profileEmail;
        String amount = amountToWithdraw;
        String time = dateOfRequest;
        AllTransactionModel allTransactionModel = new AllTransactionModel(tid, userName, userMobile, userEmail, amount, time);
        databaseReference5.child("AllTransaction").child(tid).setValue(allTransactionModel);
        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference4.child("INDIA11Users").child(UID).child("WalletHistory").child(referenceId).setValue(walletModel)
                .addOnSuccessListener(aVoid -> {
                    String subject = "Amount "+convertValueInIndianCurrency(Long.valueOf(amountToWithdraw))+" added to wallet.";
                    String body="Hello, "+Common.profileName+"\nYour amount has been successfully added to wallet." +"\nTransaction"+
                            " id: "+s+"\nTime: "+dateOfRequest+"\nThanks for using MYINDIA11" +
                            " Total wallet balance is: "+convertValueInIndianCurrency(totalBalance)+"\n" +
                            "\nRegards,"+"\nMYINDIA11 Team";
                    JavaMailAPI javaMailAPI = new JavaMailAPI(this,Common.profileEmail,subject,body);
                    javaMailAPI.execute();
                });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentError(int i, String s) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        failedDetailsDialog.show();
        userName2.setText("Hi,"+Common.profileName);
        failedTid.setText("Reason: "+s);
        failedAmount.setText("Amount: "+convertValueInIndianCurrency(Common.addedMoney));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String d = sdf.format(calendar.getTime());
        failedDate.setText("Date: "+d);
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String t = sdf1.format(calendar1.getTime());
        failedTime.setText("Time: "+t);
        Random random = new Random();
        int id = random.nextInt(999999999)+100000000;
        String rfid = "MI11"+String.valueOf(id);
        failedRfid.setText(rfid);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(rfid, BarcodeFormat.CODE_128,300,50);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            failedBar.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
    public String convertValueInIndianCurrency(Long amount)
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}