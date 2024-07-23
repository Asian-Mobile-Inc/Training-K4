package com.example.asian.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.asian.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Issues4f1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Issues4f1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Issues4f1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Issues4_1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Issues4f1Fragment newInstance(String param1, String param2) {
        Issues4f1Fragment fragment = new Issues4f1Fragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issues4_1, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.main_fr_1);
        String colorCode = getArguments().getString("colorCode");
        if (colorCode != null && !colorCode.isEmpty()) {
            if (!colorCode.startsWith("#")) {
                colorCode = "#" + colorCode;
            }
            try {
                Color.parseColor(colorCode);
            } catch (Exception e) {
                colorCode = "#000000";
            }
            relativeLayout.setBackgroundColor(Color.parseColor(colorCode));
        }
        return view;
    }
}