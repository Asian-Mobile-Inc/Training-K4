package com.example.asian.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asian.R;

public class FragmentOne extends Fragment {

    private static String mParamColor;

    public static FragmentOne newInstance(String paramColor) {
        FragmentOne fragment = new FragmentOne();
        mParamColor = paramColor;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = view.findViewById(R.id.rlFragmentOne);
        relativeLayout.setBackgroundColor(Color.parseColor(mParamColor));
        super.onViewCreated(view, savedInstanceState);
    }
}