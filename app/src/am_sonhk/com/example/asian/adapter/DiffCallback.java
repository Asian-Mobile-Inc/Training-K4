package com.example.asian.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffCallback extends DiffUtil.Callback {

    private final List<String> oldList;
    private final List<String> newList;

    public DiffCallback(List<String> oldList, List<String> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare items based on their unique identifier
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compare items based on their content
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
