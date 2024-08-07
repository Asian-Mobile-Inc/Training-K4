package com.example.asian.issuenine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issuenine.model.UserInfo;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private final List<UserInfo> mUserInfoLists;
    private final OnItemSelected mOnItemSelected;

    public interface OnItemSelected {
        void onItemSelected(UserInfo userInfo);
    }

    public UserInfoAdapter(List<UserInfo> mUserInfoLists, Context mContext) {
        this.mUserInfoLists = mUserInfoLists;
        mOnItemSelected = (OnItemSelected) mContext;
    }

    public static class DiffUserCallBack extends DiffUtil.Callback {
        private final List<UserInfo> mUserOldLists;
        private final List<UserInfo> mUserNewLists;

        public DiffUserCallBack(List<UserInfo> mUserOldLists, List<UserInfo> mUserNewLists) {
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
    }

    @NonNull
    @Override
    public UserInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sqlite_tutorial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoAdapter.ViewHolder holder, int position) {
        UserInfo userInfo = mUserInfoLists.get(holder.getAdapterPosition());
        initData(userInfo, holder);
        initListener(userInfo, holder);
    }

    @Override
    public int getItemCount() {
        if (mUserInfoLists != null) {
            return mUserInfoLists.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvUserId;
        private TextView mTvUsername;
        private TextView mTvAge;
        private Button mBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(this, itemView);
        }
    }

    private void initUI(ViewHolder holder, View itemView) {
        holder.mTvUserId = itemView.findViewById(R.id.tvUserId);
        holder.mTvUsername = itemView.findViewById(R.id.tvUserName);
        holder.mTvAge = itemView.findViewById(R.id.tvUserAge);
        holder.mBtnDelete = itemView.findViewById(R.id.btnDelete);
    }

    private void initData(UserInfo userInfo, ViewHolder holder) {
        if (userInfo != null && userInfo.getUserId() != -1) {
            holder.mTvUserId.setText(String.valueOf(userInfo.getUserId()));
            holder.mTvUsername.setText(userInfo.getUsername());
            holder.mTvAge.setText(userInfo.getAge());
        } else {
            holder.mBtnDelete.setVisibility(View.GONE);
        }
    }

    private void initListener(UserInfo userInfo, ViewHolder holder) {
        holder.mBtnDelete.setOnClickListener(v -> {
            mOnItemSelected.onItemSelected(userInfo);
        });
    }

    public void updateData(List<UserInfo> userInfoNewLists) {
        DiffUserCallBack diffUserCallBack = new DiffUserCallBack(mUserInfoLists, userInfoNewLists);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUserCallBack);
        diffResult.dispatchUpdatesTo(this);
        mUserInfoLists.clear();
        mUserInfoLists.addAll(userInfoNewLists);
    }
}
