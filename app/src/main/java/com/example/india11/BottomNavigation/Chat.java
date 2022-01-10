package com.example.india11.BottomNavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.india11.Adapters.GroupChatAdapter;
import com.example.india11.Adapters.MessageAdapter;
import com.example.india11.ChatGroup.GroupDetails;
import com.example.india11.Common;
import com.example.india11.Model.ChatModel;
import com.example.india11.Model.GroupChatModel;
import com.example.india11.R;
import com.example.india11.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.india11.Common.activeFragment;

public class Chat extends Fragment {
    private FragmentChatBinding binding;
    DatabaseReference reference,databaseReference,databaseReference1;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String msg,uid;
    String sender, time, message;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<GroupChatModel> groupChatModelList = new ArrayList<>();
    GroupChatAdapter groupChatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view12 -> requireActivity().onBackPressed());
        binding.name.setText(Common.groupName);
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        Glide.with(getContext()).load(Common.groupPic).into(binding.pic);
        reference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        binding.name.setOnClickListener(view1 -> {
            Fragment detail = new GroupDetails();
            loadFragment(detail,"GroupDetails");
        });
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                groupChatAdapter.notifyDataSetChanged();
                binding.swiperefresh.setRefreshing(false);

            }
        });
        databaseReference1.child("ChatGroups").child(Common.groupId).child("GroupMembers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)){
                    binding.recyclerviewMess.setVisibility(View.VISIBLE);
                    binding.note.setVisibility(View.GONE);
                }else {
                    binding.chatBoxLay.setVisibility(View.GONE);
                    binding.recyclerviewMess.setVisibility(View.GONE);
                    binding.note.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.imgSend.setOnClickListener(view1 -> {
            msg = binding.editChat.getText().toString();
            if (msg.isEmpty()){
                Toast.makeText(getContext(), "Write something!", Toast.LENGTH_SHORT).show();
            }else {
                sender = Common.profileName;
                time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                message = msg;
                GroupChatModel groupChatModel = new GroupChatModel(sender, time, message);
                databaseReference.child("ChatGroups").child(Common.groupId).child("Messages").push().setValue(groupChatModel);
            }
        });
        binding.recyclerviewMess.setHasFixedSize(true);
        binding.recyclerviewMess.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        reference.child("ChatGroups").child(Common.groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    groupChatModelList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        GroupChatModel groupChatModel = dataSnapshot.getValue(GroupChatModel.class);
                        groupChatModelList.add(groupChatModel);
                    }
                    groupChatAdapter = new GroupChatAdapter(getContext(),groupChatModelList);
                    binding.recyclerviewMess.setAdapter(groupChatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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