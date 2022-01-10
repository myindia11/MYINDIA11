package com.example.india11.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ContestsPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentsList=new ArrayList<>();
    private final ArrayList<String> fragmentTitle=new ArrayList<>();

    public ContestsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ContestsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
    public void addFragment(Fragment fragment,String title)
    {
        fragmentsList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

}
