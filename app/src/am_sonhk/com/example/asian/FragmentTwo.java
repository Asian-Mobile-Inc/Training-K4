package com.example.asian;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FragmentTwo extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView mTextView = mView.findViewById(R.id.fragmentText);
        mTextView.setText(R.string.fragment_two);

        if (getArguments() != null) {
            String mColor = getArguments().getString(BaseFragment.KEY_COLOR_FRAGMENT);
            if (mColor != null) {
                mView.setBackgroundColor(Color.parseColor(mColor));
            }
        }

        return mView;
    }

    @Override
    public String getFragmentName() {
        return getString(R.string.fragment_two);
    }

    public static FragmentTwo newInstance(String color) {
        FragmentTwo mFragment = new FragmentTwo();
        Bundle mArgs = new Bundle();
        mArgs.putString(BaseFragment.KEY_COLOR_FRAGMENT, color);
        mFragment.setArguments(mArgs);
        return mFragment;
    }
}