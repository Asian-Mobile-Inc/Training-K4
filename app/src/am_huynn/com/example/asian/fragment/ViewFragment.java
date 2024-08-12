package com.example.asian.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.adapter.ItemAdapter;
import com.example.asian.model.Item;

import java.util.ArrayList;

public class ViewFragment extends Fragment {
    private final ArrayList<Item> mItems;
    private RecyclerView mRvItemsList;
    private ItemAdapter mItemAdapter;


    public ViewFragment(ArrayList<Item> items) {
        this.mItems = new ArrayList<>(items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        initView(view);
        setAdapter();
        return view;
    }

    private void initView(View view) {
        mRvItemsList = view.findViewById(R.id.rvItemsList);
    }

    private void setAdapter() {
        mItemAdapter = new ItemAdapter(getContext(), mItems);
        mRvItemsList.setAdapter(mItemAdapter);
        mRvItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void createItem(Item item) {
        mItems.add(item);
        mItemAdapter.setData(mItems);
    }

    public void updateItem(Item item) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getId() == item.getId()) {
                mItems.set(i, item);
                mItemAdapter.setData(mItems);
                return;
            }
        }
    }

    public void deleteItem(int id) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getId() == id) {
                mItems.remove(i);
                mItemAdapter.setData(mItems);
                return;
            }
        }
    }

}
