package com.example.asian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> mUserList;
    private final DatabaseHelper mDatabaseHelper;

    public UserAdapter(List<User> userList, DatabaseHelper databaseHelper) {
        this.mUserList = userList;
        this.mDatabaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.tvUserId.setText(String.valueOf(user.getUserId()));
        holder.tvUserName.setText(user.getUserName());
        holder.tvUserAge.setText(String.valueOf(user.getAge()));

        holder.btnDeleteUser.setOnClickListener(v -> {
            // Delete the user from the database
            mDatabaseHelper.deleteUserById(user.getUserId());
            // Remove the user from the list and notify RecyclerView by using DiffUtil
            List<User> newListUser = new ArrayList<>(mUserList);
            newListUser.remove(user);
            updateList(newListUser);
            // Show a toast message
            Toast.makeText(holder.itemView.getContext(), holder.itemView.getContext().getString(R.string.user_deleted), Toast.LENGTH_SHORT).show();
//            Toast.makeText()
        });
    }

    public void updateList(List<User> newList) {
        MyDiffUtilsCallback diffCallback = new MyDiffUtilsCallback(this.mUserList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        mUserList.clear();
        mUserList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserId;
        TextView tvUserName;
        TextView tvUserAge;
        Button btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserAge = itemView.findViewById(R.id.tvUserAge);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
