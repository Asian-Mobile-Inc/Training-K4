package com.example.asian;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private int mFragmentClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        Button mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);

        mBtnFragmentOne.setOnClickListener(v -> replaceFragment(FragmentOne.newInstance("#4CAF50")));
        mBtnFragmentTwo.setOnClickListener(v -> addFragment(FragmentTwo.newInstance("#9C27B0")));

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateTitle);
    }

    private void replaceFragment(BaseFragment fragment) {
        mFragmentClickCount++;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.fragmentContainer, fragment);
        if (mFragmentClickCount > 2) {
            mTransaction.addToBackStack(null);
        }
        mTransaction.commit();
        updateTitle();
    }

    private void addFragment(BaseFragment fragment) {
        mFragmentClickCount++;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.fragmentContainer, fragment);
        if (mFragmentClickCount > 2) {
            mTransaction.addToBackStack(null);
        }
        mTransaction.commit();
        updateTitle();
    }

    private void updateTitle() {
        Fragment mCurrentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (mCurrentFragment instanceof BaseFragment) {
            setTitle(((BaseFragment) mCurrentFragment).getFragmentName());
        } else {
            setTitle("Android Fragment");
        }
    }
}