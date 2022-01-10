package com.example.india11.Dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.india11.Adapters.AvailableContestsAdapter;
import com.example.india11.Adapters.ImageSliderAdapter;
import com.example.india11.Adapters.JoinedMatchAdapter;
import com.example.india11.Adapters.JoinedSubContestsAdapter;
import com.example.india11.Adapters.MatchListAdapter;
import com.example.india11.BottomNavigation.MyConnections;
import com.example.india11.BottomNavigation.MyMatches;
import com.example.india11.BottomNavigation.MyTeams;
import com.example.india11.BottomNavigation.Settings;
import com.example.india11.BottomNavigation.Winners;
import com.example.india11.Common;
import com.example.india11.MenuBarLayouts.Notification;
import com.example.india11.MenuBarLayouts.PointsTable;
import com.example.india11.MenuBarLayouts.Wallet;
import com.example.india11.Model.AvailableContestsModel;
import com.example.india11.Model.JoinedSubContestsModel;
import com.example.india11.Model.MatchListModel;
import com.example.india11.Model.SliderModel;
import com.example.india11.MoneyRelatedLayouts.WalletHistory;
import com.example.india11.R;
import com.example.india11.databinding.FragmentDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import me.ibrahimsn.lib.OnItemSelectedListener;

import static com.example.india11.Common.activeFragment;


public class
Dashboard extends Fragment {
    private FragmentDashboardBinding binding;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    DatabaseReference databaseReference,databaseReferenceProfile;
    public List<AvailableContestsModel> availableContestsModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    String UID;
    private ArrayList<SliderModel> sliderModelArrayList = new ArrayList<>();
    private ImageSliderAdapter imageSliderAdapter;
    public List<JoinedSubContestsModel> joinedSubContestsModelList = new ArrayList<>();
    public List<AvailableContestsModel> availableContestsModel = new ArrayList<>();
    public List<MatchListModel> modelList = new ArrayList<>();
    private String url = "https://cricapi.com/api/matches?apikey=R0Fi2W5ZWIVAYFJVHp3ICGB4WKs2";
    private AvailableContestsAdapter availableContestsAdapter;
    private JoinedMatchAdapter joinedSubContestsAdapter;
    DatabaseReference databaseReferenceV;
    Dialog update,exit,loading;
    LinearLayout updateBtn;
    TextView ok,cancel,newThing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container,false);
        View view = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        exit = new Dialog(getContext());
        exit.setContentView(R.layout.exit_app_layout);
        exit.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exit.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        exit.getWindow().setGravity(Gravity.CENTER);
        ok = exit.findViewById(R.id.exit);
        cancel = exit.findViewById(R.id.cancel);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyCode == KeyEvent.KEYCODE_BACK){
                        exit.show();
                        return true;
                    }
                }
                return false;
            }
        });
        ok.setOnClickListener(view1 -> {
            System.exit(0);
        });
        cancel.setOnClickListener(view1 -> {
            exit.dismiss();
        });
        //dialog=new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        //dialog.show();
        update = new Dialog(getContext());
        update.setContentView(R.layout.update_app_layout);
        update.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        update.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;
        update.getWindow().setGravity(Gravity.CENTER);
        updateBtn = update.findViewById(R.id.updateBtn);
        newThing = update.findViewById(R.id.newThings);
        loading = new Dialog(getContext());
        loading.setContentView(R.layout.loading_screen_layout);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.getWindow().getAttributes().windowAnimations = R.style.confirmDialog;

        loading.show();
        /*String value = "Something";
        String last = null;
        if (value != null && value.length() >= 2){
            last = value.substring(value.length() - 2);
            Toast.makeText(getContext(), last, Toast.LENGTH_SHORT).show();
        }*/
        try {
            String versionName = getActivity().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(),0).versionName;
            Common.versionName = versionName;
            databaseReferenceV = FirebaseDatabase.getInstance().getReference();
            databaseReferenceV.child("AppVersion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String vName = snapshot.child("version").getValue().toString();
                        String newThings = snapshot.child("versionDescription").getValue().toString();
                        if(!versionName.equals(vName)){
                            newThing.setText(newThings);
                            update.show();
                            updateBtn.setOnClickListener(view1 -> {
                                DatabaseReference databaseReference23 = FirebaseDatabase.getInstance().getReference();
                                databaseReference23.child("AppLink").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String link = snapshot.child("link").getValue().toString();
                                            update.dismiss();
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            });
                        }else {
                            update.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.notification.setOnClickListener(view1 -> {
            Fragment notification = new Notification();
            loadFragment(notification,"Notification");
        });
        binding.wallet.setOnClickListener(view1 -> {
            Fragment wallet = new Wallet();
            loadFragment(wallet,"Wallet");
        });
        binding.pointsTable.setOnClickListener(view1 -> {
            Fragment pointsTable = new PointsTable();
            loadFragment(pointsTable,"PointsTable");
        });
        loadAvailableContests();
        loadProfileDetails();
        loadTopBanners();
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                availableContestsAdapter.notifyDataSetChanged();
                //joinedSubContestsAdapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        binding.home.setOnClickListener(view1 -> {
            Fragment home = new Dashboard();
            loadFragment(home,"Dashboard");
        });
        binding.joined.setOnClickListener(view1 -> {
            Fragment myMatches = new MyMatches();
            loadFragment(myMatches,"MyMatches");
        });
        binding.referral.setOnClickListener(view1 -> {
            Fragment connections = new MyConnections();
            loadFragment(connections,"MyConnections");
        });
        binding.history.setOnClickListener(view1 -> {
            Fragment history = new WalletHistory();
            loadFragment(history,"WalletHistory");
        });
        binding.settings.setOnClickListener(view1 -> {
            Fragment settings = new Settings();
            loadFragment(settings,"Settings");
        });
        loadBanners();
        loadMatchList();
        return view;
    }

    private void loadTopBanners() {
        DatabaseReference databaseReference20 = FirebaseDatabase.getInstance().getReference();
        databaseReference20.child("HomeBanners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    sliderModelArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        SliderModel sliderModel = dataSnapshot.getValue(SliderModel.class);
                        sliderModelArrayList.add(sliderModel);
                    }
                    for (int i = 0; i < sliderModelArrayList.size(); i++) {
                        String downloadImageUrl = sliderModelArrayList.get(i).getImageUrl();
                        ImageView imageView = new ImageView(getContext());
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(getContext()).load(downloadImageUrl).into(imageView);
                        binding.topSlider.addView(imageView);
                        binding.topSlider.setFlipInterval(2500);
                        binding.topSlider.setAutoStart(true);
                        binding.topSlider.startFlipping();
                        binding.topSlider.setInAnimation(getContext(), android.R.anim.slide_in_left);
                        binding.topSlider.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                        loading.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBanners() {
        DatabaseReference databaseReferenceBanner = FirebaseDatabase.getInstance().getReference();
        databaseReferenceBanner.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    sliderModelArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        SliderModel sliderModel = dataSnapshot.getValue(SliderModel.class);
                        sliderModelArrayList.add(sliderModel);
                    }
                    for (int i = 0; i < sliderModelArrayList.size(); i++) {
                        String downloadImageUrl = sliderModelArrayList.get(i).getImageUrl();
                        ImageView imageView = new ImageView(getContext());
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(getContext()).load(downloadImageUrl).into(imageView);
                        binding.slider.addView(imageView);
                        binding.slider.setFlipInterval(2500);
                        binding.slider.setAutoStart(true);
                        binding.slider.startFlipping();
                        binding.slider.setInAnimation(getContext(), android.R.anim.slide_in_left);
                        binding.slider.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMatchList() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("INDIA11Users").child(UID).child("UserStats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String earned = snapshot.child("earned").getValue().toString();
                    String played = snapshot.child("played").getValue().toString();
                    String won = snapshot.child("won").getValue().toString();
                    Common.earned = earned;
                    Common.played = played;
                    Common.won = won;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProfileDetails() {
        databaseReferenceProfile.child("INDIA11Users").child(UID).child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userNameID = snapshot.child("userReferralCode").getValue().toString();
                Common.userNameID = userNameID;
                String profileName = snapshot.child("userName").getValue().toString();
                Common.profileName = profileName;
                String profileEmail = snapshot.child("userEmail").getValue().toString();
                Common.profileEmail = profileEmail;
                String profileMobile = snapshot.child("userMobile").getValue().toString();
                Common.profileMobile = profileMobile;
                String profilePic = snapshot.child("profilePic").getValue().toString();
                Common.userProfilePic = profilePic;
                String nickName = snapshot.child("nickName").getValue().toString();
                Common.nickName = nickName;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAvailableContests() {
        binding.availableContestRecycler.setHasFixedSize(true);
        binding.availableContestRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        databaseReference.child("AvailableContests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableContestsModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AvailableContestsModel availableContestsModel = dataSnapshot.getValue(AvailableContestsModel.class);
                    availableContestsModelList.add(availableContestsModel);
                }
                availableContestsAdapter = new AvailableContestsAdapter(getContext(),availableContestsModelList);
                binding.availableContestRecycler.setAdapter(availableContestsAdapter);
                availableContestsAdapter.notifyDataSetChanged();
                loading.dismiss();
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


}