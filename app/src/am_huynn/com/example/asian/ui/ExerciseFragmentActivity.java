package com.example.asian.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.asian.R;
import com.example.asian.constants.Constants;
import com.example.asian.fragment.OneFragment;
import com.example.asian.fragment.TwoFragment;

public class ExerciseFragmentActivity extends AppCompatActivity implements OneFragment.FragmentOneListener, TwoFragment.FragmentTwoListener {
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;
    private FragmentManager mFragmentManager;
    private int mOnFragmentOneClick = 0;
    private int mOnFragmentTwoClick = 0;
    private static final int MAX_ON_CLICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_fragment);
        initView();
        mFragmentManager = getSupportFragmentManager();
        initListener();
    }

    private void initView() {
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
    }

    private void initListener() {
        mBtnFragmentOne.setOnClickListener(view -> {
            OneFragment oneFragment = new OneFragment();
            Bundle data = new Bundle();
            data.putInt(Constants.KEY_COLOR_FRAGMENT, getResources().getColor(R.color.green_319532));
            oneFragment.setArguments(data);
            if (mOnFragmentOneClick > MAX_ON_CLICK) {
                mFragmentManager.beginTransaction().replace(R.id.frContent, oneFragment).commit();
            } else {
                mFragmentManager.beginTransaction().replace(R.id.frContent, oneFragment).addToBackStack(null).commit();
            }
            mOnFragmentOneClick++;
            mOnFragmentTwoClick = 0;
        });

        mBtnFragmentTwo.setOnClickListener(view -> {
            TwoFragment twoFragment = new TwoFragment();
            Bundle data = new Bundle();
            data.putInt(Constants.KEY_COLOR_FRAGMENT, getResources().getColor(R.color.purple_523051));
            twoFragment.setArguments(data);
            if (mOnFragmentTwoClick > MAX_ON_CLICK) {
                mFragmentManager.beginTransaction().add(R.id.frContent, twoFragment).commit();
            } else {
                mFragmentManager.beginTransaction().add(R.id.frContent, twoFragment).addToBackStack(null).commit();
            }
            mOnFragmentTwoClick++;
            mOnFragmentOneClick = 0;
        });
    }

    @Override
    public void setTitleFragmentOne() {
        setTitle(getString(R.string.fragment_one));
    }

    @Override
    public void setTitleFragmentTwo() {
        setTitle(R.string.fragment_two);
    }
}
