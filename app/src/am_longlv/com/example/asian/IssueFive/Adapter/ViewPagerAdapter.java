package com.example.asian.IssueFive.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.asian.IssueFive.Fragment.IssueFiveFirstFragment;
import com.example.asian.IssueFive.Fragment.IssueFiveSecondFragment;
import com.example.asian.IssueFive.Fragment.IssueFiveThirdFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
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
        return 3;
    }
}
