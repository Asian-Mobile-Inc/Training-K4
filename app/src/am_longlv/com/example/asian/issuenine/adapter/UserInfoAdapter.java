package com.example.asian.issuenine.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issuenine.database.DBHelper;
import com.example.asian.issuenine.model.UserInfo;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private final List<UserInfo> mUserInfoLists;
    private final Context mContext;

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
        if (userInfo!=null && userInfo.getUserId()!=-1){
            holder.mTvUserId.setText(String.valueOf(userInfo.getUserId()));
            holder.mTvUsername.setText(userInfo.getUsername());
            holder.mTvAge.setText(userInfo.getAge());
        }else{
            holder.mBtnDelete.setVisibility(View.GONE);
        }
    }
    private void initListener(UserInfo userInfo, ViewHolder holder){
        holder.mBtnDelete.setOnClickListener(v -> deleteUser(userInfo));
    }
    public void deleteUser(UserInfo userInfo){
        try (DBHelper mDBHelper = new DBHelper(this.mContext, "User.db", 1)) {
            mDBHelper.deleteUser(userInfo);
            mUserInfoLists.remove(userInfo);
            int position = mUserInfoLists.indexOf(userInfo);
            for (int i= position; i < mUserInfoLists.size(); i++) {
                notifyItemChanged(i);
            }
        }catch (Exception e){
            Toast.makeText(mContext, mContext.getString(R.string.err_load_data), Toast.LENGTH_SHORT).show();
        }
    }
    public void getAllUser(DBHelper mDBHelper){
        mUserInfoLists.clear();
        mUserInfoLists.add(0,new UserInfo(-1,"",""));
        mUserInfoLists.addAll(mDBHelper.getAllUser());
        for (int i = 0; i < mUserInfoLists.size(); i++) {
            notifyItemChanged(i);
        }
    }
    public void addUser(UserInfo userInfo){
        mUserInfoLists.add(userInfo);
        for (int i = 0; i < mUserInfoLists.size(); i++) {
            if (mUserInfoLists.get(i)!=null){
                Log.d("androidRuntime", "addUser: "+mUserInfoLists.get(i).getUserId());
            }

        }
        notifyItemInserted(mUserInfoLists.size()-1);
    }
}
