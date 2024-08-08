package com.example.asian.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asian.R;

public class FragmentTwo extends Fragment {

    private static final String ARG_COLOR = "paramColor";
    private int mParamColor;

    public static FragmentTwo newInstance(int paramColor) {
        FragmentTwo fragment = new FragmentTwo();
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlFragmentTwo);
        relativeLayout.setBackgroundColor(mParamColor);
        Log.e("FragmentTwo","onCreateView: ");
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FragmentTwo", "onPause: ");
    }
}
