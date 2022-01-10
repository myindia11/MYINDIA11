package com.example.india11.ChatGroup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import com.example.india11.Model.GroupModel;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentCreateGroupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;
import static com.example.india11.Common.activeFragment;

public class CreateGroup extends Fragment {
    private FragmentCreateGroupBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    FirebaseAuth firebaseAuth;
    String uid;
    String groupId, groupName, groupDescription, groupPic,groupCreatedTime, groupAdmin,groupStatus;
    Integer groupSize;
    Uri filePath=null;
    private final int PICK_IMAGE_REQUEST = 22;
    String profileImageUrl;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String connectionName, connectionMobile, connectionPic, connectionUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateGroupBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.gid.setText(Common.groupId);
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        binding.create.setOnClickListener(view1 -> {
            groupName = binding.gName.getText().toString();
            groupDescription = binding.description.getText().toString();
            if (groupName.isEmpty()){
                binding.gName.requestFocus();
                binding.gName.setError("Group Name please!");
            }else if (groupDescription.isEmpty()){
                binding.description.requestFocus();
                binding.description.setError("Write something..");
            }else {
                groupSize = 1;
                groupCreatedTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                groupId = Common.groupId;
                if (filePath == null){
                    groupPic = "https://firebasestorage.googleapis.com/v0/b/india11fantasy-bb723.appspot.com/o/grp.jpg?alt=media&token=2308f6c5-f20a-477f-ab93-f33812762824";
                }else {
                    groupPic = profileImageUrl;
                }
                groupAdmin = Common.profileName;
                groupStatus = "NO";
                Common.groupName = groupName;
                Common.groupPic = groupPic;
                Common.groupCreation = groupCreatedTime;
                GroupModel groupModel = new GroupModel(groupId, groupName, groupDescription, groupPic,groupCreatedTime, groupAdmin,groupStatus,groupSize);
                connectionMobile = Common.profileMobile;
                connectionName = Common.profileName;
                connectionUid = uid;
                connectionPic = Common.userProfilePic;
                ReferralConnectionModel referralConnectionModel = new ReferralConnectionModel(connectionName, connectionMobile, connectionPic, connectionUid);
                databaseReference.child("ChatGroups").child(groupId).setValue(groupModel);
                databaseReference1.child("ChatGroups").child(groupId).child("GroupMembers").child(connectionUid).setValue(referralConnectionModel);
                databaseReference2.child("INDIA11Users").child(uid).child("Groups").child(groupId).setValue(groupModel);
                Fragment fragment = new SelectGroupMembers();
                loadFragment(fragment,"SelectGroupMembers");
            }
        });
        binding.groupPic.setOnClickListener(view1 -> {
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
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            Glide.with(getContext()).load(filePath).into(binding.groupPic);
            uploadImageOnFirebase(filePath);
        }
    }
    private void uploadImageOnFirebase(Uri filePath) {
        if (filePath != null) {
            Random random = new Random();
            int i = random.nextInt(99999999)+10000000;
            String id = String.valueOf(i);
            binding.loading.setVisibility(View.VISIBLE);
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref=storageReference.child("images/" + id);

            ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                profileImageUrl= Objects.requireNonNull(task.getResult()).toString();
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