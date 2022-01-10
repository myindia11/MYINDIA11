package com.example.india11.BottomNavigation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;
import static com.example.india11.Common.activeFragment;

public class EditProfile extends Fragment {
    private FragmentEditProfileBinding binding;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    FirebaseAuth firebaseAuth;
    String uid;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String uName, uNick, imageUrl;
    Uri filePath=null;
    private final int PICK_IMAGE_REQUEST = 22;
    String profileImageUrl;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Dialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.emailBox.setText(Common.profileEmail);
        binding.mobileBox.setText(Common.profileMobile);
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference.child("INDIA11Users").child(uid).child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userNameID = snapshot.child("userReferralCode").getValue().toString();
                String profileName = snapshot.child("userName").getValue().toString();
                binding.nameBox.setText(profileName);
                String profileEmail = snapshot.child("userEmail").getValue().toString();
                String profileMobile = snapshot.child("userMobile").getValue().toString();
                String nick = snapshot.child("nickName").getValue().toString();
                int level = Integer.parseInt(snapshot.child("level").getValue().toString());
                String joined = snapshot.child("joinedOn").getValue().toString();
                String profile = snapshot.child("profilePic").getValue().toString();
                Glide.with(getContext()).load(profile).into(binding.profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.changeName.setOnClickListener(view1 -> {
            binding.loading.setVisibility(View.VISIBLE);
            uName = binding.nameBox.getText().toString();
            databaseReference.child("INDIA11Users").child(uid).child("UserInfo").child("userName").setValue(uName).addOnSuccessListener(aVoid -> {
                binding.loading.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
            });
        });
        binding.changeNick.setOnClickListener(view1 -> {
            uNick = binding.usernameBox.getText().toString();
            if (uNick.isEmpty()){
                binding.usernameBox.requestFocus();
                binding.usernameBox.setError("Username!");
            }else {
                binding.loading.setVisibility(View.VISIBLE);
                databaseReference1.child("UserNickNames").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(uNick)){
                            binding.usernameBox.requestFocus();
                            binding.usernameBox.setError("Already taken!");
                            binding.loading.setVisibility(View.GONE);
                        }else {
                            loading.show();
                            databaseReference2.child("UserNickNames").child(uNick).setValue(uNick);
                            databaseReference.child("INDIA11Users").child(uid).child("UserInfo").child("nickName").setValue(uNick);
                            binding.loading.setVisibility(View.GONE);
                            loading.dismiss();
                            Toast.makeText(getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        binding.changeProfile.setOnClickListener(view1 -> {
            selectImage();
        });
        return view;
    }
    public void selectImage(){

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."),PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            Glide.with(getContext()).load(filePath).into(binding.newPic);
            uploadImageOnFirebase(filePath);
        }
    }

    private void uploadImageOnFirebase(Uri filePath) {
        if (filePath != null) {
            loading.show();
            binding.loading.setVisibility(View.VISIBLE);
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref=storageReference.child("images/" + UID);

            ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                profileImageUrl= Objects.requireNonNull(task.getResult()).toString();
                databaseReference1.child("INDIA11Users").child(uid).child("UserInfo").child("profilePic").setValue(profileImageUrl);
                binding.loading.setVisibility(View.GONE);
                loading.dismiss();
                Toast.makeText(getContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
            })).addOnFailureListener(e -> {
                binding.loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(
                    taskSnapshot -> {
                        binding.loading.setVisibility(View.GONE);
                    });

        }
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