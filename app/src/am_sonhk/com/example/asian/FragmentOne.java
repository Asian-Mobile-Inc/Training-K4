package com.example.asian;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FragmentOne extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView mTextView = mView.findViewById(R.id.fragmentText);
        mTextView.setText("Fragment One");

        if (getArguments() != null) {
            String mColor = getArguments().getString("color");
            if (mColor != null) {
                mView.setBackgroundColor(Color.parseColor(mColor));
            }
        }

        return mView;
    }

    @Override
    public String getFragmentName() {
        return "Fragment One";
    }

    public static FragmentOne newInstance(String color) {
        FragmentOne mFragment = new FragmentOne();
        Bundle mArgs = new Bundle();
        mArgs.putString("color", color);
        mFragment.setArguments(mArgs);
        return mFragment;
    }
}