package com.example.asian.issuefive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asian.issuefive.adapter.RecyclerViewAdapter;
import com.example.asian.R;

import java.util.ArrayList;
import java.util.List;

public class IssueFiveFirstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<String> mLists;
    private static final String FIRST_NAME_ITEM = "Item 1";
    private static final char FIRST_CHAR = 'A', LAST_CHAR = 'Z';

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
        for (char i = FIRST_CHAR; i <= LAST_CHAR; i++) {
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
        this.mLists.add(name);
        mRecyclerView.scrollToPosition(mLists.size() - 1);
        mAdapter.updateDataPosition(mLists, mLists.size() - 1);
    }
}
