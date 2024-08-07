package com.example.asian.issuefive.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;

import java.util.ArrayList;
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

    private final List<String> mLists;
    private final Context mContext;

    public RecyclerViewAdapter(List<String> mLists, Context mContext) {
        this.mLists = mLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_issue5, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvItemIssue5.setText(mLists.get(position));
        holder.mTvItemIssue5.setOnClickListener(v -> showDialogActionItem(holder.getAdapterPosition()));
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

    private void showDialogActionItem(int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_action_item_issue_five);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setUpActionItemDialog(dialog, position);
        dialog.show();
    }

    private void setUpActionItemDialog(Dialog dialog, int position) {
        TextView tvTitle = dialog.findViewById(R.id.tvTitleItem);
        TextView tvEdit = dialog.findViewById(R.id.tvRename);
        TextView tvDelete = dialog.findViewById(R.id.tvDelete);
        tvTitle.setText(mLists.get(position));
        tvEdit.setOnClickListener(v -> {
            showDialogConfirmRename(position);
            dialog.dismiss();
        });
        tvDelete.setOnClickListener(v -> {
            showDialogConfirmDelete(position);
            dialog.dismiss();
        });
    }

    private void showDialogConfirmDelete(int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> {
            List<String> lists = new ArrayList<>(mLists);
            lists.remove(position);
            updateData(lists);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showDialogConfirmRename(int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_name);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            EditText edtNewName = dialog.findViewById(R.id.edtNewName);
            List<String> lists = new ArrayList<>(mLists);
            lists.set(position, edtNewName.getText().toString());
            updateData(lists);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
