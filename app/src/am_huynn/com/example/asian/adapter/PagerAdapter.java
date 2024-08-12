package com.example.asian.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.asian.fragment.ViewFragment;
import com.example.asian.model.Item;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Item> mItems;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Item> items) {
        super(fm, behavior);
        this.mItems = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ArrayList<Item> listItem = new ArrayList<>();
        if (position == 0) {
            for (Item i : mItems) {
                if (i.getName().contains("1")) {
                    listItem.add(i);
                }
            }
        } else if (position == 1) {
            for (Item i : mItems) {
                if (i.getName().contains("2")) {
                    listItem.add(i);
                }
            }
        } else if (position == 2) {
            for (Item i : mItems) {
                if (i.getName().contains("3")) {
                    listItem.add(i);
                }
            }
        }
        return new ViewFragment(listItem);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Tab 1";
                break;
            case 1:
                title = "Tab 2";
                break;
            case 2:
                title = "Tab 3";
                break;
        }
        return title;
    }
}
