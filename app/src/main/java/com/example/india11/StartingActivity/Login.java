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
import android.widget.TextView;
import android.widget.Toast;

import com.example.india11.Activities.HomeActivity;
import com.example.india11.Activities.MainActivity;
import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentLoginBinding;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.india11.Common.activeFragment;

public class Login extends Fragment {

    private FragmentLoginBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    FirebaseAuth firebaseAuth;
    String email, password,mobile;
    private static final int RC_SIGN_IN = 234;
    CallbackManager callbackManager;
    String emailRegEx;
    DatabaseReference databaseReference;
    Dialog toastDialog;
    TextView toastMessage;

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    String pattern1 = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        toastDialog = new Dialog(getContext());
        toastDialog.setContentView(R.layout.toast_layout);
        toastDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toastDialog.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        toastDialog.getWindow().setGravity(Gravity.BOTTOM);
        toastMessage = toastDialog.findViewById(R.id.toast_message);
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        binding.regBtn.setOnClickListener(view1 -> {
            Fragment register = new Signup();
            loadFragment(register,"Signup");
        });
        binding.login.setOnClickListener(this::loginUser);
        binding.forgotPassword.setOnClickListener(view1 -> {
            Fragment forgot = new ResetPassword();
            loadFragment(forgot,"ResetPassword");
        });
        return view;
    }

    private void loginUser(View view) {
        email = binding.email.getText().toString();
        Pattern pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);
        password = binding.password.getText().toString();
        Pattern r = Pattern.compile(pattern1);
        m = r.matcher(email);
        if (email.isEmpty()){
            binding.email.setError("Enter registered email id/Mobile");
            binding.email.requestFocus();
        }else if (password.isEmpty()){
            binding.password.setError("Enter Password");
            binding.password.requestFocus();
        }else if (matcher.find()){
            authenticateUser(email,password);
        }else if (m.find() && email.length() == 10){
            binding.login.setVisibility(View.INVISIBLE);
            binding.loading.setVisibility(View.VISIBLE);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("UsersEmail").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(email)){
                        email = snapshot.child(email).getValue().toString();
                        authenticateUser(email,password);
                    }else {
                        toastMessage.setText("User does not exists. Please check");
                        toastDialog.show();
                        final Timer timer3 = new Timer();
                        timer3.schedule(new TimerTask() {
                            public void run() {
                                toastDialog.dismiss();
                                timer3.cancel(); //this will cancel the timer of the system
                            }
                        }, 2000);
                        binding.login.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if (!m.find() || !matcher.find()){
            toastMessage.setText("Please check email or mobile.");
            toastDialog.show();
            final Timer timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                public void run() {
                    toastDialog.dismiss();
                    timer3.cancel(); //this will cancel the timer of the system
                }
            }, 2000);
        }
    }

    private void authenticateUser(String email, String password) {
        binding.login.setVisibility(View.INVISIBLE);
        binding.loading.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                binding.login.setVisibility(View.VISIBLE);
                binding.loading.setVisibility(View.INVISIBLE);
            }else {
                if (firebaseAuth.getCurrentUser() != null){
                    binding.login.setVisibility(View.VISIBLE);
                    binding.loading.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getActivity(),HomeActivity.class));
                    toastMessage.setText("Logged in successfully.");
                    toastDialog.show();
                    final Timer timer3 = new Timer();
                    timer3.schedule(new TimerTask() {
                        public void run() {
                            toastDialog.dismiss();
                            timer3.cancel(); //this will cancel the timer of the system
                        }
                    }, 1000);
                }
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
                        startActivity(new Intent(getActivity(),HomeActivity.class));
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
                startActivity(new Intent(getActivity(),HomeActivity.class));
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