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

    public IssueFourFirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_four_first, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlMainFrOne);
        String colorCode = null;
        if (getArguments() != null) {
            colorCode = getArguments().getString(Constant.KEY_COLOR_CODE);
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
