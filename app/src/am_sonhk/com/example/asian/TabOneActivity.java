package com.example.asian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.adapterIssue5.CustomAdapter;

import java.util.ArrayList;

public class TabOneActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private CustomAdapter mCustomAdapter;
    private final ArrayList<String> mListData;
    private View mRootView;

    public TabOneActivity(ArrayList<String> mListData) {
        this.mListData = mListData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recycler_view_list_item, container, false);
        initView();
        createRecyclerView();
        return mRootView;
    }

    public void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
    }

    public void createRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mCustomAdapter = new CustomAdapter(this.getContext(), mListData);
        mRecyclerView.setAdapter(mCustomAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addDataToRecyclerView(String newData) {
        mCustomAdapter.addItem(newData);
    }

    public void delDataToRecyclerView(String item) {
        mCustomAdapter.removeItem(item);
    }
}
