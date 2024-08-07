package com.example.asian;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.asian.fragment.FragmentOne;
import com.example.asian.fragment.FragmentTwo;

public class FragmentActivity extends AppCompatActivity {

    private int mFragmentClickCount;
    private int mLastClickTime = 0;
    private FragmentManager mFragmentManager;
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        initView();
        handleOnclick();
    }

    private void handleOnclick() {
        mBtnFragmentOne.setOnClickListener(view -> {
            deleteBackStack();
            handleFragmentChange(
                    FragmentOne.newInstance("#338837"),
                    "Fragment One");
        });
        mBtnFragmentTwo.setOnClickListener(view -> {
            deleteBackStack();
            handleFragmentChange(
                    FragmentTwo.newInstance("#671063"),
                    "Fragment Two");
        });
    }

    private void initView() {
        mFragmentClickCount = 0;
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
    }

    private void handleFragmentChange(Fragment fragment, String nameBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction(); //Begin transaction
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(nameBackStack);
        fragmentTransaction.commit();
    }

    private void handleBackStackChanged() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount(); // get Total item in list backstack
        if (backStackEntryCount > 0) { // if list more 0
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);  // Get item earlyer
            String fragmentName = backEntry.getName();
            setTitle(fragmentName);
        }
    }

    private void deleteBackStack() {
        mFragmentClickCount++;
        Log.d("FragmentActivity", "mFragmentClickCount: " + mFragmentClickCount); // Log the click count
        if (mFragmentClickCount > 2) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // delete list backstack
            mFragmentClickCount = 0;
        }
        else if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {  //the time that passed after the last time the user clicked the button
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.d("FragmentActivity", "mLastClickTime: " + mLastClickTime); //log clicktime
        }
        mLastClickTime = (int) SystemClock.elapsedRealtime();  //record the time the user last clicked the button validly
    }
}

