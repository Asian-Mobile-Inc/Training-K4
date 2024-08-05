package com.example.asian.issuenine.diff;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.asian.issuenine.model.UserInfo;

import java.util.List;

public class UserDiffCallback extends DiffUtil.Callback {
    private final List<UserInfo> mUserOldLists;
    private final List<UserInfo> mUserNewLists;

    public UserDiffCallback(List<UserInfo> mUserOldLists, List<UserInfo> mUserNewLists) {
        this.mUserOldLists = mUserOldLists;
        this.mUserNewLists = mUserNewLists;
    }

    @Override
    public int getOldListSize() {
        return mUserOldLists.size();
    }

    @Override
    public int getNewListSize() {
        return mUserNewLists.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mUserOldLists.get(oldItemPosition).getUserId() == mUserNewLists.get(newItemPosition).getUserId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mUserOldLists.get(oldItemPosition).getUsername().equals(mUserNewLists.get(newItemPosition).getUsername()) &&
                mUserOldLists.get(oldItemPosition).getAge().equals(mUserNewLists.get(newItemPosition).getAge());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
