package com.example.asian.ex_sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.ex_sqlite.helper.UserSQLiteHelper;
import com.example.asian.ex_sqlite.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> mUsers;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.mContext = context;
        this.mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View userView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mTvUserId.setText(String.valueOf(user.getUserId()));
        holder.mTvUserName.setText(user.getUserName());
        holder.mTvAge.setText(String.valueOf(user.getAge()));
        holder.mBtnDelete.setOnClickListener(view -> {
            UserSQLiteHelper userSQLiteHelper = new UserSQLiteHelper(mContext);
            userSQLiteHelper.deleteUser(user.getUserId());
            deleteUser(user.getUserId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView mTvUserId;
        final private TextView mTvUserName;
        final private TextView mTvAge;
        final private Button mBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUserId = itemView.findViewById(R.id.tvUserId);
            mTvUserName = itemView.findViewById(R.id.tvUserName);
            mTvAge = itemView.findViewById(R.id.tvAge);
            mBtnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void addUser(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size());
    }

    public void deleteAll() {
        int size = mUsers.size();
        mUsers.clear();
        notifyItemRangeRemoved(0, size);
    }

    private void deleteUser(int id, int position) {
        for (User user : mUsers) {
            if (user.getUserId() == id) {
                mUsers.remove(user);
                notifyItemRemoved(position);
                return;
            }
        }
    }

    public void showAllUser(ArrayList<User> users) {
        mUsers = users;
        notifyItemRangeChanged(0, mUsers.size());
    }
}
