package com.example.asian.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.asian.fragment.ViewFragment;
import com.example.asian.model.Item;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Item> mItems;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity,ArrayList<Item> items) {
        super(fragmentActivity);
        mItems = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArrayList<Item> listItem = new ArrayList<>();
        int tabOne = 1;
        int tabTwo = 2;
        int tabThree = 3;
        if (position == tabOne) {
            for (Item i : mItems) {
                if (i.getName().contains(String.valueOf(tabOne))) {
                    listItem.add(i);
                }
            }
        } else if (position == tabTwo) {
            for (Item i : mItems) {
                if (i.getName().contains(String.valueOf(tabTwo))) {
                    listItem.add(i);
                }
            }
        } else if (position == tabThree) {
            for (Item i : mItems) {
                if (i.getName().contains(String.valueOf(tabThree))) {
                    listItem.add(i);
                }
            }
        }
        return new ViewFragment(listItem);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
