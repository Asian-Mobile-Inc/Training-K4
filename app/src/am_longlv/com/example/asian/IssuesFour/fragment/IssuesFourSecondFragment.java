package com.example.asian.IssuesFour.fragment;

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
import com.example.asian.IssuesFour.IssuesFourActivity;
import com.example.asian.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssuesFourSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssuesFourSecondFragment extends Fragment {
    private OnFragmentSecondChange mListener;

    public interface OnFragmentSecondChange {
        void onFragmentSecondChange(String colorCode);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IssuesFourSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IssuesFourSecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssuesFourSecondFragment newInstance(String param1, String param2) {
        IssuesFourSecondFragment fragment = new IssuesFourSecondFragment();
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
        View view = inflater.inflate(R.layout.fragment_issues_four_second, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.rlMainFrTwo);
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
        mListener.onFragmentSecondChange(colorCode);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnFragmentSecondChange) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
