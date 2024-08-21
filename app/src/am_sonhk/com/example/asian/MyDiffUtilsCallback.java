package com.example.asian;

import androidx.recyclerview.widget.DiffUtil;

import com.example.asian.User;


import java.util.List;

public class MyDiffUtilsCallback extends DiffUtil.Callback {

    private final List<User> mOldUsers;
    private final List<User> mNewUsers;

    public MyDiffUtilsCallback(List<User> oldUsers, List<User> newUsers) {
        this.mOldUsers = oldUsers;
        this.mNewUsers = newUsers;
    }

    @Override
    public int getOldListSize() {
        return mOldUsers.size();
    }

    @Override
    public int getNewListSize() {
        return mNewUsers.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldUsers.get(oldItemPosition).getUserId()==(mNewUsers.get(newItemPosition).getUserId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = mOldUsers.get(oldItemPosition);
        User newUser = mNewUsers.get(newItemPosition);
        return oldUser.equals(newUser);
    }
}
