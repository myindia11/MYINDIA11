package com.example.india11.BottomNavigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.india11.R;
import com.example.india11.databinding.FragmentFAQBinding;

public class FAQ extends Fragment {
    private FragmentFAQBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFAQBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.downkey1.setOnClickListener(view1 -> {
            binding.downkey1.setVisibility(View.GONE);
            binding.upkey1.setVisibility(View.VISIBLE);
            binding.faq1layout.setVisibility(View.GONE);
        });
        binding.upkey1.setOnClickListener(view1 -> {
            binding.upkey1.setVisibility(View.GONE);
            binding.downkey1.setVisibility(View.VISIBLE);
            binding.faq1layout.setVisibility(View.VISIBLE);
        });
        binding.downkey2.setOnClickListener(view1 -> {
            binding.downkey2.setVisibility(View.GONE);
            binding.upkey2.setVisibility(View.VISIBLE);
            binding.faq2layout.setVisibility(View.GONE);
        });
        binding.upkey2.setOnClickListener(view1 -> {
            binding.upkey2.setVisibility(View.GONE);
            binding.downkey2.setVisibility(View.VISIBLE);
            binding.faq2layout.setVisibility(View.VISIBLE);
        });
        binding.downkey3.setOnClickListener(view1 -> {
            binding.downkey3.setVisibility(View.GONE);
            binding.upkey3.setVisibility(View.VISIBLE);
            binding.faq3layout.setVisibility(View.GONE);
        });
        binding.upkey3.setOnClickListener(view1 -> {
            binding.upkey3.setVisibility(View.GONE);
            binding.downkey3.setVisibility(View.VISIBLE);
            binding.faq3layout.setVisibility(View.VISIBLE);
        });
        return view;
    }
}