package com.example.asian.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class TwoFragment extends Fragment {
    private RelativeLayout mRlFragmentTwo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        initView(view);
        getDataColor();
        return view;
    }

    private void initView(View view) {
        mRlFragmentTwo = view.findViewById(R.id.rlFragmentTwo);
    }

    private void getDataColor() {
        Bundle data = getArguments();
        if (data != null) {
            mRlFragmentTwo.setBackgroundColor(data.getInt(Constants.KEY_COLOR_FRAGMENT));
        }
    }
}
