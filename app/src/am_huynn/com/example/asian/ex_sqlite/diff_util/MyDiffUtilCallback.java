package com.example.asian.ex_sqlite.diff_util;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.asian.ex_sqlite.model.User;

import java.util.ArrayList;

public class MyDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<User> newList;
    ArrayList<User> oldList;

    public MyDiffUtilCallback(ArrayList<User> oldList, ArrayList<User> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        if (oldList == null) {
            return 0;
        } else {
            return oldList.size();
        }
    }

    @Override
    public int getNewListSize() {
        if (newList == null) {
            return 0;
        } else {
            return newList.size();
        }
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUserId() == newList.get(newItemPosition).getUserId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (!oldList.get(oldItemPosition).getUserName().equals(newList.get(newItemPosition).getUserName())) {
            return false;
        } else {
            if (oldList.get(oldItemPosition).getAge() != newList.get(newItemPosition).getAge()) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
