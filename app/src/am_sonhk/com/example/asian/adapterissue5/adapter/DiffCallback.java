package com.example.asian.adapterissue5.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffCallback extends DiffUtil.Callback {

    private final List<String> mOldList;
    private final List<String> mNewList;

    public DiffCallback(List<String> oldList, List<String> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare items based on their unique identifier
        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare items based on their content
        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
    }
}
