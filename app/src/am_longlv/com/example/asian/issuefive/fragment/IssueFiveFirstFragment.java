package com.example.asian.issuefive.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asian.Constant;
import com.example.asian.issuefive.adapter.RecyclerViewAdapter;
import com.example.asian.R;

import java.util.ArrayList;
import java.util.List;

public class IssueFiveFirstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<String> mLists;
    private static final String FIRST_NAME_ITEM = "Item 1";

    public IssueFiveFirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_five_first, container, false);
        setUpListData();
        setUpRecyclerView(view);
        return view;
    }

    private void setUpListData() {
        mLists = new ArrayList<>();
        for (char i = Constant.FIRST_CHAR; i <= Constant.LAST_CHAR; i++) {
            mLists.add(FIRST_NAME_ITEM + i);
        }
    }

    private void setUpRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.rvFragmentFirst);
        mAdapter = new RecyclerViewAdapter(mLists, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateListItem(String name) {
        List<String> mLists = new ArrayList<>(this.mLists);
        mLists.add(name);
        mAdapter.updateData(mLists);
        mRecyclerView.scrollToPosition(mLists.size() - 1);
    }

    public void showDialogActionItem(int position) {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_action_item_issue_five);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            setUpActionItemDialog(dialog, position);
            dialog.show();
        }
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
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            dialog.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> {
                List<String> lists = new ArrayList<>(mLists);
                lists.remove(position);
                mAdapter.updateData(lists);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }

    private void showDialogConfirmRename(int position) {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_name);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
                EditText edtNewName = dialog.findViewById(R.id.edtNewName);
                List<String> lists = new ArrayList<>(mLists);
                lists.set(position, edtNewName.getText().toString());
                mAdapter.updateData(lists);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }
}
