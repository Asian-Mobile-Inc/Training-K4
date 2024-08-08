package com.example.asian.issuefive.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static class DiffStringCallback extends DiffUtil.Callback {
        private final List<String> mOldLists;
        private final List<String> mNewLists;

        public DiffStringCallback(List<String> mOldList, List<String> mNewList) {
            this.mOldLists = mOldList;
            this.mNewLists = mNewList;
        }

        @Override
        public int getOldListSize() {
            return mOldLists.size();
        }

        @Override
        public int getNewListSize() {
            return mNewLists.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldLists.get(oldItemPosition).equals(mNewLists.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldLists.get(oldItemPosition).equals(mNewLists.get(newItemPosition));
        }
    }

    public interface OnItemSelected {
        void onItemSelected(int position);
    }

    private final OnItemSelected mOnItemSelected;
    private final List<String> mLists;

    public RecyclerViewAdapter(List<String> mLists, Context mContext) {
        mOnItemSelected = (OnItemSelected) mContext;
        this.mLists = mLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_issue5, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvItemIssue5.setText(mLists.get(position));
        holder.mTvItemIssue5.setOnClickListener(v -> {
            mOnItemSelected.onItemSelected(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        if (mLists == null) {
            return 0;
        } else {
            return mLists.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvItemIssue5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemIssue5 = itemView.findViewById(R.id.tvItemIssue5);
        }
    }

    public void updateData(List<String> lists) {
        DiffStringCallback diffCallback = new DiffStringCallback(this.mLists, lists);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
        this.mLists.clear();
        this.mLists.addAll(lists);
    }
}
