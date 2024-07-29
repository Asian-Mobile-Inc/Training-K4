package com.example.asian.issuefive.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.asian.issuefive.fragment.IssueFiveFirstFragment;
import com.example.asian.issuefive.fragment.IssueFiveSecondFragment;
import com.example.asian.issuefive.fragment.IssueFiveThirdFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new IssueFiveFirstFragment();
            case 1:
                return new IssueFiveSecondFragment();

            case 2:
                return new IssueFiveThirdFragment();
            default:
                return new IssueFiveFirstFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
