package com.example.asian.diff;

import androidx.recyclerview.widget.DiffUtil;

import com.example.asian.model.Item;

import java.util.ArrayList;

public class ItemDiffUtilCallBack extends DiffUtil.Callback {
    private final ArrayList<Item> mOldItems;
    private final ArrayList<Item> mNewItems;

    public ItemDiffUtilCallBack(ArrayList<Item> oldItems, ArrayList<Item> newItems) {
        this.mOldItems = oldItems;
        this.mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).getItemId() == mNewItems.get(newItemPosition).getItemId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).getItemName().equals(mNewItems.get(newItemPosition).getItemName());
    }
}
