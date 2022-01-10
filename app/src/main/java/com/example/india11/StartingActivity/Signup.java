package com.example.india11.StartingActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.BottomNavigation.TandC;
import com.example.india11.Common;
import com.example.india11.EmailClients.JavaMailAPI;
import com.example.india11.Model.AccountDetailsModel;
import com.example.india11.Model.AllUserDataModel;
import com.example.india11.Model.BonusBalanceModel;
import com.example.india11.Model.NotificationModel;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.Model.TotalBalanceModel;
import com.example.india11.Model.UserEmailModel;
import com.example.india11.Model.UserInfoModel;
import com.example.india11.Model.UserStatsModel;
import com.example.india11.Model.WinningBalanceModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentSignupBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class Signup extends Fragment {
    private FragmentSignupBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String name, email, password, mobile, enteredReferral, userReferral,UID;
    String holderName, bankName, accountNumber, ifsCode;
    FirebaseAuth firebaseAuth;
    long totalBalance;
    long winningBalance;
    long bonusBalance,bBal,creditBonus;
    DatabaseReference databaseReference12,databaseReference10,databaseReference11,databaseReference9,databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5,databaseReference6,databaseReference7,databaseReference8;
    String userName, userMobile, userEmail, enteredReferralCode, userReferralCode, userPassword, uid,joinedOn,profilePic,nickName;
    private static final int RC_SIGN_IN = 234;
    CallbackManager callbackManager;
    Integer level;
    Integer played, won;
    Double invested, earned;
    String connectionName, connectionMobile, connectionPic, connectionUid;
    String state = "false";
    Dialog toastDialog;
    TextView toastMessage;
    String userApic, userAfid, userAname, userAemail;

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        databaseReference6 = FirebaseDatabase.getInstance().getReference();
        databaseReference7 = FirebaseDatabase.getInstance().getReference();
        databaseReference8 = FirebaseDatabase.getInstance().getReference();
        databaseReference9 = FirebaseDatabase.getInstance().getReference();
        databaseReference10 = FirebaseDatabase.getInstance().getReference();
        databaseReference11 = FirebaseDatabase.getInstance().getReference();
        databaseReference12 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        binding.login.setOnClickListener(view1 -> {
            Fragment login = new Login();
            loadFragment(login,"Login");
        });
        binding.tcRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    state = "true";
                }else {
                    state = "false";
                }
            }
        });
        binding.register.setOnClickListener(this::registerUser);
        binding.terms.setOnClickListener(view1 -> {
            Fragment tc = new TandC();
            loadFragment(tc,"TandC");
        });

        return view;
    }

    private void registerUser(View view) {
        email = binding.email.getText().toString();
        password = binding.password.getText().toString();
        mobile = binding.mobile.getText().toString();
        name = binding.name.getText().toString();
        enteredReferral = binding.referral.getText().toString();
        nickName = binding.nickName.getText().toString();
        if (name.isEmpty()){
           binding.name.setError("Enter Name");
           binding.name.requestFocus();
        }else if (nickName.isEmpty()){
            binding.nickName.setError("Enter username");
            binding.nickName.requestFocus();
        }
        else if (mobile.isEmpty()){
            binding.mobile.setError("Enter Mobile Number");
            binding.mobile.requestFocus();
        }else if (email.isEmpty()){
            binding.email.setError("Enter Email Id");
            binding.email.requestFocus();
        }else if (password.isEmpty()){
            binding.password.setError("Enter Password");
            binding.password.requestFocus();
        }else if (password.length() < 6){
            toastMessage.setText("Password length must be greater than 6 digits!");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
            binding.password.requestFocus();
        }else if (state.equals("false")){
            toastMessage.setText("Please select Terms & Condition.");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 1000);
        }else {
            Random random = new Random();
            int referralCode = random.nextInt(1000000);
            userReferral = "MI11"+referralCode;
            if (enteredReferral.equals("")){
                enteredReferral = "MYI11NEWUSER";
            }else if(!enteredReferral.equals("")){
                enteredReferral = binding.referral.getText().toString();
            }
            signUpUser(userReferral,name,email,password,mobile,enteredReferral,nickName);
        }
    }

    private void signUpUser(String userReferral, String name, String email, String password, String mobile, String enteredReferral, String nickName) {
        binding.loading.setVisibility(View.VISIBLE);
        binding.register.setVisibility(View.INVISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String userEmailId = email;
                databaseReference6.child("UsersEmail").child(mobile).setValue(userEmailId);
                databaseReference7.child("UserNickNames").child(nickName).setValue(nickName);

                UID = firebaseAuth.getCurrentUser().getUid();

                databaseReference9.child("UserReferralId").child(userReferral).setValue(UID);
                userName = name;
                userMobile = mobile;
                userEmail = email;
                enteredReferralCode = enteredReferral;
                userReferralCode = userReferral;
                userPassword = "NA";
                uid = UID;
                holderName = "None";
                bankName = "None";
                accountNumber = "None";
                ifsCode = "None";
                totalBalance = 0;
                winningBalance = 0;

                level = 1;
                profilePic = "https://firebasestorage.googleapis.com/v0/b/india11fantasy-bb723.appspot.com/o/myindia_11_half_logo.png?alt=media&token=eb4501b3-2997-4051-b382-44a4fd7e96dd";
                joinedOn = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                UserInfoModel userInfoModel = new UserInfoModel(userName,userMobile,userEmail,enteredReferralCode,userReferralCode,userPassword,uid,joinedOn,profilePic,nickName,level);
                databaseReference.child("INDIA11Users").child(UID).child("UserInfo").setValue(userInfoModel).addOnSuccessListener(aVoid -> {
                });
                AccountDetailsModel accountDetailsModel = new AccountDetailsModel(holderName, bankName, accountNumber, ifsCode);
                databaseReference2.child("INDIA11Users").child(UID).child("UserBankDetails").setValue(accountDetailsModel).addOnSuccessListener(aVoid -> {
                });
                TotalBalanceModel totalBalanceModel = new TotalBalanceModel(totalBalance);
                databaseReference3.child("INDIA11Users").child(UID).child("TotalBalance").setValue(totalBalanceModel).addOnSuccessListener(aVoid -> {
                });

                DatabaseReference databaseReference21 = FirebaseDatabase.getInstance().getReference();
                databaseReference21.child("INDIA11Users").child(UID).child("MiPin").child("miPin").setValue(Common.miPin);

                DatabaseReference databaseReference22 = FirebaseDatabase.getInstance().getReference();
                userAemail = email;
                userAfid = UID;
                userAname = name;
                userApic = profilePic;
                AllUserDataModel allUserDataModel = new AllUserDataModel(userApic, userAfid, userAname, userAemail);
                databaseReference22.child("AllMyIndiaUsers").child(userAfid).setValue(allUserDataModel);

                WinningBalanceModel winningBalanceModel = new WinningBalanceModel(winningBalance);
                databaseReference5.child("INDIA11Users").child(UID).child("WinningBalance").setValue(winningBalanceModel).addOnSuccessListener(aVoid -> {
                });
                played = 0;
                won = 0;
                invested = 0.0;
                earned = 0.0;
                UserStatsModel userStatsModel = new UserStatsModel(played, won,invested, earned);
                databaseReference8.child("INDIA11Users").child(UID).child("UserStats").setValue(userStatsModel).addOnSuccessListener(aVoid -> {});
                databaseReference10.child("UserReferralId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(enteredReferral)){
                            String id = snapshot.child(enteredReferral).getValue().toString();
                            bonusBalance = 125;
                            BonusBalanceModel bonusBalanceModel = new BonusBalanceModel(bonusBalance);
                            databaseReference4.child("INDIA11Users").child(UID).child("BonusBalance").setValue(bonusBalanceModel).addOnSuccessListener(aVoid -> {
                            });
                            databaseReference11.child("INDIA11Users").child(id).child("BonusBalance").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        bBal = Long.parseLong(snapshot.child("bonusBalance").getValue().toString());
                                        creditBonus = bBal+25;
                                        binding.bonus.setText(String.valueOf(creditBonus));
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference11.child("INDIA11Users").child(id).child("UserInfo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        String refereName = snapshot.child("userName").getValue().toString();
                                        String refereMobile = snapshot.child("userMobile").getValue().toString();
                                        String referePic= snapshot.child("profilePic").getValue().toString();
                                        bonusBalance = Long.parseLong(binding.bonus.getText().toString());
                                        BonusBalanceModel bonusBalanceModel = new BonusBalanceModel(bonusBalance);
                                        databaseReference12.child("INDIA11Users").child(id).child("BonusBalance").setValue(bonusBalanceModel).addOnSuccessListener(aVoid -> {
                                        });
                                        String heading = userName+" is a new connection of yours.";
                                        String body = "Hi, "+userName+" has joined MYINDIA11 using your referral link. The bonus of Rs.25" +
                                                " has been credited to your wallet. Keep sharing the MYINDIA11 and Play more, Win more.";
                                        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                                        NotificationModel notificationModel = new NotificationModel(heading, body, date);
                                        databaseReference6.child("INDIA11Users").child(id).child("Notifications").push().setValue(notificationModel);
                                        connectionName = userName;
                                        connectionMobile = userMobile;
                                        connectionUid = UID;
                                        connectionPic = profilePic;
                                        ReferralConnectionModel referralConnectionModel = new ReferralConnectionModel(connectionName, connectionMobile, connectionPic, connectionUid);
                                        databaseReference7.child("INDIA11Users").child(id).child("ReferralConnections").child(connectionMobile)
                                                .setValue(referralConnectionModel);
                                        connectionName = refereName;
                                        connectionMobile = refereMobile;
                                        connectionUid = id;
                                        connectionPic = referePic;
                                        referralConnectionModel = new ReferralConnectionModel(connectionName, connectionMobile, connectionPic, connectionUid);
                                        databaseReference.child("INDIA11Users").child(UID).child("ReferralConnections").child(connectionMobile)
                                                .setValue(referralConnectionModel);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            bonusBalance = 100;
                            BonusBalanceModel bonusBalanceModel = new BonusBalanceModel(bonusBalance);
                            databaseReference4.child("INDIA11Users").child(UID).child("BonusBalance").setValue(bonusBalanceModel).addOnSuccessListener(aVoid -> {
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                binding.loading.setVisibility(View.INVISIBLE);
                binding.register.setVisibility(View.VISIBLE);
                toastMessage.setText("Account registered successfully. Please login..");
                toastDialog.show();
                final Timer timer3 = new Timer();
                timer3.schedule(new TimerTask() {
                    public void run() {
                        toastDialog.dismiss();
                        timer3.cancel(); //this will cancel the timer of the system
                    }
                }, 1000);
                //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                Fragment login = new Login();
                loadFragment(login,"Login");
            }else {
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                binding.loading.setVisibility(View.INVISIBLE);
                binding.register.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in dashboard_progress, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        assert user != null;
                        String UID = user.getUid();
                        String uid = UID;
                        String userMail = email;
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        Toast.makeText(getContext(), "Signed in..", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void signInGmail() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        final boolean[] flag = new boolean[1];
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.fetchSignInMethodsForEmail(Objects.requireNonNull(acct.getEmail())).addOnCompleteListener(task -> {
            // email not existed
            // email existed
            flag[0]= Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).size() != 0;
        });

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                if(!flag[0])
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    String email=user.getEmail();
                    String userName=user.getDisplayName();
                    String UID=user.getUid();
                }
                startActivity(new Intent(getActivity(), HomeActivity.class));
                Toast.makeText(getContext(), "Signed In", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
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