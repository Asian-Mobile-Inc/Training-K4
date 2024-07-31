package com.example.asian.issuenine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issuenine.database.DBHelper;
import com.example.asian.issuenine.model.UserInfo;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private List<UserInfo> mUserInfoLists;
    private Context mContext;

    public UserInfoAdapter(List<UserInfo> mUserInfoLists, Context mContext) {
        this.mUserInfoLists = mUserInfoLists;
        this.mContext = mContext;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvUserId;
        private TextView mTvUsername;
        private TextView mTvAge;
        private Button mBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(this,itemView);
        }
    }
    private void initUI(ViewHolder holder,  View itemView){
        holder.mTvUserId = itemView.findViewById(R.id.tvUserId);
        holder.mTvUsername = itemView.findViewById(R.id.tvUserName);
        holder.mTvAge = itemView.findViewById(R.id.tvUserAge);
        holder.mBtnDelete = itemView.findViewById(R.id.btnDelete);
    }
    private void initData(UserInfo userInfo, ViewHolder holder){
        if (userInfo!=null){
            holder.mTvUserId.setText(String.valueOf(userInfo.getmUserId()));
            holder.mTvUsername.setText(userInfo.getmUsername());
            holder.mTvAge.setText(userInfo.getmAge());
        }else{
            holder.mBtnDelete.setVisibility(View.GONE);
        }
    }
    private void initListener(UserInfo userInfo, ViewHolder holder){
        holder.mBtnDelete.setOnClickListener(v -> {
            deleteUser(userInfo);
        });
    }
    public void deleteUser(UserInfo userInfo){
        DBHelper dbHelper = new DBHelper(mContext, "User.db", 1);
        dbHelper.deleteUser(userInfo);
        mUserInfoLists.remove(userInfo);
        notifyDataSetChanged();
    }
}
