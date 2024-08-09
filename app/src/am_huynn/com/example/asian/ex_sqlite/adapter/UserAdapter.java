package com.example.asian.ex_sqlite.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.ex_sqlite.diff_util.MyDiffUtilCallback;
import com.example.asian.ex_sqlite.model.User;

import java.util.ArrayList;
import java.util.function.Function;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private IDeleteUser iDeleteUser;
    private final Context mContext;
    private ArrayList<User> mUsers;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.mContext = context;
        this.mUsers = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View userView = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mTvUserId.setText(String.valueOf(user.getUserId()));
        holder.mTvUserName.setText(user.getUserName());
        holder.mTvAge.setText(String.valueOf(user.getAge()));
        holder.mBtnDelete.setOnClickListener(view -> {
            if (mContext instanceof IDeleteUser) {
                iDeleteUser = (IDeleteUser) mContext;
                iDeleteUser.deleteUser(user.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
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

    public void setData(ArrayList<User> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallback(mUsers, newList));
        mUsers.clear();
        mUsers.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface IDeleteUser {
        void deleteUser(int id);
    }
}
