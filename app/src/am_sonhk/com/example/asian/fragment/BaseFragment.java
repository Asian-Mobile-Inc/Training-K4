package com.example.asian.fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public static final String KEY_COLOR_FRAGMENT = "keyColorFragment";

    public abstract String getFragmentName();
}
