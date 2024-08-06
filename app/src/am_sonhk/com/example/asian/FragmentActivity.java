package com.example.asian;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.asian.FragmentOne;
import com.example.asian.FragmentTwo;
import com.example.asian.R;

public class FragmentActivity extends AppCompatActivity {
    public static final String KEY_COLOR_FRAGMENT = "keyColorFragment";

    private int mFragmentClickCount = 0;
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        initView();
        initListener();
    }

    private void initView() {
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
    }

    private void initListener() {
        mBtnFragmentOne.setOnClickListener(v -> replaceFragment(FragmentOne.newInstance("#4CAF50")));
        mBtnFragmentTwo.setOnClickListener(v -> addFragment(FragmentTwo.newInstance("#9C27B0")));

//        getSupportFragmentManager().addOnBackStackChangedListener(this::updateTitle);
    }

    private void replaceFragment(FragmentOne fragment) {
        mFragmentClickCount++;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.fragmentContainer, fragment);
        if (mFragmentClickCount > 2) {
            mTransaction.addToBackStack(null);
        }
        mTransaction.commit();
//        updateTitle();
    }

    private void addFragment(FragmentTwo fragment) {
        mFragmentClickCount++;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.fragmentContainer, fragment);
        if (mFragmentClickCount > 2) {
            mTransaction.addToBackStack(null);
        }
        mTransaction.commit();
//        updateTitle();
    }
    }
}

