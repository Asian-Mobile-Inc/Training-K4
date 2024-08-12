package com.example.asian.issuefive.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.asian.issuefive.fragment.IssueFiveFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return IssueFiveFragment.newInstance(position + 1);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
