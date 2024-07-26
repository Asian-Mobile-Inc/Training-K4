package com.example.asian.IssueFive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asian.IssueFive.Adapter.RecyclerViewAdapter;
import com.example.asian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueFiveThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueFiveThirdFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<String> mLists;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FIRST_NAME_ITEM = "Item 3";
    private static final char FIRST_CHAR = 'A', LAST_CHAR = 'Z';

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IssueFiveThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IssueFiveThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssueFiveThirdFragment newInstance(String param1, String param2) {
        IssueFiveThirdFragment fragment = new IssueFiveThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issue_five_third, container, false);
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

    public void updateListItem() {
        this.mLists.add(FIRST_NAME_ITEM + (char) (FIRST_CHAR + mLists.size() % 26));
        mRecyclerView.scrollToPosition(mLists.size() - 1);
        mAdapter.updateData(mLists);
    }
}
