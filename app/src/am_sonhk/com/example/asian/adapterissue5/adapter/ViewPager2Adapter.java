package com.example.asian.adapterissue5.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class ViewPager2Adapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitle = new ArrayList<>();

    public ViewPager2Adapter(FragmentManager supportFragmentManager, Lifecycle lifecycle) {
        super(supportFragmentManager, lifecycle);
    }

    // Add fragment and title to the list
    public void addFragment(Fragment fragment, String title) {
        mFragmentArrayList.add(fragment);
        mFragmentTitle.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment at the specified position
        return mFragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        // Return the total number of fragments
        return mFragmentArrayList.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        // Return the title associated with the fragment at the specified position
        return mFragmentTitle.get(position);
    }
}
