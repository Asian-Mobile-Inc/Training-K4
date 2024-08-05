package com.example.asian;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseFragment extends Fragment {
    public static final String KEY_COLOR_FRAGMENT = "keyColorFragment";
    public abstract String getFragmentName();

    public static class FragmentActivity extends AppCompatActivity {

        private int mFragmentClickCount = 0;
        private Button mBtnFragmentOne;
        private Button mBtnFragmentTwo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
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

            getSupportFragmentManager().addOnBackStackChangedListener(this::updateTitle);
        }

        private void replaceFragment(FragmentOne fragment) {
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
            }
        }
    }



}
