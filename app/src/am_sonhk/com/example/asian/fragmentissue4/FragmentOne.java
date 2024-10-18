package com.example.asian.fragmentissue4;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asian.R;

public class FragmentOne extends Fragment {

    private static final String ARG_COLOR = "paramColor";
    private int mParamColor;

    public static FragmentOne newInstance(int paramColor) {
        FragmentOne fragment = new FragmentOne();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, paramColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamColor = getArguments().getInt(ARG_COLOR, Color.WHITE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlFragmentOne);
        relativeLayout.setBackgroundColor(mParamColor);
        return view;
    }
}
