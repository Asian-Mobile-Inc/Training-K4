package com.example.asian.issuefour.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.asian.Constant;
import com.example.asian.R;

public class IssueFourFirstFragment extends Fragment {
    private OnFragmentFirstChange mListener;

    public interface OnFragmentFirstChange {
        void onFragmentFirstChange(String colorCode);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public IssueFourFirstFragment() {
    }

    public static IssueFourFirstFragment newInstance(String param1, String param2) {
        IssueFourFirstFragment fragment = new IssueFourFirstFragment();
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
        View view = inflater.inflate(R.layout.fragment_issue_four_first, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlMainFrOne);
        String colorCode = getArguments().getString(Constant.KEY_COLOR_CODE);
        if (colorCode != null && !colorCode.isEmpty()) {
            if (!colorCode.startsWith(Constant.START_CHAR_COLOR)) {
                colorCode = Constant.START_CHAR_COLOR + colorCode;
            }
            try {
                Color.parseColor(colorCode);
            } catch (Exception e) {
                colorCode = Constant.COLOR_DEFAULT;
            }
            relativeLayout.setBackgroundColor(Color.parseColor(colorCode));
        }
        mListener.onFragmentFirstChange(colorCode);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnFragmentFirstChange) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
