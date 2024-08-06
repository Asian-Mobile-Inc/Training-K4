package com.example.asian.issuenine.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issuenine.database.DBHelper;
import com.example.asian.issuenine.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private final List<UserInfo> mUserInfoLists;
    private final Context mContext;

    public UserInfoAdapter(List<UserInfo> mUserInfoLists, Context mContext) {
        this.mUserInfoLists = mUserInfoLists;
        this.mContext = mContext;
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

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            UserInfo userInfo = mUserNewLists.get(newItemPosition);
            UserInfo oldUserInfo = mUserOldLists.get(oldItemPosition);
            Bundle bundle = new Bundle();
            if (!userInfo.getUsername().equals(oldUserInfo.getUsername())) {
                bundle.putString("username", userInfo.getUsername());
            }
            if (!userInfo.getAge().equals(oldUserInfo.getAge())) {
                bundle.putString("age", userInfo.getAge());
            }
            return bundle;
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
        UserInfo userInfo = mUserInfoLists.get(position);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else{
            Bundle bundle = (Bundle) payloads.get(0);
            for (String key : bundle.keySet()){
                if (key.equals("username")){
                    holder.mTvUsername.setText(bundle.getString(key));
                }
                if (key.equals("age")){
                    holder.mTvAge.setText(bundle.getString(key));
                }
            }
        }
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
        holder.mBtnDelete.setOnClickListener(v -> deleteUser(userInfo));
    }

    public void deleteUser(UserInfo userInfo) {
        try (DBHelper mDBHelper = new DBHelper(this.mContext, "User.db", 1)) {
            mDBHelper.deleteUser(userInfo);
            List<UserInfo> mUserInfoNewLists = new ArrayList<>();
            mUserInfoNewLists.add(0, new UserInfo(-1, "", ""));
            mUserInfoNewLists.addAll(mDBHelper.getAllUser());
            DiffUserCallBack diffUserCallBack = new DiffUserCallBack(mUserInfoLists, mUserInfoNewLists);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUserCallBack);
            mUserInfoLists.clear();
            mUserInfoLists.addAll(mUserInfoNewLists);
            diffResult.dispatchUpdatesTo(this);
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.err_load_data), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(List<UserInfo> userInfoNewLists) {
        DiffUserCallBack diffUserCallBack = new DiffUserCallBack(mUserInfoLists, userInfoNewLists);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUserCallBack);
        diffResult.dispatchUpdatesTo(this);
        mUserInfoLists.clear();
        mUserInfoLists.addAll(userInfoNewLists);
    }
}
