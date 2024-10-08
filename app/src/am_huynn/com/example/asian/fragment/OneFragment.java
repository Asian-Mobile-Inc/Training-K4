package com.example.asian.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.asian.R;
import com.example.asian.constants.Constants;

public class OneFragment extends Fragment {
    private FragmentOneListener mFragmentOneListener;

    private RelativeLayout mRlFragmentOne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initView(view);
        getDataColor();
        return view;
    }

    private void initView(View view) {
        mRlFragmentOne = view.findViewById(R.id.rlFragmentOne);
    }

    private void getDataColor() {
        Bundle data = getArguments();
        if (data != null) {
            mRlFragmentOne.setBackgroundColor(data.getInt(Constants.KEY_COLOR_FRAGMENT));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentOneListener) {
            mFragmentOneListener = (FragmentOneListener) context;
            mFragmentOneListener.setTitleFragmentOne();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentOneListener = null;
    }

    public interface FragmentOneListener {
        void setTitleFragmentOne();
    }
}
